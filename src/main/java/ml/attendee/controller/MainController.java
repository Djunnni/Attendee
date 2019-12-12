package ml.attendee.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ml.attendee.domain.Member;
import ml.attendee.repository.MemberRepository;
import ml.attendee.service.MemberService;

@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	MemberRepository memRepo;

	@Autowired
	MemberService memberService;

	// 메인 화면 소개
	@GetMapping(value = { "", "index" })
	public String index(Authentication authentication) {
		if (authentication != null) {

			return "redirect:/member/home";
		}
		return "index";
	}

	@GetMapping("{param}")
	public String statusAlam(@PathVariable("param") String param, HttpServletResponse response) throws IOException {
		String script = "";
		boolean run = false;
		switch (param) {
		case "loginFail":
			script = "로그인에 실패하였습니다. 확인해주세요 :)";
			run = true;
			break;
		case "signupSuccess":
			script = "회원가입 되었습니다. 감사합니다 :)";
			run = true;
			break;
		case "signupFail":
			script = "회원가입 실패하였습니다. 다시 시도해주세요. :)";
			run = true;
			break;
		case "emailAuthFail":
			script = "이메일 인증에 실패하였습니다.\\n관리자에게 문의바랍니다.";
			run = true;
			break;
		case "emailAuth":
			script = "이메일 인증에 성공하셨습니다.\\n감사합니다.";
			run = true;
			break;
		case "emailAuthDone":
			script = "이미 이메일 인증을 하셨습니다.\\n감사합니다.";
			run = true;
			break;
		default:
			break;
		}
		if (run) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('" + script + "'); location.href=\"/\";</script>");
			out.flush();
		}
		return "index";
	}

	// 거부화면
	@GetMapping("error")
	public void denied() throws IOException {

	}

	// 회원가입
	@GetMapping("signup")
	public void signup(@RequestParam(value = "error", required = false) String error, Model model) throws IOException {
		model.addAttribute("error", error);
	}

	// 개인정보 처리방침
	@GetMapping("private")
	public String PrivatePage() throws IOException {
		return "private";
	}

	// 회원가입 -멤버 추가-
	@PostMapping("signup/add")
	public String addMember(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("utf-8");

		String uemail = request.getParameter("uemail");
		String uid = request.getParameter("uid");
		String upw = request.getParameter("upw");
		String re_upw = request.getParameter("re_upw");
		if (uemail == null || uid == null || upw == null || re_upw == null) {
			return "redirect:/signup?error=";
		}
		String path = request.getRequestURL().toString();
		path = path.replace("/signup/add", "/auth?");
		String type = memberService.create(path,uemail, uid, upw, re_upw);
		if (type.equals("success")) {
			return "redirect:/signupSuccess";
		}
		return "redirect:/signup?error=" + type;
	}

	// login
	@RequestMapping("/login")
	public void login() {

	}

	// 크롤링 robots.txt
	@GetMapping(value = { "/robots.txt", "/robot.txt" })
	public String robot() throws IOException {
		return "robots";
	}

	// favicon
	@GetMapping(value = { "/favicon.ico" })
	public String favicon() throws IOException {
		return "forward:/imgs/favicon.ico";
	}

	// 아이디 인증
	@GetMapping("/auth")
	public String auth(@RequestParam(value = "access", required = false) String access,
			@RequestParam(value = "id", required = false) String id) {
		if (access == null || id == null) {
			return "redirect:/";
		}
		try {
			Member m = memRepo.findOneByMid(id);
			String value = m.getEnabled();
			if (value.equals("Y")) {
				return "redirect:/emailAuthDone";
			}
			if (value.equals(access)) {
				m.setEnabled("Y");
				memRepo.save(m);
			}
			System.out.println(access + " " + m.getEnabled());
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/emailAuthFail";
		}
		return "redirect:/emailAuth";
	}
//  소개화면  
//	@GetMapping("info")
//	public void info() throws IOException {
//		
//	}
}
