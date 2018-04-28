package com.richard.movieretrieval;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.richard.model.Movie;
import com.richard.movieretrieval.omdb.OmdbApiConnector;

@Component
public class MovieServiceImpl implements MovieService {

	private final OmdbApiConnector imdbApiConnector;
	private final MovieTitleParser titleParser;

	public MovieServiceImpl(OmdbApiConnector imdbApiConnector, MovieTitleParser titleParser) {
		this.imdbApiConnector = imdbApiConnector;
		this.titleParser = titleParser;
	}

	@Override
	public Optional<Movie> getMovieByTitle(String title) {
		if (StringUtils.isEmpty(title)) {
			return Optional.empty();
		}

		List<Movie> queryResult = imdbApiConnector.query(title);
		if (!queryResult.isEmpty()) {
			return getFirstElement(queryResult);
		}

		Optional<String> mainTitle = titleParser.parseMainTitle(title);
		if (!mainTitle.isPresent()) {
			return Optional.empty();
		}
		queryResult = imdbApiConnector.query(mainTitle.get());
		if (!queryResult.isEmpty()) {
			return getFirstElement(queryResult);
		}

		return getFirstElement(queryOnlyWithSubTitle(title));
	}

	private Optional<Movie> getFirstElement(List<Movie> queryResult) {
		return queryResult.stream().findFirst();
	}

	private List<Movie> queryOnlyWithSubTitle(String fullTitle) {
		Optional<String> subTitle = titleParser.parseSubtitle(fullTitle);
		if (subTitle.isPresent()) {
			return imdbApiConnector.query(subTitle.get());
		}
		return emptyList();
	}
}