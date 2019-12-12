package ml.attendee.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ml.attendee.domain.Activity;
import ml.attendee.domain.Member;
import ml.attendee.domain.User;
import ml.attendee.encode.Hash;
import ml.attendee.repository.ActivityRepository;
import ml.attendee.repository.MemberRepository;
import ml.attendee.repository.UserRepository;
import ml.attendee.service.ActivityService;
import ml.attendee.service.CsvUtils;

@Controller
@RequestMapping("/member/")
public class MemberController {
	
	@Autowired
	ActivityRepository actRepo;
	@Autowired
	MemberRepository memRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ActivityService actService;
	
	@RequestMapping("/logout")
	public void logout() {
		
	}
	// 관리자 화면
	@GetMapping("/home")
	public void home(Model model,Authentication authentication,@PageableDefault Pageable pageable) throws IOException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Member m = memRepo.findOneByMid(userDetails.getUsername());
		Page<Activity> boardList = actService.getAcitivtyList(pageable,m.getId());
		model.addAttribute("boardList",boardList);
		
	}
	@GetMapping("/code/{url}")
	public String qrcode(HttpServletResponse response,HttpServletRequest request,Authentication authentication,Model model,@PathVariable String url) throws IOException {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Activity result =  actRepo.AuthActivityUser(url, userDetails.getUsername());
			if(result == null) {
				return "member/error";
			}
			String name = result.getAname();
			String currenturl = request.getRequestURL().toString();
			String qrcodeurl = currenturl.replace("/member/code/","/activity/");
			model.addAttribute("qrname",name);
			model.addAttribute("qrcodeurl",qrcodeurl);
			
		return "member/qrcode";
	}
	// 액티비티 화면 띄우기
	@GetMapping("/activity/{url}")
	public String activity(HttpServletResponse response,Model model,@PathVariable String url,Authentication authentication) throws IOException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Activity result =  actRepo.AuthActivityUser(url, userDetails.getUsername());
		
		if(result == null) {
			return "member/error";
		}
		String name = result.getAname();
		LocalDateTime time = result.getRegdate();
		boolean status = result.isFinished();
		List<User> userlist = result.getUsers();
		
		model.addAttribute("userlist",userlist);
		model.addAttribute("activity_status",status);
		model.addAttribute("activity_name",name);
		model.addAttribute("activity_date",time);
		
		return "member/activity";
	}
// QR 코드 삭제화면 만들기 
	@RequestMapping(value="/activity/{url}/delete",method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public int delete(HttpServletResponse response,Model model,@PathVariable String url,Authentication authentication) {
		//현재 로그인한 유저의 정보를 받아옵니다.
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();	
		Activity act =  actRepo.AuthActivityUser(url, userDetails.getUsername());
		if(act == null) {
				return 500;	
		} 
		try {
			actRepo.delete(act);
		} catch(Exception e) {
			return 500;
		} 
		return 200;
	}
	
	@PostMapping(value= {"/activity/{url}/statusChange"})
	@Transactional
	@ResponseBody
	public int ChangeSatusActivity(@PathVariable String url,Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Activity act =  actRepo.AuthActivityUser(url, userDetails.getUsername());
		if(act == null ) {
			return 500;
		} 
		act.setFinished(!act.isFinished());
		try {
		actRepo.save(act);
		} catch(Exception e) {
			return 500;
		}
		return 200;
		
	}
	@GetMapping(value= {"/download/{url}"})
	@Transactional
	public void CSVdownload(@PathVariable String url,HttpServletResponse response,Authentication authentication) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Activity act =  actRepo.AuthActivityUser(url, userDetails.getUsername());
		if(act==null) {
	        response.sendRedirect("/member/home/");
		} else {
			List<User> data = act.getUsers();
	        if(data.isEmpty()) {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('출석한 사람이 없습니다.'); history.back();</script>");
				out.flush();
	        }
	        else { 
	        	response.setContentType("application/vnd.ms-excel");
	        	response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(act.getAname(), "UTF-8")+".xlsx");
		        try {
		        	
		        	XSSFWorkbook objWorkBook = CsvUtils.downloadXlsx(data) ;
		        	OutputStream fileOut = response.getOutputStream();
		        	objWorkBook.write(fileOut);
		            fileOut.close();

		            response.getOutputStream().flush();
		            response.getOutputStream().close();
		        	
		        } catch(IOException e) {
		        	e.printStackTrace();
		        }
	        }
		}
	}
	// 관리자 화면 - 일정 추가 -
	@PostMapping("/home/add")
	@Transactional
	@ResponseBody
	public int addActivity(@RequestParam("name") String name,Hash hash,Authentication authentication) {
		//현재 로그인한 유저의 정보를 받아옵니다.
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		Member m = memRepo.findOneByMid(userDetails.getUsername());
		List<Activity> activityList = m.getActivities();
		
		//activity생성하기
		Activity activity = new Activity();
		LocalDateTime format1 = LocalDateTime.now();
		String md5_url = hash.testMD5(name+userDetails.getUsername()+format1);
		activity.setMember(m);
		activity.setDummyURL(md5_url);
		activity.setAname(name);
		//기존 activity List에 집어넣기 
		activityList.add(activity);
		m.setActivities(activityList);
		//member의 정보에 저장하기 
		memRepo.save(m);
		return 200;
	}
	@GetMapping("/auth")
	@Transactional
	public String auth(@RequestParam("access") String access,@RequestParam("id") String id) {
		try {
			Member m = memRepo.findOneByMid(id);
			String value = m.getEnabled();
			if(value.equals("Y")) {
				return "redirect:/";
			}
			if(value.equals(access)){
				m.setEnabled("Y");
				memRepo.save(m);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "redirect:/?emailAuthFalse";
		}
		return "redirect:/?emailAuth";
	}
}
