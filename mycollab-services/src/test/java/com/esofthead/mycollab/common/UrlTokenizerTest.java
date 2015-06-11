/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UrlTokenizerTest {
	private String testData;

	@Before
	public void setUp() {
		testData = GenericLinkUtils.encodeParam(1, "a", "b", "1/2/3");
	}

	@Test
	public void testGetProperValue() throws InvalidTokenException {
		UrlTokenizer tokenizer = new UrlTokenizer(testData);
		Assert.assertEquals(new Integer(1), tokenizer.getInt());
		Assert.assertEquals("a", tokenizer.getString());
		Assert.assertEquals("b", tokenizer.getString());
		Assert.assertEquals("1/2/3", tokenizer.getRemainValue());
	}
}
