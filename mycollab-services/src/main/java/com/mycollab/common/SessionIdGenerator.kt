package com.mycollab.common

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
abstract class SessionIdGenerator {

    abstract val sessionIdApp: String

    companion object {
        private lateinit var instance: SessionIdGenerator

        fun registerSessionIdGenerator(provider: SessionIdGenerator) {
            instance = provider
        }

        val sessionId: String
            get() = instance.sessionIdApp
    }
}
