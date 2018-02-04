package com.richard.model;

import static com.richard.TestUtil.EMPTY_STRING;
import static com.richard.TestUtil.VALID_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LocationTest {

	private Location location;

	@Test(expected = IllegalArgumentException.class)
	public void nullName() throws Exception {
		location = new Location(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyName() throws Exception {
		location = new Location(EMPTY_STRING);
	}

	@Test
	public void validName() throws Exception {
		location = new Location(VALID_STRING);
	}

	@Test
	public void toStringFormat() throws Exception {
		location = new Location(VALID_STRING);
		assertThat(location.toString(), is(VALID_STRING));
	}
}
