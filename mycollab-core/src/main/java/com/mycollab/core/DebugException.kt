package com.mycollab.core

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
class DebugException : MyCollabException {
    constructor(message: String) : super(message)

    constructor(message: String, e: Throwable) : super(message, e)
}
