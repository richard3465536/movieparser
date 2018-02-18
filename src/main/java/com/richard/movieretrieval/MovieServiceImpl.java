package com.richard.movieretrieval;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.richard.model.Movie;
import com.richard.movieretrieval.omdb.OmdbApiConnector;

@Component
public class MovieServiceImpl implements MovieService {

	private final OmdbApiConnector imdbApiConnector;

	public MovieServiceImpl(OmdbApiConnector imdbApiConnector) {
		this.imdbApiConnector = imdbApiConnector;
	}

	@Override
	public Optional<Movie> getMovieByTitle(String title) {
		if (StringUtils.isEmpty(title)) {
			return Optional.empty();
		}
		List<Movie> queryResult = imdbApiConnector.query(title);
		return queryResult.stream().findFirst();
	}

}
