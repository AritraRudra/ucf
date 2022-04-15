package in.co.test.ucf.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import in.co.test.ucf.models.UserForm;

public interface UserFormRepository extends JpaRepository<UserForm, Integer> {
	@Transactional(readOnly = true)
	Optional<UserForm> findByUserName(String userName);

	@Transactional(readOnly = true)
	Optional<UserForm> findByEmail(String email);

	@Transactional(readOnly = true)
	List<UserForm> findByOrganisation(String organisation);

	@Transactional(readOnly = true)
	List<UserForm> findByCreatedBy(String creatorUserName);

	@Query(value = "SELECT * from USER_FORM where status in :statusList", nativeQuery = true)
	@Transactional(readOnly = true)
	List<UserForm> findUcfsByStatuses(@Param("statusList") final List<String> statusList);

}
