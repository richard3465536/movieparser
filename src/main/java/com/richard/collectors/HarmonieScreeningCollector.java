package com.richard.collectors;

import com.richard.parsers.HarmonieParser;
import com.richard.parsers.ScreeningParser;

public class HarmonieScreeningCollector extends AbstractScreeningCollector {

	private final HarmonieParser harmonieParser;

	public HarmonieScreeningCollector(WebsiteReader websiteReader, HarmonieParser harmonieParser) {
		super(websiteReader);
		this.harmonieParser = harmonieParser;
	}

	@Override
	protected String getUrl() {
		return "http://www.arthouse-kinos.de/programm-in-der-harmonie/";
	}

	@Override
	protected ScreeningParser getScreeningParser() {
		return harmonieParser;
	}
}