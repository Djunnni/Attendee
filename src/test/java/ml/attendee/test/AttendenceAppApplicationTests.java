package ml.attendee.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ml.attendee.repository.ActivityRepository;
import ml.attendee.repository.MemberRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AttendenceAppApplicationTests {

	@Autowired
	MemberRepository memRepo;
	@Autowired
	ActivityRepository actRepo;
//	
//	@Test 
//	public void insertMember() {
//		Member member = new Member();
//		member.setMid("admin_topspin1473");
//		member.setMpw("1473");
//
//		Activity activity = new Activity();
//		activity.setAname("1차 정기모임");
//		
//		Hash hash = new Hash();
//		
//		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
//		Date time = new Date();
//		
//		String time1 = format1.format(time);
//		
//		String md5_url = hash.testMD5("1차 정기모임"+time1);
//		
//		activity.setDummyURL(md5_url);
//		activity.setUsers(null);
//		
//		ArrayList<Activity> arr = new ArrayList<Activity>();
//		arr.add(activity);
//		
//		member.setActivities(arr);
//		
//		memRepo.save(member);
//		
//	}
//	@Test
//	public void findActivity() {
//		Collection<Activity> results = actRepo.findByDummyURLLike("2da3642f1502d92918c8e3444bd8424c");
//		results.forEach(e->System.out.println(e));
//	}
}
