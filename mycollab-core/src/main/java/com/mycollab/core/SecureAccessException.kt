package com.mycollab.core

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
open class SecureAccessException : MyCollabException {
    constructor() : super("") {}

    constructor(message: String) : super(message) {}
}
