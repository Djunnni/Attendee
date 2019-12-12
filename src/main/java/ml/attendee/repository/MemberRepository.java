package ml.attendee.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import ml.attendee.domain.Member;

public interface MemberRepository extends CrudRepository<Member, Long>{

	// id로 계정찾기
	public Collection<Member> findByMid(String mid);
	public Member findOneByMid(String mid);
	
}
