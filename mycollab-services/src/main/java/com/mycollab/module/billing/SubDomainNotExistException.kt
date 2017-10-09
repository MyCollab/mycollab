package com.mycollab.module.billing

import com.mycollab.core.UserInvalidInputException

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SubDomainNotExistException(errorMsg: String) : UserInvalidInputException(errorMsg) {
    companion object {
        private val serialVersionUID = 1L
    }
}
