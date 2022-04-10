package in.co.test.ucf.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import in.co.test.ucf.exceptions.UCFNotFoundException;
import in.co.test.ucf.models.UserForm;
import in.co.test.ucf.repositories.UserFormRepository;

@Service
public class UCFService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UCFService.class);

	@Autowired
	private UserFormRepository userFormRepository;

	public UserForm create(final UserForm userForm) throws Exception {
		final UserForm savedForm = userFormRepository.save(userForm);
		return savedForm;
	}

	public UserForm getUcfById(final int id) throws UCFNotFoundException {
		return userFormRepository.findById(id).orElseThrow(() -> new UCFNotFoundException("Form with id " + id + " does not exist."));
	}

	// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUcfById(final int id) throws UCFNotFoundException {
		final UserForm form = getUcfById(id);
		userFormRepository.delete(form);

	}
}
