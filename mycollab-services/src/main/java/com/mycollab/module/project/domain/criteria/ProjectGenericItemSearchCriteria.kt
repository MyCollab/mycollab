package com.mycollab.module.project.domain.criteria

import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ProjectGenericItemSearchCriteria : SearchCriteria() {
    var prjKeys: SetSearchField<Int>? = null
    var txtValue: StringSearchField? = null
    var createdUsers: SetSearchField<String>? = null
    var types: SetSearchField<String>? = null
    var monitorProjectIds: SetSearchField<Int>? = null
    var tagNames: SetSearchField<String>? = null
}