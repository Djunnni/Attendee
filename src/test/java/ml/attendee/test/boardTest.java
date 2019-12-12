package ml.attendee.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ml.attendee.AttendenceAppApplication;
import ml.attendee.repository.ActivityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= AttendenceAppApplication.class)
public class boardTest {
	
	@Autowired
	ActivityRepository actRepo;
	
	@Test
	public void test() {
		
//		Page<Activity> list = actRepo.findAll(); 
	}
}
