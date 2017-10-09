package com.mycollab.core

/**
 * This exception when user do some invalid action such as typing wrong
 * password, invalid input. Note that MyCollab catch this type exception to
 * recognize user mistake
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
open class UserInvalidInputException : MyCollabException {

    constructor(message: String) : super(message)

    constructor(e: Throwable) : super(e)

    constructor(message: String, e: Throwable) : super(message, e)
}
