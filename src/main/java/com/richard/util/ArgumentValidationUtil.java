package com.richard.util;

public final class ArgumentValidationUtil {

	public static String validateNotEmpty(String string) {
		validateNotNull(string);
		if (string.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return string;
	}

	public static <T> T validateNotNull(T object) {
		if (object == null) {
			throw new IllegalArgumentException();
		}
		return object;
	}
}
