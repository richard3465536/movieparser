package com.richard.movieretrieval;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.richard.model.Movie;
import com.richard.movieretrieval.omdb.OmdbApiConnector;

public class MovieServiceImplTest {

	private static final String TITLE = "Titanic";
	private MovieServiceImpl movieService;
	private OmdbApiConnector apiConnector;

	@Before
	public void setUp() throws Exception {
		apiConnector = Mockito.mock(OmdbApiConnector.class);
		movieService = new MovieServiceImpl(apiConnector);
	}

	@Test
	public void nullTitle() throws Exception {
		Optional<Movie> movie = movieService.getMovieByTitle(null);
		assertThat(movie.isPresent(), is(false));
	}

	@Test
	public void emptyTitle() throws Exception {
		Optional<Movie> movie = movieService.getMovieByTitle("");
		assertThat(movie.isPresent(), is(false));
	}

	@Test
	public void unsuccessful() throws Exception {
		Optional<Movie> movie = movieService.getMovieByTitle("abc");
		assertThat(movie.isPresent(), is(false));
	}

	@Test
	public void unambiguousResult() throws Exception {
		Movie expected = new Movie(TITLE);
		when(apiConnector.query(TITLE)).thenReturn(Arrays.asList(expected));
		Optional<Movie> movie = movieService.getMovieByTitle(TITLE);
		assertThat(movie.get(), is(expected));
	}

	@Test
	public void returnsFirstIfMultipleResults() throws Exception {
		Movie expected = new Movie(TITLE);
		Movie otherResult = new Movie("abc");
		when(apiConnector.query(TITLE)).thenReturn(Arrays.asList(expected, otherResult, otherResult));
		Optional<Movie> movie = movieService.getMovieByTitle(TITLE);
		assertThat(movie.get(), is(expected));
	}
}
