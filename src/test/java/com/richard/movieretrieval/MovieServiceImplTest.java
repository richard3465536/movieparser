package com.richard.movieretrieval;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.richard.model.Movie;
import com.richard.movieretrieval.omdb.OmdbApiConnector;

@RunWith(MockitoJUnitRunner.class)
public class MovieServiceImplTest {

	private static final String FULL_TITLE = "Titanic";
	private static final Movie MOVIE = new Movie("movie");
	private static final Movie OTHER_MOVIE = new Movie("otherMovie");

	private MovieServiceImpl movieService;

	@Mock
	private OmdbApiConnector apiConnector;

	@Mock
	private MovieTitleParser titleParser;

	@Captor
	private ArgumentCaptor<String> titleCaptor;

	@Before
	public void setUp() throws Exception {
		movieService = new MovieServiceImpl(apiConnector, titleParser);
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
	public void searchOrder() throws Exception {
		String mainTitle = "abc";
		String subTitle = "def";
		String fullTitle = mainTitle + subTitle;
		when(titleParser.parseMainTitle(fullTitle)).thenReturn(Optional.of(mainTitle));
		when(titleParser.parseSubtitle(fullTitle)).thenReturn(Optional.of(subTitle));
		when(apiConnector.query(anyString())).thenReturn(emptyList());
		InOrder order = Mockito.inOrder(apiConnector);

		movieService.getMovieByTitle(fullTitle);

		order.verify(apiConnector).query(fullTitle);
		order.verify(apiConnector).query(mainTitle);
		order.verify(apiConnector).query(subTitle);
	}

	@Test
	public void returnsFirstResultOfSuccessfulFullTitleSearch() throws Exception {
		when(apiConnector.query(FULL_TITLE)).thenReturn(asList(MOVIE, OTHER_MOVIE));
		Optional<Movie> movie = movieService.getMovieByTitle(FULL_TITLE);
		assertThat(movie.get(), is(MOVIE));
	}

	@Test
	public void noResultIfFullTitleSearchUnsuccessfulAndNoMainTitlePresent() throws Exception {
		when(titleParser.parseMainTitle(FULL_TITLE)).thenReturn(Optional.empty());
		when(apiConnector.query(FULL_TITLE)).thenReturn(emptyList());
		Optional<Movie> movie = movieService.getMovieByTitle(FULL_TITLE);
		assertThat(movie.isPresent(), is(false));
	}

	@Test
	public void returnsFirstResultOfSuccessfulMainTitleSearchIfFullTitleSearchUnsuccessful() throws Exception {
		String mainTitle = "abc";
		String fullTitle = mainTitle + " def";
		when(titleParser.parseMainTitle(fullTitle)).thenReturn(Optional.of(mainTitle));
		when(apiConnector.query(fullTitle)).thenReturn(emptyList());
		when(apiConnector.query(mainTitle)).thenReturn(asList(MOVIE, OTHER_MOVIE));

		Optional<Movie> movie = movieService.getMovieByTitle(fullTitle);

		assertThat(movie.get(), is(MOVIE));
	}

	@Test
	public void noResultIfFullAndMainTitleSearchUnsuccessfulAndNoSubTitlePresent() throws Exception {
		String mainTitle = "abc";
		String fullTitle = mainTitle;
		when(titleParser.parseMainTitle(fullTitle)).thenReturn(Optional.of(mainTitle));
		when(titleParser.parseSubtitle(fullTitle)).thenReturn(Optional.empty());
		when(apiConnector.query(fullTitle)).thenReturn(emptyList());
		when(apiConnector.query(mainTitle)).thenReturn(emptyList());

		Optional<Movie> movie = movieService.getMovieByTitle(fullTitle);

		assertThat(movie.isPresent(), is(false));
	}

	@Test
	public void returnsFirstResultOfSuccessfulSubTitleSearchIfFullAndMainTitleSearchUnsuccessful() throws Exception {
		String mainTitle = "abc";
		String subTitle = "def";
		String fullTitle = mainTitle + subTitle;
		when(titleParser.parseMainTitle(fullTitle)).thenReturn(Optional.of(mainTitle));
		when(titleParser.parseSubtitle(fullTitle)).thenReturn(Optional.of(subTitle));
		when(apiConnector.query(fullTitle)).thenReturn(emptyList());
		when(apiConnector.query(mainTitle)).thenReturn(emptyList());
		when(apiConnector.query(subTitle)).thenReturn(asList(MOVIE, OTHER_MOVIE));

		Optional<Movie> movie = movieService.getMovieByTitle(fullTitle);

		assertThat(movie.get(), is(MOVIE));
	}

	@Test
	public void allSearchesUnsuccessful() throws Exception {
		when(titleParser.parseMainTitle(FULL_TITLE)).thenReturn(Optional.of("abc"));
		when(titleParser.parseSubtitle(FULL_TITLE)).thenReturn(Optional.of("def"));
		when(apiConnector.query(anyString())).thenReturn(emptyList());
		Optional<Movie> movie = movieService.getMovieByTitle(FULL_TITLE);
		assertThat(movie.isPresent(), is(false));
	}
}
