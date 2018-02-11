package com.richard.movieretrieval.omdb;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.richard.MovieparserApplication;
import com.richard.model.Movie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MovieparserApplication.class })
public class OmdbApiConnectorTest {

	private OmdbApiConnector apiConnector;
	@Autowired
	private OmdbApiSearchResultEntryMapper omdbApiSearchResultEntryMapper;

	@Before
	public void setUp() throws Exception {
		apiConnector = new OmdbApiConnector(omdbApiSearchResultEntryMapper);
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
		assertThat(result, everyItem(new BaseMatcher<Movie>() {

			@Override
			public boolean matches(Object object) {
				Movie movie = (Movie) object;
				return movie.getName() != null;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("a Movie object with non-null title.");
			}
		}));
	}
}
