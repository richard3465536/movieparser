package com.richard.collectors;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebsiteReader {

	private static final int TIMEOUT = 1000;

	public Document read(String url) throws IOException {
		return Jsoup.parse(new URL(url), TIMEOUT);
	}
}