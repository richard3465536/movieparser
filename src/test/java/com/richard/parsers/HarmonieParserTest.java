package com.richard.parsers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.richard.model.Screening;

public class HarmonieParserTest {

	private static final ZoneId ZONE_ID = ZoneId.of("Europe/Berlin");
	private HarmonieParser harmonieParser = new HarmonieParser();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test(expected = IllegalArgumentException.class)
	public void nullUrl() throws Exception {
		harmonieParser.parse(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyUrl() throws Exception {
		harmonieParser.parse("");
	}

	@Test
	public void unreadableWebsite() throws Exception {
		exception.expect(InvalidFormatException.class);
		exception.expectMessage("website abc.html not readable");
		harmonieParser.parse("abc.html");
	}

	@Test
	public void emptySchedule() throws Exception {
		assertThat(parseScreenings("harmonieNoProgramItem.html"), is(empty()));
	}

	@Test
	public void noScheduleTime() throws Exception {
		exception.expect(InvalidFormatException.class);
		exception.expectMessage("No schedule time");
		parseScreenings("harmonieNoScheduleTime.html");
	}

	@Test
	public void wrongStartDateFormat() throws Exception {
		exception.expect(InvalidFormatException.class);
		exception.expectMessage("schedule start date (11.11.11) does not comply with expected format dd. MMM yyyy");
		parseScreenings("harmonieWrongStartDateFormat.html");
	}

	@Test
	public void parsesScreeningTime() throws Exception {
		Screening screening = parseFirstScreening("harmonieOneScreening.html");
		Object expectedScreeningTime = ZonedDateTime.of(LocalDate.of(2017, 4, 5), LocalTime.of(14, 15), ZONE_ID);
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
		List<Screening> screenings = parseScreenings("harmonieOneScreening.html");
		assertThat(screenings.size(), is(1));
	}

	@Test
	public void twoScreeningsSameProgramItemSameDay() throws Exception {
		List<Screening> screenings = parseScreenings("harmonieTwoScreeningsSameProgramItemSameDay.html");
		assertThat(screenings.size(), is(2));
	}

	@Test
	public void twoScreeningsSameProgramItemDifferentDay() throws Exception {
		List<Screening> screenings = parseScreenings("harmonieTwoScreeningsSameProgramItemDifferentDay.html");
		assertThat(screenings.size(), is(2));
	}

	@Test
	public void twoScreeningsDifferentProgramItem() throws Exception {
		List<Screening> screenings = parseScreenings("harmonieTwoScreeningsDifferentProgramItem.html");
		assertThat(screenings.size(), is(2));
	}

	@Test
	public void parsesMovieTitle() throws Exception {
		Screening screening = parseFirstScreening("harmonieOneScreening.html");
		assertThat(screening.getMovie().getName(), is("BAUER UNSER"));
	}

	@Test
	public void skipScreeningsWithoutMovieTitle() throws Exception {
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

	private Screening parseFirstScreening(String fileName) throws InvalidFormatException {
		List<Screening> screenings = parseScreenings(fileName);
		return screenings.get(0);
	}

	private List<Screening> parseScreenings(String fileName) throws InvalidFormatException {
		String url = getUrl(fileName);
		return harmonieParser.parse(url);
	}

	private String getUrl(String fileName) {
		return getClass().getClassLoader().getResource(fileName).getPath();
	}

	private void assertNoScreenings(String fileName) {
		List<Screening> screenings = parseScreenings(fileName);
		assertThat(screenings, is(empty()));
	}
}
