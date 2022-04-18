package in.co.test.ucf.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import in.co.test.ucf.exceptions.CustomFieldValidationException;
import in.co.test.ucf.exceptions.UserNameOrIdNotFound;
import in.co.test.ucf.models.User;
import in.co.test.ucf.repositories.UserRepository;
import in.co.test.ucf.utils.Constants;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	private boolean checkUserNameAvailablility(final User user) throws Exception {
		final Optional<User> userFound = userRepository.findByUserName(user.getUserName());
		if (userFound.isPresent()) {
			throw new CustomFieldValidationException("Username not available", "username");
		}
		return true;
	}

	// Password validation and encoding to be done at front-end
	public User createUser(User user) throws Exception {
		if (checkUserNameAvailablility(user)) {
			user = userRepository.save(user);
		}
		return user;
	}

	public User getUserById(final int id) throws UserNameOrIdNotFound {
		return userRepository.findById(id).orElseThrow(() -> new UserNameOrIdNotFound("User id does not exist."));
	}

	public User updateUser(final User fromUser) throws Exception {
		final User toUser = getUserById(fromUser.getId());
		mapUser(fromUser, toUser);
		return userRepository.save(toUser);
	}

	/**
	 * Map everything but the password.
	 *
	 */
	private void mapUser(final User from, final User to) {
		to.setUserName(from.getUserName());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
	public void deleteUser(final int id) throws UserNameOrIdNotFound {
		final User user = getUserById(id);
		userRepository.delete(user);
	}

	private User getCurrentUser() throws Exception {
		// Get the logged in user
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserDetails loggedUser = null;
		// Verify that this fetched session object is the user
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}

		final User currentUser = userRepository.findByUserName(loggedUser.getUsername())
				.orElseThrow(() -> new Exception("Error getting the logged in user from the session."));
		return currentUser;
	}
}
