package com.richard.parsers;

import java.util.List;

import org.jsoup.nodes.Document;

import com.richard.model.Screening;

public interface ScreeningParser {

	List<Screening> parse(Document htmlDocument);
}
