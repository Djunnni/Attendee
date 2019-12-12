package ml.attendee.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude="users")
@Entity
@Table(name="ABapp_activity")
@EqualsAndHashCode(of="ano",callSuper = false)
public class Activity extends BaseTimeEntity{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ano;
	
	// =========== URL hash ======== //
	private String dummyURL;
	// =========== name =========== //
	private String aname;
	// =========== enable ========== //
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean finished;
	
	// =========== user list =========== //
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="activity_id")
	private List<User> users;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
	
}
