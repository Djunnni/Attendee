package ml.attendee.aws.ses;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import groovy.transform.builder.Builder;
import lombok.Getter;

@Getter
public class SenderDto {

    private String from ="";
    private String to;
    private String subject="";
    private String content="<!DOCTYPE html>\n" + 
    		"<html xmlns:th=\"http://www.thymeleaf.org\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
    		"<html lang=\"en\">\n" + 
    		"<head>\n" + 
    		"  <meta charset=\"UTF-8\">\n" + 
    		"  <title></title>\n" + 
    		"</head>\n" + 
    		"<body>\n" + 
    		"  <p>tmp</p>\n" + 
    		"  <p>tmp</p>\n" + 
    		"  <br><p><a href=";

    
    @Builder
    public SenderDto(String url,String to,String content) {
        this.to = to;
        this.content = this.content+url+content+">여기</a>를 눌러 인증을 해주시기 바랍니다.</p></body></html>";
    }

    public SendEmailRequest toSendRequestDto(){
        Destination destination = new Destination()
                .withToAddresses(this.to);

        Message message = new Message()
                .withSubject(createContent(this.subject))
                .withBody(new Body()
                        .withHtml(createContent(this.content))); // content body는 HTML 형식으로 보내기 때문에 withHtml을 사용합니다.

        return new SendEmailRequest()
                .withSource(this.from)
                .withDestination(destination)
                .withMessage(message);
    }

    private Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }
}