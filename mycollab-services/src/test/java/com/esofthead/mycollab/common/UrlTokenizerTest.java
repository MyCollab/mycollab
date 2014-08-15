package com.esofthead.mycollab.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UrlTokenizerTest {

	private String testData;

	@Before
	public void setUp() {
		testData = GenericLinkUtils.encodeParam(new Object[] { 1, "a", "b",
				"1/2/3" });
	}

	@Test
	public void testGetProperValue() throws InvalidTokenException {
		UrlTokenizer tokenizer = new UrlTokenizer(testData);
		Assert.assertEquals(1, tokenizer.getInt());
		Assert.assertEquals("a", tokenizer.getString());
		Assert.assertEquals("b", tokenizer.getString());
		Assert.assertEquals("1/2/3", tokenizer.getRemainValue());
	}
}
