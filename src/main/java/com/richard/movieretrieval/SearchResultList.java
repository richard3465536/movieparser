package com.richard.movieretrieval;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.richard.model.Movie;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultList {

	@JsonProperty(value = "Search")
	private List<Movie> results;

	@JsonProperty(value = "Response")
	private String hasResults;

	public List<Movie> getResults() {
		return results;
	}

	public boolean hasResults() {
		return Boolean.parseBoolean(hasResults);
	}
}
