package com.richard.model;

import static com.richard.util.ArgumentValidationUtil.validateNotEmpty;

public class Location {

	private final String name;

	public Location(String name) {
		this.name = validateNotEmpty(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		return builder.toString();
	}

}
