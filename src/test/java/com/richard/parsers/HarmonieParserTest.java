package com.richard.parsers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.richard.model.Movie;
import com.richard.model.Screening;
import com.richard.movieretrieval.MovieService;

public class HarmonieParserTest extends ScreeningParserBaseTest {

	private static final ZoneId ZONE_ID = ZoneId.of("Europe/Berlin");

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private MovieService movieService;
	private HarmonieParser harmonieParser;

	@Before
	public void setUp() throws Exception {
		movieService = Mockito.mock(MovieService.class);
		letMovieServiceAlwaysRetrieveMovie();
		harmonieParser = new HarmonieParser(movieService);
	}

	@Test
	public void emptySchedule() throws Exception {
		Document website = readWebsite("harmonieNoProgramItem.html");
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings, is(empty()));
	}

	@Test
	public void noScheduleTime() throws Exception {
		Document website = readWebsite("harmonieNoScheduleTime.html");
		exception.expect(InvalidFormatException.class);
		exception.expectMessage("No schedule time");
		harmonieParser.parse(website);
	}

	@Test
	public void wrongStartDateFormat() throws Exception {
		Document website = readWebsite("harmonieWrongStartDateFormat.html");
		exception.expect(InvalidFormatException.class);
		exception.expectMessage("schedule start date (11.11.11) does not comply with expected format dd. MMM yyyy");
		harmonieParser.parse(website);
	}

	@Test
	public void parsesScreeningTime() throws Exception {
		Object expectedScreeningTime = ZonedDateTime.of(LocalDate.of(2017, 4, 5), LocalTime.of(14, 15), ZONE_ID);
		Screening screening = parseFirstScreening("harmonieOneScreening.html");
		assertThat(screening.getDateTime(), is(expectedScreeningTime));
	}

	@Test
	public void noScreeningTable() throws Exception {
		assertNoScreenings("harmonieNoScreeningTable.html");
	}

	@Test
	public void noScreeningTableBody() throws Exception {
		assertNoScreenings("harmonieNoScreeningTableBody.html");
	}

	@Test
	public void screeningTableNumberRowsLessFour() throws Exception {
		assertNoScreenings("harmonieScreeningTableWithoutRows.html");
	}

	@Test
	public void oneScreening() throws Exception {
		Document website = readWebsite("harmonieOneScreening.html");
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings.size(), is(1));
	}

	@Test
	public void twoScreeningsSameProgramItemSameDay() throws Exception {
		Document website = readWebsite("harmonieTwoScreeningsSameProgramItemSameDay.html");
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings.size(), is(2));
	}

	@Test
	public void twoScreeningsSameProgramItemDifferentDay() throws Exception {
		Document website = readWebsite("harmonieTwoScreeningsSameProgramItemDifferentDay.html");
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings.size(), is(2));
	}

	@Test
	public void twoScreeningsDifferentProgramItem() throws Exception {
		Document website = readWebsite("harmonieTwoScreeningsDifferentProgramItem.html");
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings.size(), is(2));
	}

	@Test
	public void skipScreeningsWithoutMovieTitle() throws Exception {
		when(movieService.getMovieByTitle("")).thenReturn(Optional.empty());
		assertNoScreenings("harmonieNoMovieTitle.html");
	}

	@Test
	public void skipScreeningsWithWrongTimeFormat() throws Exception {
		assertNoScreenings("harmonieWrongTimeFormatOfScreenings.html");
	}

	@Test
	public void correctLocation() throws Exception {
		Screening screening = parseFirstScreening("harmonieOneScreening.html");
		assertThat(screening.getLocation().getName(), is("Harmonie"));
	}

	@Test
	public void noScreeningIfMovieNotRetrievableByTitle() throws Exception {
		Document website = readWebsite("harmonieOneScreening.html");
		when(movieService.getMovieByTitle(anyString())).thenReturn(Optional.empty());
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings, is(empty()));
	}

	private void letMovieServiceAlwaysRetrieveMovie() {
		Optional<Movie> movie = Optional.of(new Movie());
		when(movieService.getMovieByTitle(anyString())).thenReturn(movie);
	}

	private Screening parseFirstScreening(String fileName) throws InvalidFormatException {
		Document website = readWebsite(fileName);
		List<Screening> screenings = harmonieParser.parse(website);
		return screenings.get(0);
	}

	private void assertNoScreenings(String fileName) {
		Document website = readWebsite(fileName);
		List<Screening> screenings = harmonieParser.parse(website);
		assertThat(screenings, is(empty()));
	}
}