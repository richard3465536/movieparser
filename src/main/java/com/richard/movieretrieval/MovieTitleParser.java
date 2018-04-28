package com.richard.movieretrieval;

import static org.apache.commons.lang3.StringUtils.removePattern;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MovieTitleParser {

	private static final String MAIN_TITLE_SUB_TITLE_SEPARATOR = " - ";

	public Optional<String> parseMainTitle(String title) {
		if (StringUtils.isEmpty(title)) {
			return Optional.empty();
		}
		String unclearedMainTitle = substringBefore(title, MAIN_TITLE_SUB_TITLE_SEPARATOR);
		return clear(unclearedMainTitle);
	}

	public Optional<String> parseSubtitle(String title) {
		if (doesNotContainSubtitle(title)) {
			return Optional.empty();
		}
		String unclearedSubTitle = substringAfter(title, MAIN_TITLE_SUB_TITLE_SEPARATOR);
		return clear(unclearedSubTitle);
	}

	private Optional<String> clear(String title) {
		String parenthesisless = removePattern(title, "[(][a-zA-Z0-9_]*[)]");
		String cleared = parenthesisless.trim();
		return handleEmptyResult(cleared);
	}

	private Optional<String> handleEmptyResult(String result) {
		if (result.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(result);
	}

	private boolean doesNotContainSubtitle(String title) {
		return title == null || !title.contains(MAIN_TITLE_SUB_TITLE_SEPARATOR);
	}

}
