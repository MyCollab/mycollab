package com.mycollab.module.file.domain.criteria

import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.module.ecm.domain.ExternalDrive

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class FileSearchCriteria : SearchCriteria() {

    var rootFolder: String? = null
    var fileName: String? = null
    var baseFolder: String? = null
    var storageName: String? = null
    var externalDrive: ExternalDrive? = null
}
