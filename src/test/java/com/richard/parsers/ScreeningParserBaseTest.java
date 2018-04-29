package com.richard.parsers;

public class ScreeningParserBaseTest {

	protected String getUrl(String fileName) throws InvalidFormatException {
		return getClass().getClassLoader().getResource(fileName).getPath();
	}
}