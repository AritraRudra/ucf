package in.co.test.ucf.exceptions;

public class UserNameOrIdNotFound extends Exception {
	private static final long serialVersionUID = 276324732548827654L;

	public UserNameOrIdNotFound() {
		super("UserName or ID not found.");
	}

	public UserNameOrIdNotFound(final String message) {
		super(message);
	}
}
