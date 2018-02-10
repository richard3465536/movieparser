package com.richard.movieretrieval;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultList {

	@JsonProperty(value = "Search")
	private List<OmdbApiSearchResultEntry> results;

	@JsonProperty(value = "Response")
	private String hasResults;

	public List<OmdbApiSearchResultEntry> getResults() {
		return results;
	}

	public boolean hasResults() {
		return Boolean.parseBoolean(hasResults);
	}
}
