package com.mycollab.security

import com.mycollab.common.i18n.SecurityI18nEnum

/**
 * Signal interface of Permission flag
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
open class PermissionFlag {
    companion object {
        @JvmStatic fun toVal(flag: Int?): SecurityI18nEnum {
            return when {
                flag == null || flag == AccessPermissionFlag.NO_ACCESS -> SecurityI18nEnum.NO_ACCESS
                flag == AccessPermissionFlag.READ_ONLY -> SecurityI18nEnum.READONLY
                flag == AccessPermissionFlag.READ_WRITE -> SecurityI18nEnum.READ_WRITE
                flag == AccessPermissionFlag.ACCESS -> SecurityI18nEnum.ACCESS
                flag == BooleanPermissionFlag.TRUE -> SecurityI18nEnum.YES
                flag == BooleanPermissionFlag.FALSE -> SecurityI18nEnum.NO
                else -> SecurityI18nEnum.UNDEFINE
            }
        }
    }
}
