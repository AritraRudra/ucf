package in.co.test.ucf.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import in.co.test.ucf.models.UserForm;
import in.co.test.ucf.utils.Constants;

public interface UserFormRepository extends JpaRepository<UserForm, Integer> {
	@Transactional(readOnly = true)
	Optional<UserForm> findByUserName(String userName);

	@Transactional(readOnly = true)
	Optional<UserForm> findByEmail(String email);

	@Transactional(readOnly = true)
	List<UserForm> findByOrganisation(String organisation);

	@Transactional(readOnly = true)
	List<UserForm> findByCreatedBy(final String createrBy);

	@Transactional(readOnly = true)
	List<UserForm> findByChecker(final String checker);

	@Query(value = "SELECT * from USER_FORM where status in :statusList", nativeQuery = true)
	@Transactional(readOnly = true)
	List<UserForm> findUcfsByStatuses(@Param("statusList") final List<String> statusList);

	@Modifying
	@Transactional
	@Query("update " + Constants.TABLE_USER_FORM + " ucf set ucf.status= ?2, ucf.lastStatusChangedOn = ?3, ucf.lastModifiedOn = ?4 where ucf.id = ?1")
	void updateUcfStatus(final int id, final String status, final LocalDateTime lastStatusChangedOn, final LocalDateTime lastModifiedOn);

	@Query(value = "SELECT * from USER_FORM where createdBy = :createdBy and status in :statusList", nativeQuery = true)
	@Transactional(readOnly = true)
	List<UserForm> getUcfsByStatusesAndMaker(@Param("statusList") final List<String> statusList, @Param("createdBy") final String createdBy);

	@Query(value = "SELECT * from USER_FORM where checker = :checker and status in :statusList", nativeQuery = true)
	@Transactional(readOnly = true)
	List<UserForm> getUcfsByStatusesAndChecker(@Param("statusList") final List<String> statusList, @Param("checker") final String checker);

}
