package in.co.test.ucf.exceptions;

public class CustomFieldValidationException extends Exception {
	private static final long serialVersionUID = 23684325435853255L;

	private final String fieldName;

	public CustomFieldValidationException(final String message, final String fieldName) {
		super(message);
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
