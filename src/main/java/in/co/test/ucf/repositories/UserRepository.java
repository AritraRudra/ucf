package in.co.test.ucf.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import in.co.test.ucf.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Transactional(readOnly = true)
	Optional<User> findByUserName(String userName);

}
