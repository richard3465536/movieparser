package com.richard.model;

import static com.richard.TestUtil.EMPTY_STRING;
import static com.richard.TestUtil.VALID_STRING;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MovieTest {

	private Movie movie;

	@Test(expected = IllegalArgumentException.class)
	public void nullName() throws Exception {
		movie = new Movie(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyName() throws Exception {
		movie = new Movie(EMPTY_STRING);
	}

	@Test
	public void validName() throws Exception {
		movie = new Movie(VALID_STRING);
	}

	@Test
	public void toStringFormat() throws Exception {
		movie = new Movie(VALID_STRING);
		assertThat(movie.toString(), is(VALID_STRING));
	}
}
