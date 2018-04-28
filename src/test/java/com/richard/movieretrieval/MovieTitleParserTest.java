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

	@Test
	public void noMainTitleIfNullInput() throws Exception {
		Optional<String> result = parser.parseMainTitle(null);
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void noMainTitleIfEmptyInput() throws Exception {
		Optional<String> result = parser.parseMainTitle("");
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void noResultIfMainTitleAbsent() throws Exception {
		Optional<String> result = parser.parseMainTitle(" - def");
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void mainTitleEndsAtSeparator() throws Exception {
		Optional<String> result = parser.parseMainTitle("abc - def");
		assertThat(result.get(), is("abc"));
	}

	@Test
	public void parenthesizedContentIsRemovedFromMainTitle() throws Exception {
		Optional<String> result = parser.parseMainTitle("abc(def)");
		assertThat(result.get(), is("abc"));
	}

	@Test
	public void mainTitleIsTrimmed() throws Exception {
		Optional<String> result = parser.parseMainTitle("   abc ");
		assertThat(result.get(), is("abc"));
	}

	@Test
	public void noMainTitleIfEmptyAfterClearing() throws Exception {
		Optional<String> result = parser.parseMainTitle("  (abc)  ");
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void noSubtitleIfInputNull() throws Exception {
		Optional<String> result = parser.parseSubtitle(null);
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void noSubtitleIfInputEmpty() throws Exception {
		Optional<String> result = parser.parseSubtitle("");
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void absenceOfSeparatorIndicatesMissingSubtitle() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc");
		assertThat(result.isPresent(), is(false));
	}

	@Test
	public void noResultIfSubTitleAbsent() throws Exception {
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
	public void subTitleIsTrimmed() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc -    def    ");
		assertThat(result.get(), is("def"));
	}

	@Test
	public void noSubTitleIfEmptyAfterClearing() throws Exception {
		Optional<String> result = parser.parseSubtitle("abc -    (def)   ");
		assertThat(result.isPresent(), is(false));
	}
}
