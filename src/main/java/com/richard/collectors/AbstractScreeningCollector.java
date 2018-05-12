package com.richard.collectors;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;

import com.richard.model.Screening;
import com.richard.parsers.ScreeningParser;

public abstract class AbstractScreeningCollector implements ScreeningCollector {

	private final WebsiteReader websiteReader;

	public AbstractScreeningCollector(WebsiteReader websiteReader) {
		this.websiteReader = websiteReader;
	}

	@Override
	public final List<Screening> collect() {
		try {
			Document htmlDocument = websiteReader.read(getUrl());
			return getScreeningParser().parse(htmlDocument);
		} catch (IOException e) {
			// TODO logging
		}
		return Collections.emptyList();
	}

	protected abstract String getUrl();

	protected abstract ScreeningParser getScreeningParser();
}
