package com.richard.collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.richard.model.Screening;
import com.richard.parsers.ScreeningParser;

public class AbstractScreeningCollectorTest {

	private static final String URL = "https://www.test.de";
	private ScreeningCollector screeningCollector;
	private WebsiteReader websiteReader;
	private ScreeningParser screeningParser;

	@Before
	public void setUp() throws Exception {
		websiteReader = mock(WebsiteReader.class);
		screeningParser = mock(ScreeningParser.class);
		screeningCollector = new TestableScreeningCollector(websiteReader, screeningParser);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void emptyListIfWebsiteNotAccessible() throws Exception {
		when(websiteReader.read(anyString())).thenThrow(IOException.class);
		assertThat(screeningCollector.collect(), is(empty()));
	}

	@Test
	public void retrievesScreeningsFromGivenUrl() throws Exception {
		List<Screening> expected = asList(mock(Screening.class), mock(Screening.class));
		Document htmlDocument = new Document("abc");
		when(websiteReader.read(URL)).thenReturn(htmlDocument);
		when(screeningParser.parse(htmlDocument)).thenReturn(expected);

		assertThat(screeningCollector.collect(), is(expected));
	}

	private class TestableScreeningCollector extends AbstractScreeningCollector {

		private final ScreeningParser screeningParser;

		public TestableScreeningCollector(WebsiteReader websiteReader, ScreeningParser screeningParser) {
			super(websiteReader);
			this.screeningParser = screeningParser;
		}

		@Override
		protected String getUrl() {
			return URL;
		}

		@Override
		protected ScreeningParser getScreeningParser() {
			return screeningParser;
		}
	}
}