package com.mycollab.common.domain

import com.mycollab.core.utils.StringUtils

class SimpleClient : Client() {

    var createdUserAvatarId: String? = null

    var createdUserFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(createduser)
        } else field

    var assignUserAvatarId: String? = null

    var assignUserFullName: String = ""
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(assignuser)
        } else field

    var numProjects: Int? = null

    enum class Field {
        assignUserFullName;

        fun equalTo(value: Any): Boolean = name == value
    }
}