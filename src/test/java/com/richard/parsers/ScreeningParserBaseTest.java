package com.richard.parsers;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ScreeningParserBaseTest {

	protected Document readWebsite(String fileName) {
		String path = getPath(fileName);
		File htmlFile = new File(path);
		try {
			return Jsoup.parse(htmlFile, "UTF-8");
		} catch (IOException exception) {
			String message = String.format("website %s not readable", path);
			throw new InvalidFormatException(message, exception);
		}
	}

	private String getPath(String fileName) throws InvalidFormatException {
		return getClass().getClassLoader().getResource(fileName).getPath();
	}
}