package com.mycollab.core

/**
 * This exception occur when MyCollab can not find any resource (Document, User,
 * etc)
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ResourceNotFoundException : MyCollabException {

    constructor(message: String) : super(message)

    constructor(e: Throwable) : super(e)

    constructor() : super("")

}
