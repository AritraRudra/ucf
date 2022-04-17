package in.co.test.ucf.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.co.test.ucf.exceptions.CustomFieldValidationException;
import in.co.test.ucf.exceptions.UCFNotFoundException;
import in.co.test.ucf.models.UCFStatus;
import in.co.test.ucf.models.UserForm;
import in.co.test.ucf.services.UCFService;
import in.co.test.ucf.utils.Constants;

@Controller
public class UCFController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UCFController.class);

	private static final String TAB_FORM = "formTab";
	private static final String TAB_LIST = "listTab";
	private static final String USER_FORM = "userForm";
	private static final String LIST_ERROR_MESSAGE = "listErrorMessage";
	private static final String FORM_ERROR_MESSAGE = "formErrorMessage";
	private static final String FORMS_LIST = "formsList";
	private static final String APPLIED_FORMS_LIST = "appliedFormsList";
	private static final String PENDING_FORMS_LIST = "pendingFormsList";
	private static final String EDIT_MODE = "editMode";
	private static final String DISABLE_FIELDS = "disableFields";

	private static final List<String> statusList = Stream.of(UCFStatus.PENDING.name(), UCFStatus.RESUBMITTED.name(), UCFStatus.IN_REVIEW.name())
			.collect(Collectors.toList());

	@Autowired
	private UCFService ucfService;

	@GetMapping({ "/", "/login" })
	public String getIndex() {
		LOGGER.info("In UCFController.getIndex(), login HTTP GET");
		return "index";
	}

	@RequestMapping(value = "/logout")
	public String logout(final HttpServletRequest request, final HttpServletResponse response) {
		final CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(
				AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
		cookieClearingLogoutHandler.logout(request, response, null);
		new SecurityContextLogoutHandler().logout(request, response, null);
		return Constants.REDIRECT_TO_LOGIN;
	}

	//@PreAuthorize("hasRole('ROLE_MAKER')")
	@GetMapping(Constants.VERIFY_ROLE_AND_FORWARD)
	public String verifyRoleAndForward(final Model model) {
		// baseAttributerForUserForm(model, new UserForm(), TAB_LIST);
		// baseAttributerForUserForm(model, new UserForm(), TAB_FORM);
		model.addAttribute(TAB_FORM, TAB_FORM);
		//model.addAttribute("appliedFormsList", ucfService.getUcfByLoggedInUserName("testMaker"));
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
			return viewResolutionByRole(model, auth);
		return "index";
	}

	private String viewResolutionByRole(final Model model, final Authentication auth) {
		LOGGER.info("Got User : {}, Role(s) : {}", auth.getName(), auth.getAuthorities());
		// auth.getAuthorities().forEach(System.out::println);
		// First check if MAKER then CHECKER and then ADMIN in case of user with
		// multiple ROLES
		if (auth.getAuthorities().stream().anyMatch(a -> Constants.ROLE_MAKER.equals(a.getAuthority()))) {
			LOGGER.info("Got User : {}, Role : {}", auth.getName(), Constants.ROLE_MAKER);
			baseAttributerForUserForm(model, new UserForm(), TAB_LIST);
			model.addAttribute(APPLIED_FORMS_LIST, ucfService.getUcfsByMaker(auth.getName()));
			return "maker/maker-view";
		} else if (auth.getAuthorities().stream().anyMatch(a -> Constants.ROLE_CHECKER.equals(a.getAuthority()))) {
			LOGGER.info("Got User : {}, Role : {}", auth.getName(), Constants.ROLE_CHECKER);
			baseAttributerForUserForm(model, new UserForm(), TAB_LIST);
			model.addAttribute(FORMS_LIST, ucfService.getUcfsByStatusesAndChecker(statusList, auth.getName()));
			return "checker/checker-view";
		} else if (auth.getAuthorities().stream().anyMatch(a -> Constants.ROLE_ADMIN.equals(a.getAuthority()))) {
			LOGGER.info("Got User : {}, Role : {}", auth.getName(), Constants.ROLE_ADMIN);
			return "admin/admin-view";
		}
		return "index";
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_MAKER + "')")
	@GetMapping("/getucf")
	public String getUcf(@Valid @ModelAttribute(USER_FORM) final UserForm userForm, final BindingResult result, final Model model) {
		LOGGER.info("Show maker-form page");
		return "maker/maker-form";
	}

	// @PreAuthorize("hasRole('ROLE_MAKER')")
	@PostMapping("/createform")
	public String createFormByMaker(@Valid @ModelAttribute(USER_FORM) final UserForm userForm, final BindingResult result, final Model model) {
		// LOGGER.info("Inside createFormByMaker : {}, {}", auth.getName(), userForm);
		if (result.hasErrors()) {
			LOGGER.warn("Inside createFormByMaker, still has errors : {}", userForm);
			// baseAttributerForUserForm(model, userForm, TAB_FORM);
			model.addAttribute(USER_FORM, userForm);
		} else {
			try {
				final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				userForm.setCreatedBy(auth.getName());
				ucfService.create(userForm);
				LOGGER.info("UCF created succesfully.");
				// baseAttributerForUserForm(model, new UserForm(), TAB_LIST);
				// model.addAttribute(APPLIED_FORMS_LIST, ucfService.getUcfByLoggedInUserName(auth.getName()));
				// return "maker/maker-view";
				return "redirect:" + Constants.VERIFY_ROLE_AND_FORWARD;
			} catch (final CustomFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				// baseAttributerForUserForm(model, userForm, TAB_FORM);
				model.addAttribute(USER_FORM, userForm);
			} catch (final Exception e) {
				model.addAttribute(FORM_ERROR_MESSAGE, e.getMessage());
				// baseAttributerForUserForm(model, userForm, TAB_FORM);
				model.addAttribute(USER_FORM, userForm);
				LOGGER.error("Error during UCF creation.");
			}
		}
		return "maker/maker-form";
	}


	@PreAuthorize("hasRole('" + Constants.ROLE_MAKER + "')")
	@GetMapping("/editform/{id}")
	public String getEditUcf(final Model model, @PathVariable(name = "id") final int id) throws Exception {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final UserForm ucfToEdit = ucfService.getUcfById(id);
		// Validate if current user is the creator
		if (!ucfToEdit.getCreatedBy().equals(auth.getName()))
			return Constants.REDIRECT_TO_LOGIN;

		// model.addAttribute(EDIT_MODE, Boolean.TRUE.toString());
		// model.addAttribute(DISABLE_FIELDS, Boolean.TRUE.toString());
		model.addAttribute(USER_FORM, ucfToEdit);
		LOGGER.info("Show maker-edit page.");
		return "maker/maker-edit";
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_MAKER + "')")
	@PostMapping("/editform")
	public String editUcf(@Valid @ModelAttribute(USER_FORM) final UserForm userForm, final BindingResult result, final Model model) {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return Constants.REDIRECT_TO_LOGIN;
		LOGGER.info("Inside editUcf, current UCF : {}", userForm);
		/*
		if (!userForm.getCreatedBy().equals(auth.getName()))
			return Constants.REDIRECT_TO_LOGIN;
		 */
		if (result.hasErrors()) {
			LOGGER.warn("Inside editUcf, still has errors : {}", userForm);
			// model.addAttribute(EDIT_MODE, Boolean.TRUE.toString());
			// model.addAttribute(DISABLE_FIELDS, Boolean.TRUE.toString());
			model.addAttribute(USER_FORM, userForm);
			return "maker/maker-edit";
		} else {
			try {
				ucfService.update(userForm, auth.getName());
				// baseAttributerForUserForm(model, new UserForm(), TAB_LIST);
				LOGGER.info("Form updated successfully.");
				return "redirect:" + Constants.VERIFY_ROLE_AND_FORWARD;
			} catch (final Exception e) {
				LOGGER.error("Error during UCF updation, {}" + e.getMessage());
				model.addAttribute(FORM_ERROR_MESSAGE, e.getMessage());
				// model.addAttribute(EDIT_MODE, Boolean.TRUE.toString());
				// model.addAttribute(DISABLE_FIELDS, Boolean.TRUE.toString());
				model.addAttribute(USER_FORM, userForm);
			}
		}
		return "maker/maker-view";
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_MAKER + "')")
	@GetMapping("/editform/cancel")
	public String cancelEditUcf(final Model model) {
		return "redirect:" + Constants.VERIFY_ROLE_AND_FORWARD;
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_MAKER + "')")
	@GetMapping("/deleteform/{id}")
	public String deleteUcfById(final Model model, @PathVariable(name = "id") final int id) {
		try {
			final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			final UserForm ucfToEdit = ucfService.getUcfById(id);
			// Validate if current user is the creator
			if (!ucfToEdit.getCreatedBy().equals(auth.getName()))
				return Constants.REDIRECT_TO_LOGIN;
			ucfService.deleteUcfById(id);
			LOGGER.info("UCF request deleted successfully.");
		} catch (final UCFNotFoundException ucfnfEx) {
			model.addAttribute(LIST_ERROR_MESSAGE, ucfnfEx.getMessage());
		}
		return "redirect:" + Constants.VERIFY_ROLE_AND_FORWARD;
	}

	private void baseAttributerForUserForm(final Model model, final UserForm userForm, final String activeTab) {
		model.addAttribute(USER_FORM, userForm);
		// model.addAttribute("userList", ucfService.getAllUsers());
		model.addAttribute(activeTab, "active");
	}

	@PreAuthorize("hasAnyRole('" + Constants.ROLE_MAKER + "', '" + Constants.ROLE_CHECKER + "' )")
	@GetMapping("/viewform/{id}")
	public String viewUcf(final Model model, @PathVariable(name = "id") final int id) throws Exception {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LOGGER.info("Inside viewUcf, current ID : {}, user : {}", id, auth.getName());
		final UserForm ucfToView = ucfService.getUcfById(id);
		// Validate if current user is the creator or approver
		if (ucfToView.getCreatedBy().equals(auth.getName()) || ucfToView.getChecker().equals(auth.getName())) {
			model.addAttribute(USER_FORM, ucfToView);
			LOGGER.info("Show ucf-view page.");
			return "common/ucf-view";
		} else
			return Constants.REDIRECT_TO_LOGIN;
	}

	// Checker related operations
	@PreAuthorize("hasRole('" + Constants.ROLE_CHECKER + "')")
	@PostMapping("/viewallwithChecker")
	public String viewAllUcfsWithChecker(final Model model, @RequestParam(value = "user_name") final String user_name) throws Exception {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LOGGER.info("Inside viewAllUcfsWithChecker, sent user_name : {}, context user : {}", user_name, auth.getName());
		if(!auth.getName().equals(user_name))
			return Constants.REDIRECT_TO_LOGIN;
		baseAttributerForUserForm(model, new UserForm(), TAB_LIST);
		model.addAttribute(FORMS_LIST, ucfService.getUcfsByChecker(auth.getName()));
		return "checker/checker-view";
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_CHECKER + "')")
	@GetMapping("/approveform/{id}")
	public String approveUcf(final Model model, @PathVariable(name = "id") final int id) throws Exception {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LOGGER.info("Inside approveUcf, current ID : {}, user : {}", id, auth.getName());
		final UserForm ucfToApprove = ucfService.getUcfById(id);
		// Validate if current user is the creator or approver
		if (ucfToApprove.getChecker().equals(auth.getName())) {
			ucfService.updateUcfStatus(id, UCFStatus.APPROVED);
			LOGGER.info("UCF with id {} approved by {}.", id, auth.getName());
			return "redirect:" + Constants.VERIFY_ROLE_AND_FORWARD;
		} else
			return Constants.REDIRECT_TO_LOGIN;
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_CHECKER + "')")
	@GetMapping("/rejectform/{id}")
	public String rejectUcf(final Model model, @PathVariable(name = "id") final int id) throws Exception {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LOGGER.info("Inside rejectUcf, current ID : {}, user : {}", id, auth.getName());
		final UserForm ucfToApprove = ucfService.getUcfById(id);
		// Validate if current user is the creator or approver
		if (ucfToApprove.getChecker().equals(auth.getName())) {
			ucfService.updateUcfStatus(id, UCFStatus.REJECTED);
			LOGGER.info("UCF with id {} rejected by {}.", id, auth.getName());
			return "redirect:" + Constants.VERIFY_ROLE_AND_FORWARD;
		} else
			return Constants.REDIRECT_TO_LOGIN;
	}

	@PreAuthorize("hasRole('" + Constants.ROLE_CHECKER + "')")
	@PostMapping("/updatestatusform")
	public String changeStatusByChecker(@ModelAttribute(USER_FORM) final UserForm userForm, final Model model) {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return Constants.REDIRECT_TO_LOGIN;
		LOGGER.info("Inside changeStatusByChecker, current UCF : {}", userForm);
		/*
		if (!userForm.getApprover().equals(auth.getName()))
			return Constants.REDIRECT_TO_LOGIN;
		 */
		try {
			ucfService.update(userForm, auth.getName());
			LOGGER.info("Form updated successfully.");
		} catch (final Exception e) {
			LOGGER.error("Error during UCF status updation, {}" + e.getMessage());
			model.addAttribute(FORM_ERROR_MESSAGE, e.getMessage());
			model.addAttribute(USER_FORM, userForm);
		}
		return "redirect:" + Constants.VERIFY_ROLE_AND_FORWARD;
	}

}
