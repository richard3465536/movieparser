package com.richard.model;

import static java.util.Locale.ENGLISH;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class ScreeningConfigurationTest {

	private static final Locale LANGUAGE = ENGLISH;
	private ScreeningConfiguration screeningConfiguration;

	@Test(expected = IllegalArgumentException.class)
	public void nullAudioLanguage() throws Exception {
		screeningConfiguration = new ScreeningConfiguration(null);
	}

	@Test
	public void nullSubtitleLanguage() throws Exception {
		screeningConfiguration = new ScreeningConfiguration(LANGUAGE, null);
		assertThat(screeningConfiguration.getSubtitleLanguage().isPresent(), is(false));
	}

	@Test
	public void bothLanguagesSet() throws Exception {
		screeningConfiguration = new ScreeningConfiguration(LANGUAGE, LANGUAGE);
		assertThat(screeningConfiguration.getSubtitleLanguage().isPresent(), is(true));
	}
}
