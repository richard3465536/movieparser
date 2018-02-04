package com.richard.parsers;

import java.util.List;

import com.richard.model.Screening;

public interface ScreeningParser {

	public List<Screening> parse(String url);
}
