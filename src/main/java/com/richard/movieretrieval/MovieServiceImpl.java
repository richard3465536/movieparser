package com.richard.movieretrieval;

import static com.richard.util.ArgumentValidationUtil.validateNotEmpty;

import java.util.List;
import java.util.Optional;

import com.richard.model.Movie;

public class MovieServiceImpl implements MovieService {

	private final OmdbApiConnector imdbApiConnector;

	public MovieServiceImpl(OmdbApiConnector imdbApiConnector) {
		this.imdbApiConnector = imdbApiConnector;
	}

	@Override
	public Optional<Movie> getMovieByTitle(String title) {
		validateNotEmpty(title);

		List<Movie> queryResult = imdbApiConnector.query(title);
		return queryResult.stream().findFirst();
	}

}
