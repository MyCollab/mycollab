package com.mycollab.security

import com.mycollab.common.i18n.SecurityI18nEnum

/**
 * Boolean permission flag
 *
 * @author MyCollab Ltd
 * @version 1.0
 */
class BooleanPermissionFlag : PermissionFlag() {
    companion object {
        @JvmField val TRUE = 128

        @JvmField val FALSE = 129

        /**
         * Check whether `flag` is true permission
         *
         * @param flag
         * @return
         */
        fun beTrue(flag: Int?): Boolean {
            return flag == TRUE
        }

        /**
         * Check whether `flag` is false permission
         *
         * @param flag
         * @return
         */
        fun beFalse(flag: Int?): Boolean {
            return flag == FALSE
        }

        fun toKey(flag: Int?): SecurityI18nEnum {
            return if (flag == TRUE) SecurityI18nEnum.YES else SecurityI18nEnum.NO
        }
    }
}
