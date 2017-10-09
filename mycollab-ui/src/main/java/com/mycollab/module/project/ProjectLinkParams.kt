package com.mycollab.module.project

import com.mycollab.core.MyCollabException

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author MyCollab Ltd.
 * @since 4.5.1
 */
object ProjectLinkParams {
    private val PATTERN = Pattern.compile("^\\w{1,3}-\\d*$")

    fun isValidParam(param: String): Boolean {
        val matcher = PATTERN.matcher(param)
        return matcher.find()
    }

    fun getProjectShortName(param: String): String {
        val index = param.indexOf("-")
        return when {
            index > 0 -> param.substring(0, index)
            else -> throw MyCollabException("Invalid param " + param)
        }
    }

    fun getItemKey(param: String): Int {
        val index = param.indexOf("-")
        return if (index > 0) {
            Integer.parseInt(param.substring(index + 1))
        } else {
            throw MyCollabException("Invalid param " + param)
        }
    }
}
