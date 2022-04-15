package in.co.test.ucf.models;

import in.co.test.ucf.constants.Constants;

public enum UCFStatus {
	PENDING(Constants.STATUS_PENDING), REJECTED(Constants.STATUS_REJECTED), APPROVED(Constants.STATUS_APPROVED),
	IN_REVIEW(Constants.STATUS_IN_REVIEW), RESUBMITTED(Constants.STATUS_RESUBMITTED),
	REVIEWED_UPDATE_REQUIRED(Constants.STATUS_REVIEWED_UPDATE_REQUIRED);

	private final String value;

	UCFStatus(final String type) {
		value = type;
	}

	public String getValue() {
		return value;
	}
}
