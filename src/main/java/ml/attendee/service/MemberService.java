package ml.attendee.service;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ml.attendee.aws.ses.Sender;
import ml.attendee.aws.ses.SenderDto;
import ml.attendee.domain.Member;
import ml.attendee.domain.MemberRole;
import ml.attendee.repository.MemberRepository;

@Component
public class MemberService {

	@Autowired
	MemberRepository memberRepository;
	
	@Transactional
	public String create(String url,String uemail,String mid,String mpw, String rpw) {
		// 아이디 중복 체크 
		if(memberRepository.findByMid(mid).size() > 0) {
			return "idExist";
		}
		// 비밀번호 중복 체크 
		if(!mpw.equals(rpw)) {
			System.out.println("password is not equal");
			return "passwordNotEqual";
		}
		StringBuilder sb = new StringBuilder(uemail);
		sb.append(mid);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String BcryptText = passwordEncoder.encode(sb.toString());
		// HASH HASH
		
		// ======== 메일 인증 보내기 ========= //
		Sender sender = new Sender();
		String uri = "access="+BcryptText+"&id="+mid;
		SenderDto dto = new SenderDto(url,uemail,uri);
		if(sender.send(dto)) {
			Member member = new Member();
			member.setEmail(uemail);
			member.setMid(mid);
			member.setMpw(new BCryptPasswordEncoder().encode(mpw));
			// role 만들기
			MemberRole role = new MemberRole();
			role.setRoleName("ROLE_MEMBER");
			member.setRoles(Arrays.asList(role));
			member.setEnabled(BcryptText);
			
			memberRepository.save(member);
			return "success";
		};
		// =============================== //
		
		return "emailError";
	}
	
}
