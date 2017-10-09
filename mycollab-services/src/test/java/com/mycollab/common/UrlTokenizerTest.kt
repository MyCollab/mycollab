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
