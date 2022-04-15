package in.co.test.ucf.models;

import in.co.test.ucf.constants.Constants;

public enum Role {

	ADMIN(Constants.ROLE_ADMIN), CHECKER(Constants.ROLE_CHECKER), MAKER(Constants.ROLE_MAKER);

	private final String value;

	Role(final String type) {
		value = type;
	}

	public String getValue() {
		return value;
	}

}
