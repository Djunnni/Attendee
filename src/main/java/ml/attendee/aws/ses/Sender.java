package ml.attendee.aws.ses;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;


public class Sender {
	
    public boolean send(SenderDto senderDto){
        try {
        	System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
        	
        	// ===================== AWS Crediential 추가할 것!! ===================== //
        	BasicAWSCredentials awsCreds = new BasicAWSCredentials("","");
            AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCreds);
            
           try {
                credentialsProvider.getCredentials();
            } catch (Exception e) {
                throw new AmazonClientException(
                        "WARNNING :: Check application.properties AWSAccessKeyId and AWSSecretKey\nthen verify your key!!",
                        e);
            }

            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion("us-east-1")
                    .build();

            // Send the email.
            client.sendEmail(senderDto.toSendRequestDto());
            System.out.println("Email sent!");

        } catch (Exception ex) {
        	System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
            return false;
        }
        return true;
    }
}