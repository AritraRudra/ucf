package in.co.test.ucf.models;

public enum Role {

	ADMIN("ADMIN"), MAKER("MAKER"), CHECKER("CHECKER");

	private final String value;

	Role(final String type) {
		value = type;
	}

	public String getValue() {
		return value;
	}

}
