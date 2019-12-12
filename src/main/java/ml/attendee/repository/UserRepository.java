package ml.attendee.repository;

import org.springframework.data.repository.CrudRepository;

import ml.attendee.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
