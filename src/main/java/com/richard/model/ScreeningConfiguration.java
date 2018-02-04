package com.richard.model;

import static com.richard.util.ArgumentValidationUtil.validateNotNull;

import java.util.Locale;
import java.util.Optional;

public class ScreeningConfiguration {

	private Locale audioLanguage;
	private Locale subtitleLanguage;

	public ScreeningConfiguration(Locale audioLanguage, Locale subtitleLanguage) {
		this.audioLanguage = validateNotNull(audioLanguage);
		this.subtitleLanguage = subtitleLanguage;
	}

	public ScreeningConfiguration(Locale audioLanguage) {
		this(audioLanguage, null);
	}

	public Locale getAudioLanguage() {
		return audioLanguage;
	}

	public Optional<Locale> getSubtitleLanguage() {
		return Optional.ofNullable(subtitleLanguage);
	}

}
