package com.richard.movieretrieval;

import static com.richard.util.ArgumentValidationUtil.validateNotEmpty;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class MovieTitleParser {

	private static final String MAIN_TITLE_SUB_TITLE_SEPARATOR = " - ";

	public String parseMainTitle(String title) {
		validateNotEmpty(title);
		String result = StringUtils.substringBefore(title, MAIN_TITLE_SUB_TITLE_SEPARATOR);
		return clear(result);
	}

	public Optional<String> parseSubtitle(String title) {
		if (doesNotContainSubtitle(title)) {
			return Optional.empty();
		}
		String unclearedSubtitle = StringUtils.substringAfter(title, MAIN_TITLE_SUB_TITLE_SEPARATOR);
		String result = clear(unclearedSubtitle);
		return handleEmptyResult(result);
	}

	private String clear(String result) {
		String cleared = StringUtils.removePattern(result, "[(][a-zA-Z0-9_]*[)]");
		return cleared.trim();
	}

	private boolean doesNotContainSubtitle(String title) {
		return title == null || !title.contains(MAIN_TITLE_SUB_TITLE_SEPARATOR);
	}

	private Optional<String> handleEmptyResult(String result) {
		if (result.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(result);
	}

}
