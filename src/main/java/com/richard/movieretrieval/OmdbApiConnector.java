package com.richard.movieretrieval;

import static com.richard.util.ArgumentValidationUtil.validateNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.richard.model.Movie;

public class OmdbApiConnector {

	private final OmdbApiSearchResultEntryMapper entryMapper;
	private final RestTemplate restTemplate;

	public OmdbApiConnector(OmdbApiSearchResultEntryMapper entryMapper) {
		this.entryMapper = entryMapper;
		this.restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}

	public List<Movie> query(String searchPhrase) {
		validateNotEmpty(searchPhrase);
		String url = "http://www.omdbapi.com?apikey=45f500b&s=" + searchPhrase;
		SearchResultList response = restTemplate.getForObject(url, SearchResultList.class);
		if (response.hasResults()) {
			return processSearchResult(response);
		}
		return new ArrayList<>();
	}

	private List<Movie> processSearchResult(SearchResultList searchResult) {
		List<OmdbApiSearchResultEntry> resultEntries = searchResult.getResults();
		Stream<Optional<Movie>> convertedResultEntries = convertResultEntries(resultEntries);
		return filterOutEmptyResults(convertedResultEntries);
	}

	private Stream<Optional<Movie>> convertResultEntries(List<OmdbApiSearchResultEntry> resultEntries) {
		return resultEntries.stream().map(result -> entryMapper.convert(result));
	}

	private List<Movie> filterOutEmptyResults(Stream<Optional<Movie>> potentialResults) {
		return potentialResults.filter(optional -> optional.isPresent()).map(optional -> optional.get())
				.collect(Collectors.toList());
	}
}
