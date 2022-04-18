package in.co.test.ucf.utils;

import java.time.format.DateTimeFormatter;

public interface Constants {
	// Tables
	public static final String TABLE_USER_FORM = "UserForm";
	public static final String TABLE_USER = "User";

	// Date related
	public static final String TIME_ZONE_IST = "Asia/Kolkata";
	public static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
	public static final String DEFAULT_DATE_TIME_FORMAT = "dd-MMM-yyyy HH:mm:ss";
	public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
	public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

	// Roles
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_MAKER = "ROLE_MAKER";
	public static final String ROLE_CHECKER = "ROLE_CHECKER";

	// UCF Status
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_APPROVED = "APPROVED";
	public static final String STATUS_REJECTED = "REJECTED";
	public static final String STATUS_IN_REVIEW = "IN_REVIEW";
	public static final String STATUS_RESUBMITTED = "RESUBMITTED";
	public static final String STATUS_REVIEWED_UPDATE_REQUIRED = "REVIEWED_UPDATE_REQUIRED";

	// Rest End Points
	public static final String VERIFY_ROLE_AND_FORWARD = "/verifyroleandforward";
	public static final String REDIRECT_TO_LOGIN = "redirect:/login?logout";

	// For testing
	public static final String TEST_MAKER = "testMaker";
	public static final String TEST_CHECKER = "testChecker";
}
