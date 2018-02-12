package com.richard.parsers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.richard.MovieparserApplication;
import com.richard.model.Screening;
import com.richard.movieretrieval.MovieService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MovieparserApplication.class })
public class HarmonieParserIT {

	private HarmonieParser harmonieParser;
	@Autowired
	private MovieService movieService;

	@Before
	public void setUp() throws Exception {
		harmonieParser = new HarmonieParser(movieService);
	}

	@Test
	public void elevenMoviesOnlyFiveKnownWithThirtyFiveScreenings() throws Exception {
		List<Screening> screenings = parseScreenings("elevenMoviesWithFiftyFiveScreenings.html");
		assertThat(screenings.size(), is(35));
		assertThat(numberOfDifferentMovies(screenings), is(5L));
	}

	private long numberOfDifferentMovies(List<Screening> screenings) {
		return screenings.stream().map(screening -> screening.getMovie()).distinct().count();
	}

	private List<Screening> parseScreenings(String fileName) throws InvalidFormatException {
		String url = getUrl(fileName);
		return harmonieParser.parse(url);
	}

	private String getUrl(String fileName) {
		return getClass().getClassLoader().getResource(fileName).getPath();
	}
}
