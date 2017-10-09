package com.mycollab.module.project

import com.mycollab.core.SecureAccessException

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
class UserNotBelongProjectException : SecureAccessException {
    constructor() : super("")

    constructor(message: String) : super(message)
}
