package com.richard.movieretrieval;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class MovieTitleParserTest {

	private MovieTitleParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new MovieTitleParser();
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullInput() throws Exception {
		parser.parseMainTitle(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyInput() throws Exception {
		parser.parseMainTitle("");
	}

	@Test
	public void resultIsTrimmed() throws Exception {
		String result = parser.parseMainTitle("   abc ");
		assertThat(result, is("abc"));
	}

	@Test
	public void truncatesAtHyphen() throws Exception {
		String result = parser.parseMainTitle("abc - def");
		assertThat(result, is("abc"));
	}

	@Test
	public void parenthesizedContentIsRemovedFromMainTitle() throws Exception {
		String result = parser.parseMainTitle("abc(def)");
		assertThat(result, is("abc"));
	}

	@Test
	public void noSubtitleIfInputNull() throws Exception {
		Optional<String> result = parser.parseSubtitle(null);
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void absenceOfSeparatorIndicatesMissingSubtitle() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc");
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void noResultIfSubtitleAbsent() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc - ");
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void subtitleBeginsAfterSeparator() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc - def");
		assertThat(result.get(), is("def"));
	}

	@Test
	public void parenthesizedContentIsRemovedFromSubtitle() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc - def(ghi)");
		assertThat(result.get(), is("def"));
	}

	@Test
	public void subtitleIsTrimmed() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc - def    ");
		assertThat(result.get(), is("def"));
	}
}
