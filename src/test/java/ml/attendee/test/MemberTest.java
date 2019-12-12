package ml.attendee.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import ml.attendee.AttendenceAppApplication;
import ml.attendee.repository.ActivityRepository;
import ml.attendee.repository.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= AttendenceAppApplication.class)
@Commit
public class MemberTest {
	@Autowired
	MemberRepository repo;
	@Autowired
	ActivityRepository actRepo;
	
	@Test
	public void testInsert() {
	
	}
	
}
