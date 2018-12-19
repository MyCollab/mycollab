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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UrlTokenizerTest {
    private var testData: String? = null

    @BeforeEach
    fun setUp() {
        testData = GenericLinkUtils.encodeParam(1, "a", "b", "1/2/3")
    }

    @Test
    @Throws(InvalidTokenException::class)
    fun testGetProperValue() {
        val tokenizer = UrlTokenizer(testData!!)
        Assertions.assertEquals(1, tokenizer.getInt())
        Assertions.assertEquals("a", tokenizer.getString())
        Assertions.assertEquals("b", tokenizer.getString())
        Assertions.assertEquals("1/2/3", tokenizer.remainValue)
    }
}
