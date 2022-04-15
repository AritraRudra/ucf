package in.co.test.ucf.constants;


public interface Constants {
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

	// For testing
	public static final String TEST_APPROVER = "testChecker";
}
