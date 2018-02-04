package com.richard.util;

import static com.richard.TestUtil.EMPTY_STRING;
import static com.richard.TestUtil.VALID_STRING;
import static com.richard.util.ArgumentValidationUtil.validateNotEmpty;
import static com.richard.util.ArgumentValidationUtil.validateNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ArgumentValidationUtilTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullObject() throws Exception {
		validateNotNull(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullString() throws Exception {
		validateNotEmpty(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyString() throws Exception {
		validateNotEmpty(EMPTY_STRING);
	}

	@Test
	public void nonEmptyString() throws Exception {
		String string = VALID_STRING;
		assertThat(validateNotEmpty(string), is(string));
	}

}
