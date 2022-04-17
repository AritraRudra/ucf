package in.co.test.ucf.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.OperationNotSupportedException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.co.test.ucf.exceptions.UCFNotFoundException;
import in.co.test.ucf.models.UCFStatus;
import in.co.test.ucf.models.UserForm;
import in.co.test.ucf.repositories.UserFormRepository;
import in.co.test.ucf.utils.Constants;
import in.co.test.ucf.utils.Utils;

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
		userForm.setChecker(Constants.TEST_CHECKER);
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

	public List<UserForm> getUcfsByMaker(final String maker) {
		return userFormRepository.findByCreatedBy(maker);
	}

	public List<UserForm> getUcfsByChecker(final String checker) {
		return userFormRepository.findByChecker(checker);
	}

	public List<UserForm> getAllUCFUcfs() {
		return userFormRepository.findAll();
	}

	public List<UserForm> getUcfsByStatuses(final List<String> statusList) {
		return userFormRepository.findUcfsByStatuses(statusList);
	}

	public List<UserForm> getUcfsByStatusesAndMaker(final List<String> statusList, final String maker) {
		return userFormRepository.getUcfsByStatusesAndMaker(statusList, maker);
	}

	public List<UserForm> getUcfsByStatusesAndChecker(final List<String> statusList, final String checker) {
		return userFormRepository.getUcfsByStatusesAndChecker(statusList, checker);
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

	public void updateUcfStatus(final int id, final UCFStatus status) {
		final LocalDateTime now = Utils.getCurrentLocalDateTime();
		userFormRepository.updateUcfStatus(id, status.name(), now, now);
	}

	@PostConstruct
	public void postConstruct() throws Exception {
		LOGGER.info("In postConstruct");
		for (int i = 1; i <= 100; i++) {
			userFormRepository.saveAndFlush(UCFService.generateDummyUcf(i));
		}
	}

	private static UserForm generateDummyUcf(final int index) {
		final UserForm ucf = new UserForm();
		final String prepend = index + "_test_";
		if (index % 4 == 0) {
			ucf.setChecker(Constants.TEST_CHECKER);
			ucf.setCreatedBy(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
			ucf.setStatus(UCFStatus.APPROVED.name());
		} else if (index % 5 == 0) {
			ucf.setChecker(Constants.TEST_CHECKER);
			ucf.setCreatedBy(Constants.TEST_MAKER);
			ucf.setStatus(UCFStatus.PENDING.name());
		} else if (index % 7 == 0) {
			ucf.setChecker(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
			ucf.setCreatedBy(Constants.TEST_MAKER);
			ucf.setStatus(Utils.generateRandomUcfStatus().name());
		} else if (index % 11 == 0) {
			ucf.setChecker(Constants.TEST_CHECKER);
			ucf.setCreatedBy(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
			ucf.setStatus(Utils.generateRandomUcfStatus().name());
		} else {
			ucf.setChecker(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
			ucf.setCreatedBy(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
			ucf.setStatus(Utils.generateRandomUcfStatus().name());
		}
		ucf.setCreatedOn(Utils.getCurrentLocalDateTime());
		ucf.setEmail(
				prepend + Utils.generateRandomAlphanumericStringOfLength(3) + "@" + Utils.generateRandomAlphanumericStringOfLength(2) + "mail.com");
		ucf.setFirstName(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
		ucf.setLastName(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
		ucf.setLastStatusChangedOn(Utils.getCurrentLocalDateTime());
		ucf.setOrganisation(prepend + Utils.generateRandomAlphanumericStringOfLength(5));
		ucf.setSystemIPsAsString(prepend + Utils.generateRandomAlphanumericStringOfLength(8));
		ucf.setUserName(prepend + Utils.generateRandomAlphanumericStringOfLength(4));
		return ucf;
	}

}

