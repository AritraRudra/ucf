package in.co.test.ucf.exceptions;

public class UCFNotFoundException extends Exception {
	private static final long serialVersionUID = 1668398822129870029L;

	public UCFNotFoundException() {
		super("Form not found.");
	}

	public UCFNotFoundException(final String message) {
		super(message);
	}
}
