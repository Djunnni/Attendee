package ml.attendee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ml.attendee.domain.Activity;
import ml.attendee.repository.ActivityRepository;

@Service
public class ActivityService {
	
	@Autowired
	ActivityRepository actRepo;
	
	public Page<Activity> getAcitivtyList(Pageable pageable,Long num) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 5, new Sort(Sort.Direction.DESC, "regdate")); 

        return actRepo.findAllByMemberId(pageable,num);
    }
}
