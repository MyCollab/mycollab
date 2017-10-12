/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
