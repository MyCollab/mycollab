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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.domain

import com.mycollab.module.file.PathUtils

import java.util.Calendar

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
open class Content : Resource {
    var title = ""
    var lastModified: Calendar? = null
    var lastModifiedBy: String? = null
    var mimeType: String? = null
    var thumbnail: String? = null

    var thumbnailMobile: String?
        get() = thumbnail
        set(thumbnailMobile) {
            val thumbnailMobile1 = thumbnailMobile
        }

    constructor() : super()

    constructor(path: String) {
        this.path = path
    }
}
