package in.co.test.ucf.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.co.test.ucf.exceptions.CustomFieldValidationException;
import in.co.test.ucf.models.Role;
import in.co.test.ucf.models.User;
import in.co.test.ucf.services.UserService;
import in.co.test.ucf.utils.Constants;

@Controller
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/signup")
	public String signup(final Model model) {
		LOGGER.info("Accesing singup page");
		model.addAttribute("signup", true);
		model.addAttribute("userSignupForm", new User());
		model.addAttribute("roles", Role.values());
		return "user/user-signup";
	}

	@PostMapping("/signup")
	public String signupAction(@Valid @ModelAttribute("userSignupForm") final User user, final BindingResult result, final ModelMap model) {
		LOGGER.info("Creating user : {}, roles {}", user, user.getRoles());
		model.addAttribute("userSignupForm", user);
		model.addAttribute("roles", Role.values());
		if (result.hasErrors()) {
			return "user/user-signup";
		} else {
			try {
				// final Set<Role> rolesSelected = new HashSet<>();
				// Collections.addAll(rolesSelected, roles);
				// user.setRoles(rolesSelected);
				userService.createUser(user);
				LOGGER.info("New User created succesfully.");
				return "redirect:/";
			} catch (final CustomFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
			} catch (final Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
			}
		}
		return Constants.REDIRECT_TO_LOGIN;
	}
}
