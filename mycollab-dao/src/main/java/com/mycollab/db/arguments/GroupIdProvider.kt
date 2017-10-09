package com.mycollab.db.arguments

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
abstract class GroupIdProvider {

    abstract val groupId: Int

    abstract val groupRequestedUser: String

    companion object {
        private var instance: GroupIdProvider? = null

        fun registerAccountIdProvider(provider: GroupIdProvider) {
            instance = provider
        }

        val accountId: Int
            get() = if (instance != null) {
                try {
                    instance!!.groupId
                } catch (e: Exception) {
                    0
                }

            } else {
                0
            }

        val requestedUser: String
            get() = if (instance != null) {
                try {
                    instance!!.groupRequestedUser
                } catch (e: Exception) {
                    ""
                }

            } else {
                ""
            }
    }
}
