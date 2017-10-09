package com.mycollab.common

import com.mycollab.core.MyCollabException

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class InvalidTokenException(message: String) : MyCollabException(message) {
    companion object {
        private val serialVersionUID = 1L
    }
}
