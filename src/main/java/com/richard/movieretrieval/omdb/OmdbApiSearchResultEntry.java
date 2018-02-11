package com.richard.movieretrieval.omdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmdbApiSearchResultEntry {

	@JsonProperty(value = "Title")
	private String title;

	@JsonProperty(value = "Type")
	private String type;

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}
}
