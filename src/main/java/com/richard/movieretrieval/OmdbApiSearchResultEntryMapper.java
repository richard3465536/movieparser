package com.richard.movieretrieval;

import java.util.Optional;

import com.richard.model.Movie;

public class OmdbApiSearchResultEntryMapper {

	private static final String TYPE_MOVIE = "movie";

	public Optional<Movie> convert(OmdbApiSearchResultEntry resultEntry) {
		if (isNotConvertible(resultEntry)) {
			return Optional.empty();
		}
		String title = resultEntry.getTitle();
		return Optional.of(new Movie(title));
	}

	private boolean isNotConvertible(OmdbApiSearchResultEntry resultEntry) {
		return resultEntry == null || !TYPE_MOVIE.equals(resultEntry.getType());
	}

}
