package com.richard.movieretrieval;

import java.util.Optional;

import com.richard.model.Movie;

public interface MovieService {

	public Optional<Movie> getMovieByTitle(String title);
}
