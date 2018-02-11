package com.richard.movieretrieval.omdb;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.richard.model.Movie;

public class OmdbApiSearchResultEntryMapperTest {

	private static final String TYPE_MOVIE = "movie";
	private static final String TITLE = "titanic";
	private OmdbApiSearchResultEntryMapper mapper;

	@Before
	public void setUp() throws Exception {
		mapper = new OmdbApiSearchResultEntryMapper();
	}

	@Test
	public void emptyResultIfNullInput() throws Exception {
		Optional<Movie> result = mapper.convert(null);
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void emptyResultIfTypeNotMovie() throws Exception {
		OmdbApiSearchResultEntry resultEntry = createResultEntry("abc", TITLE);
		Optional<Movie> result = mapper.convert(resultEntry);
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void titleAttributeMapsToMovieName() throws Exception {
		OmdbApiSearchResultEntry resultEntry = createResultEntry(TYPE_MOVIE, TITLE);
		Optional<Movie> result = mapper.convert(resultEntry);
		assertThat(result.isPresent(), is(true));
		assertThat(result.get().getName(), is(TITLE));
	}

	private OmdbApiSearchResultEntry createResultEntry(String type, String title) {
		OmdbApiSearchResultEntry resultEntry = new OmdbApiSearchResultEntry();
		resultEntry.setType(type);
		resultEntry.setTitle(title);
		return resultEntry;
	}

}
