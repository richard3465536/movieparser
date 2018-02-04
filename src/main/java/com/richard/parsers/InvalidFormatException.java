package com.richard.parsers;

public class InvalidFormatException extends RuntimeException {

	private static final long serialVersionUID = -7002144076903757073L;

	public InvalidFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFormatException(String message) {
		super(message);
	}

}
