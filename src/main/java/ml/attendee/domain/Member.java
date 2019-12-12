package ml.attendee.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude= {"activities","roles"})
@Entity
@Table(name="ABapp_members")
@EqualsAndHashCode(of="id",callSuper = false)
public class Member extends BaseTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 비밀번호 , 아이디
	private String mid;
	private String mpw;  
	
	// 인증받은 여부 (이름과 이메일 그리고 생성시간을 Hash 화 합니다.)
	private String enabled;
	// 이메일
	private String email;
	
//	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
//	@JoinColumn(name="memno")
//	private List<Activity> activities;
//	
	@OneToMany(mappedBy="member" ,cascade=CascadeType.ALL)
	private List<Activity> activities;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="member_id")
	private List<MemberRole> roles;
	
}
