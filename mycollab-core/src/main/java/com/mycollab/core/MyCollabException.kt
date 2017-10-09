package com.mycollab.core

/**
 * Generic exception of MyCollab. All exceptions occurs in MyCollab should be
 * wrapped into this exception type.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
open class MyCollabException : RuntimeException {

    constructor(message: String) : super(message) {}

    constructor(e: Throwable) : super(e) {}

    constructor(message: String, e: Throwable) : super(message, e) {}
}
