package ml.attendee.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ml.attendee.AttendenceAppApplication;
import ml.attendee.aws.ses.Sender;
import ml.attendee.aws.ses.SenderDto;
import ml.attendee.repository.ActivityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= AttendenceAppApplication.class)
public class emailTest {
	
	@Autowired
	ActivityRepository actRepo;
	
//	@Test
//	public void test() {
//		if(actRepo.AuthActivityUser("89eb2de7fa9ba9514324092ca65c2262","test")) {
//			System.out.println("aaa");
//		} else {
//			System.out.println("bbb");			
//		}
//	}
}
