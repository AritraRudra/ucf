package in.co.test.ucf.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.co.test.ucf.exceptions.CustomFieldValidationException;
import in.co.test.ucf.exceptions.UCFNotFoundException;
import in.co.test.ucf.models.UserForm;
import in.co.test.ucf.services.UCFService;

@Controller
public class UCFController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UCFController.class);

	private final String TAB_FORM = "formTab";
	private final String TAB_LIST = "listTab";

	@Autowired
	private UCFService ucfService;

	@GetMapping({ "/", "/login" })
	public String getIndex() {
		LOGGER.info("In UCFController.getIndex(), login HTTP GET");
		return "index";
	}

	@PostMapping("/createform")
	public String createform(@Valid @ModelAttribute("userform") final UserForm userForm, final BindingResult result, final Model model) {
		if (result.hasErrors()) {
			baseAttributerForUserForm(model, userForm, TAB_FORM);
		} else {
			try {
				ucfService.create(userForm);
				LOGGER.info("User created succesfully.");
				baseAttributerForUserForm(model, new UserForm(), TAB_LIST);

			} catch (final CustomFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				baseAttributerForUserForm(model, userForm, TAB_FORM);
			} catch (final Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				baseAttributerForUserForm(model, userForm, TAB_FORM);
				LOGGER.info("Error during UCF creation.");
			}
		}
		LOGGER.info("Show user-view page");
		return "user-form/user-view";
	}



	@GetMapping("/getForm/{id}")
	public String getFormById(final Model model, @PathVariable(name = "id") final int id) throws Exception {
		final UserForm ucf = ucfService.getUcfById(id);
		LOGGER.info("Show ucf-read page.");
		baseAttributerForUserForm(model, ucf, TAB_FORM);
		return "user-form/user-view";
	}

	// TODO which to use ? get or post and how ?
	// @PostMapping("/editForm/{id}")
	@GetMapping("/updateForm/{id}")
	public String updateForm(final Model model, @PathVariable(name = "id") final int id) throws Exception {
		final UserForm ucfToEdit = ucfService.getUcfById(id);
		LOGGER.info("Show ucf-edit page.");
		baseAttributerForUserForm(model, ucfToEdit, TAB_FORM);
		model.addAttribute("updateMode", "true");
		// model.addAttribute("passwordForm", new ChangePasswordForm(id));

		return "user-form/user-view";
	}

	// TODO Handle deletion using ID without proper role
	@GetMapping("/deleteUser/{id}")
	public String deleteForm(final Model model, @PathVariable(name = "id") final int id) {
		try {
			ucfService.deleteUcfById(id);
			LOGGER.info("User deleted successfully.");
		} catch (final UCFNotFoundException ucfnfEx) {
			model.addAttribute("listErrorMessage", ucfnfEx.getMessage());
		}
		return userForm(model);
	}

	private void baseAttributerForUserForm(final Model model, final UserForm userForm, final String activeTab) {
		model.addAttribute("userForm", userForm);
		// model.addAttribute("userList", ucfService.getAllUsers());
		model.addAttribute(activeTab, "active");
	}

	@GetMapping("/createform")
	public String userForm(final Model model) {
		baseAttributerForUserForm(model, new UserForm(), TAB_LIST);
		return "user-form/user-view";
	}
}

