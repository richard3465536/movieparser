package com.richard.model;

import static com.richard.util.ArgumentValidationUtil.validateNotNull;

import java.time.ZonedDateTime;

public class Screening {

	private Movie movie;
	private Location location;
	private ZonedDateTime dateTime;
	private ScreeningConfiguration screeningConfiguration;

	public Screening(Movie movie, Location location, ZonedDateTime dateTime,
			ScreeningConfiguration screeningConfiguration) {
		this.movie = validateNotNull(movie);
		this.location = validateNotNull(location);
		this.dateTime = validateNotNull(dateTime);
		this.screeningConfiguration = validateNotNull(screeningConfiguration);
	}

	public Movie getMovie() {
		return movie;
	}

	public Location getLocation() {
		return location;
	}

	public ZonedDateTime getDateTime() {
		return dateTime;
	}

	public ScreeningConfiguration getScreeningConfiguration() {
		return screeningConfiguration;
	}

}
