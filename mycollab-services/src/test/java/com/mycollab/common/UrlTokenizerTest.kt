/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common

import com.mycollab.common.GenericLinkUtils
import com.mycollab.common.InvalidTokenException
import com.mycollab.common.UrlTokenizer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UrlTokenizerTest {
    private var testData: String? = null

    @Before
    fun setUp() {
        testData = GenericLinkUtils.encodeParam(1, "a", "b", "1/2/3")
    }

    @Test
    @Throws(InvalidTokenException::class)
    fun testGetProperValue() {
        val tokenizer = UrlTokenizer(testData!!)
        Assert.assertEquals(1, tokenizer.getInt())
        Assert.assertEquals("a", tokenizer.getString())
        Assert.assertEquals("b", tokenizer.getString())
        Assert.assertEquals("1/2/3", tokenizer.remainValue)
    }
}
