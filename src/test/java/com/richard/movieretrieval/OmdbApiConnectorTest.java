package com.richard.movieretrieval;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.richard.model.Movie;

public class OmdbApiConnectorTest {

	private OmdbApiConnector apiConnector;

	@Before
	public void setUp() throws Exception {
		apiConnector = new OmdbApiConnector();
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullSearchPhrase() throws Exception {
		apiConnector.query(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptySearchPhrase() throws Exception {
		apiConnector.query("");
	}

	@Test
	public void noSearchResult() throws Exception {
		List<Movie> result = apiConnector.query("oirgflnsa");
		assertThat(result, is(empty()));
	}

	@Test
	public void success() throws Exception {
		List<Movie> result = apiConnector.query("titanic");
		assertThat(result, is(not(empty())));
	}
}
