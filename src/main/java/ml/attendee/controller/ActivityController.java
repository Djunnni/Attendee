package ml.attendee.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ml.attendee.domain.Activity;
import ml.attendee.domain.User;
import ml.attendee.repository.ActivityRepository;
import ml.attendee.repository.UserRepository;

@Controller
@RequestMapping("/activity/")
public class ActivityController {

	@Autowired
	ActivityRepository actRepo;
	@Autowired
	UserRepository userRepo;
	
	@GetMapping("/{url}")
	public String index(HttpServletResponse response,
						@PathVariable String url,
						@CookieValue(value="storeActCookie", required = false) Cookie storeActCookie,
						Activity activity
						) throws IOException {
		Activity result =  actRepo.findOneByDummyURLLike(url);

		if(result == null || result.isFinished()==true) {
			return "/activity/error";
		}
		else {
				activity.setAno(result.getAno());
				activity.setAname(result.getAname());
				activity.setRegdate(result.getRegdate());
				activity.setDummyURL(result.getDummyURL());
				activity.setUsers(result.getUsers());
				activity.setFinished(result.isFinished());
		}
		if(storeActCookie != null) {
			if(storeActCookie.getValue().equals(activity.getDummyURL())) {
				return "/activity/done";
			}
		}
		
		return "/activity/index";
	}
	
	@PostMapping("/{url}")
	@Transactional
	@ResponseBody
	public int attendUser(User user,
						 @RequestParam("name") String name,
						 @PathVariable String url,
						 HttpServletResponse response) {
		user.setUsername(name);
		
		Activity result =  actRepo.findOneByDummyURLLike(url);
		if(result == null ) {
			return 500;
		}
		else {
			user.setRegdate(LocalDateTime.now());
			List<User> userlist = result.getUsers();
			userlist.add(user);
			result.setUsers(userlist);
			actRepo.save(result);
			
			Cookie storeActCookie = new Cookie("storeActCookie",url);
			storeActCookie.setPath("/activity");
			storeActCookie.setMaxAge(60 * 60 * 24); //20분 타이머
			response.addCookie(storeActCookie);
			
			return 200;
		}
	}
	
}
