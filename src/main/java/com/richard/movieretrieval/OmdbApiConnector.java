package com.richard.movieretrieval;

import static com.richard.util.ArgumentValidationUtil.validateNotEmpty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.richard.model.Movie;

public class OmdbApiConnector {

	private final RestTemplate restTemplate;

	public OmdbApiConnector() {
		this.restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}

	public List<Movie> query(String searchPhrase) {
		validateNotEmpty(searchPhrase);
		String url = "http://www.omdbapi.com?apikey=45f500b&s=" + searchPhrase;
		SearchResultList response = restTemplate.getForObject(url, SearchResultList.class);
		if (response.hasResults()) {
			return response.getResults();
		}
		return new ArrayList<>();
	}
}
