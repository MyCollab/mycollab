package com.mycollab.common

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class UrlTokenizer(url: String) {

    private var internalVal: String = "";
    var remainValue: String = ""
    var query: String = ""

    @Throws(InvalidTokenException::class)
    fun getInt(): Int {
        if (hasMoreTokens())
            return getNextToken().toInt()
        else throw InvalidTokenException("Invalid token " + internalVal)
    }

    @Throws(InvalidTokenException::class)
    fun getString(): String = if (hasMoreTokens()) getNextToken() else throw InvalidTokenException("Invalid token " + internalVal)

    private fun hasMoreTokens(): Boolean = remainValue != ""

    private fun getNextToken(): String {
        val index = remainValue.indexOf("/")
        return if (index < 0) {
            val result = remainValue + ""
            remainValue = ""
            result
        } else {
            val result = remainValue.substring(0, index)
            remainValue = remainValue.substring(index + 1)
            result
        }
    }

    init {
        internalVal = if (url.startsWith("/")) url.substring(1) else url
        val queryIndex: Int = internalVal.indexOf("?")
        if (queryIndex != -1) {
            query = internalVal.substring(queryIndex + 1)
            internalVal = internalVal.substring(0, queryIndex)
        }
        internalVal = UrlEncodeDecoder.decode(internalVal)
        remainValue = internalVal
    }
}