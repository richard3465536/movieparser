package com.richard.parsers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.richard.model.Location;
import com.richard.model.Movie;
import com.richard.model.Screening;
import com.richard.model.ScreeningConfiguration;
import com.richard.movieretrieval.MovieService;
import com.richard.util.ArgumentValidationUtil;

public class HarmonieParser implements ScreeningParser {

	private static final String DATE_PATTERN = "dd. MMM yyyy";
	private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder().parseCaseInsensitive()
			.appendPattern(DATE_PATTERN).toFormatter(Locale.ENGLISH);
	private static final ZoneId ZONE_ID = ZoneId.of("Europe/Berlin");
	private static final String UTF_8 = "UTF-8";
	private static final Location LOCATION = new Location("Harmonie");
	private static final ScreeningConfiguration SCREENING_CONFIGURATION = new ScreeningConfiguration(Locale.ENGLISH);

	private final MovieService movieService;

	public HarmonieParser(MovieService movieService) {
		this.movieService = movieService;
	}

	@Override
	public List<Screening> parse(String url) {
		ArgumentValidationUtil.validateNotEmpty(url);
		Document website = readWebsite(url);
		return getScreenings(website);
	}

	private Document readWebsite(String path) {
		File htmlFile = new File(path);
		try {
			return Jsoup.parse(htmlFile, UTF_8);
		} catch (IOException exception) {
			String message = String.format("website %s not readable", path);
			throw new InvalidFormatException(message, exception);
		}
	}

	private List<Screening> getScreenings(Document website) {
		List<Screening> screenings = new ArrayList<>();
		LocalDate scheduleStart = getScheduleStart(website);
		for (Element programItem : getProgramItems(website)) {
			List<Screening> screeningsSameProgramItem = getScreenings(programItem, scheduleStart);
			screenings.addAll(screeningsSameProgramItem);
		}
		return screenings;
	}

	private LocalDate getScheduleStart(Document website) {
		Element scheduleTime = website.getElementById("program-header-time");
		if (scheduleTime == null) {
			throw new InvalidFormatException("No schedule time");
		}
		String timeRange = scheduleTime.text();
		String startDateSection = timeRange.split(" - ")[0];
		return parseDate(startDateSection);
	}

	private LocalDate parseDate(String dateAsText) {
		try {
			return LocalDate.parse(dateAsText, DATE_FORMATTER);
		} catch (DateTimeParseException exception) {
			String message = String.format("schedule start date (%s) does not comply with expected format %s",
					dateAsText, DATE_PATTERN);
			throw new InvalidFormatException(message, exception);
		}
	}

	private Elements getProgramItems(Document website) {
		return website.getElementsByClass("program-item");
	}

	private List<Screening> getScreenings(Element programItem, LocalDate scheduleStart) {
		String movieTitle = getMovieTitle(programItem);
		Optional<Movie> retrievedMovie = movieService.getMovieByTitle(movieTitle);
		if (retrievedMovie.isPresent()) {
			Movie movie = retrievedMovie.get();
			return getScreeningsOfMovie(programItem, movie, scheduleStart);
		}
		return new ArrayList<>();
	}

	private String getMovieTitle(Element programItem) {
		Elements programItemRightTitle = programItem.getElementsByClass("program-item-right-title");
		return programItemRightTitle.text();
	}

	private ArrayList<Screening> getScreeningsOfMovie(Element programItem, Movie movie, LocalDate scheduleStart) {
		ArrayList<Screening> screeningsOfMovie = new ArrayList<Screening>();
		for (ZonedDateTime screeningTime : getScreeningTimes(programItem, scheduleStart)) {
			Screening screening = new Screening(movie, LOCATION, screeningTime, SCREENING_CONFIGURATION);
			screeningsOfMovie.add(screening);
		}
		return screeningsOfMovie;
	}

	private List<ZonedDateTime> getScreeningTimes(Element programItem, LocalDate scheduleStart) {
		List<ZonedDateTime> screeningTimes = new ArrayList<>();
		LocalDate screeningDate = scheduleStart;

		for (Element dailyScreeningElement : getDailyScreeningElements(programItem)) {
			for (Element timeElement : dailyScreeningElement.getElementsByTag("p")) {
				Optional<ZonedDateTime> screeningTime = getScreeningTime(timeElement, screeningDate);
				screeningTime.ifPresent(screeningTimes::add);
			}
			screeningDate = screeningDate.plusDays(1L);
		}
		return screeningTimes;
	}

	private List<Element> getDailyScreeningElements(Element programItem) {
		List<Element> dayScreeningTimeElements = new ArrayList<>();
		try {
			Element scheduleTable = programItem.getElementsByClass("program-item-right-screenings").get(0).child(0);
			Elements firstRow = scheduleTable.child(1).children();
			Elements secondRow = scheduleTable.child(3).children();
			dayScreeningTimeElements.addAll(firstRow);
			dayScreeningTimeElements.addAll(secondRow);
		} catch (IndexOutOfBoundsException exception) {
			// TODO logging
		}
		return dayScreeningTimeElements;
	}

	private Optional<ZonedDateTime> getScreeningTime(Element timeElement, LocalDate screeningDate) {
		ZonedDateTime screeningTime = null;
		String time = timeElement.text();
		LocalTime parsedTime;
		try {
			parsedTime = LocalTime.parse(time);
			screeningTime = ZonedDateTime.of(screeningDate, parsedTime, ZONE_ID);
		} catch (DateTimeParseException parseException) {
			// ignore
			// TODO logging
		}
		return Optional.ofNullable(screeningTime);
	}
}
