package in.co.test.ucf.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.co.test.ucf.constants.Constants;
import in.co.test.ucf.exceptions.UCFNotFoundException;
import in.co.test.ucf.models.UCFStatus;
import in.co.test.ucf.models.UserForm;
import in.co.test.ucf.repositories.UserFormRepository;

@Service
public class UCFService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UCFService.class);

	@Autowired
	private UserFormRepository userFormRepository;

	public UserForm create(final UserForm userForm) throws Exception {
		prepareRequestedUcf(userForm);
		LOGGER.info("Inside create : {}", userForm);
		final UserForm savedForm = userFormRepository.save(userForm);
		return savedForm;
	}

	private void prepareRequestedUcf(final UserForm userForm) {
		//userForm.setCreatedOn(LocalDateTime.now(zone));
		//userForm.setLastModifiedOn(LocalDateTime.now(zone));
		userForm.setApprover(Constants.TEST_APPROVER);
		userForm.setStatus(UCFStatus.PENDING.name());
		userForm.setLastStatusChangedOn(LocalDateTime.now());
	}

	public UserForm getUcfById(final int id) throws UCFNotFoundException {
		return userFormRepository.findById(id).orElseThrow(() -> new UCFNotFoundException("Form with id " + id + " does not exist."));
	}

	// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUcfById(final int id) throws UCFNotFoundException {
		final UserForm form = getUcfById(id);
		userFormRepository.delete(form);
	}

	public List<UserForm> getUcfByLoggedInUserName(final String string) {
		return userFormRepository.findByCreatedBy(string);
	}

	public List<UserForm> getAllUCFUcfs() {
		return userFormRepository.findAll();
	}

	public List<UserForm> getUcfsByStatuses(final List<String> statusList) {
		return userFormRepository.findUcfsByStatuses(statusList);
	}

	public void update(@Valid final UserForm userForm) {
		final UserForm ucfInDb = userFormRepository.getById(userForm.getId());
		updateModifiableProps(userForm, ucfInDb);
		userFormRepository.save(ucfInDb);
	}

	private void updateModifiableProps(final UserForm sourceUcf, final UserForm targetUcf) {
		targetUcf.setOrganisation(sourceUcf.getOrganisation());
		targetUcf.setSystemIPsAsString(sourceUcf.getSystemIPsAsString());
	}

	public void update(final UserForm userForm, final String userName) throws OperationNotSupportedException {
		final UserForm ucfInDb = userFormRepository.getById(userForm.getId());
		if (!ucfInDb.getCreatedBy().equals(userName))
			throw new OperationNotSupportedException(
					"User : " + userName + " does not have the permissin to operate on UCF having ID : " + userForm.getId());
		updateModifiableProps(userForm, ucfInDb);
		userFormRepository.save(ucfInDb);
	}
}
