package com.richard.model;

import static com.richard.TestUtil.VALID_STRING;
import static java.util.Locale.ENGLISH;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class ScreeningTest {

	private static final Movie TEST_MOVIE = new Movie(VALID_STRING);
	private static final Location TEST_LOCATION = new Location(VALID_STRING);
	private static final ZonedDateTime TEST_ZONE_ID = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0,
			ZoneId.of("Europe/Berlin"));
	private static final ScreeningConfiguration SCREENING_CONFIGURATION = new ScreeningConfiguration(ENGLISH);

	@Test(expected = IllegalArgumentException.class)
	public void nullMovie() throws Exception {
		Screening screening = new Screening(null, TEST_LOCATION, TEST_ZONE_ID, SCREENING_CONFIGURATION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullLocation() throws Exception {
		Screening screening = new Screening(TEST_MOVIE, null, TEST_ZONE_ID, SCREENING_CONFIGURATION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullZoneId() throws Exception {
		Screening screening = new Screening(TEST_MOVIE, TEST_LOCATION, null, SCREENING_CONFIGURATION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullScreeningConfiguration() throws Exception {
		Screening screening = new Screening(TEST_MOVIE, TEST_LOCATION, TEST_ZONE_ID, null);
	}
}
