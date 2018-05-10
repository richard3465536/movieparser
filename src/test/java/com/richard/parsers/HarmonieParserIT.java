package com.richard.parsers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.jsoup.nodes.Document;
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
public class HarmonieParserIT extends ScreeningParserBaseTest {

	private HarmonieParser harmonieParser;
	@Autowired
	private MovieService movieService;

	@Before
	public void setUp() throws Exception {
		harmonieParser = new HarmonieParser(movieService);
	}

	@Test
	public void elevenMoviesOnlyNineKnownWithFiftyThreeScreenings() throws Exception {
		Document website = readWebsite("elevenMoviesWithFiftyFourScreenings.html");
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings.size(), is(53));
		assertThat(numberOfDifferentMovies(screenings), is(9L));
	}

	private long numberOfDifferentMovies(List<Screening> screenings) {
		return screenings.stream().map(screening -> screening.getMovie()).distinct().count();
	}
}