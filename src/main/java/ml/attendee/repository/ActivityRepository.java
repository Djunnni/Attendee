package ml.attendee.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ml.attendee.domain.Activity;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

	public Page<Activity> findAll(Pageable pageable);
	
	public Collection<Activity> findByDummyURLLike(String url);
	public Activity findOneByDummyURLLike(String url);

	// 생성날짜 거꾸로
	public Collection<Activity> findAllByOrderByRegdateDesc();

	public Page<Activity> findAllByMemberId(Pageable pageable,Long id);
	
	@Query("select act from Activity act inner join act.member u where act.dummyURL =:url and u.mid=:id")
	public Activity AuthActivityUser(@Param("url") String dummyurl,@Param("id") String mid);
}

