package in.co.test.ucf.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import in.co.test.ucf.models.UCFStatus;

public class Utils {

	public static LocalDateTime getCurrentLocalDateTime() {
		final Instant instant = Instant.now();
		final ZonedDateTime atZone = instant.atZone(ZoneId.of(Constants.TIME_ZONE_IST));
		return atZone.toLocalDateTime();
	}

	public static String generateRandomAlphanumericStringOfLength(final int targetStringLength) {
		final int leftLimit = 48; // numeral '0'
		final int rightLimit = 122; // letter 'z'
		final Random random = new Random();

		final String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();

		return generatedString;
	}

	public static UCFStatus generateRandomUcfStatus() {
		final int randomInt = ThreadLocalRandom.current().nextInt(0, UCFStatus.values().length);
		return UCFStatus.values()[randomInt];
	}
}
