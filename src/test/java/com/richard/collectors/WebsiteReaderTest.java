package com.richard.collectors;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class WebsiteReaderTest {

	private WebsiteReader websiteReader;

	@Before
	public void setUp() throws Exception {
		websiteReader = new WebsiteReader();
	}

	@Test(expected = IOException.class)
	public void invalidUrl() throws Exception {
		websiteReader.read("abc");
	}

	// TODO undertested
}