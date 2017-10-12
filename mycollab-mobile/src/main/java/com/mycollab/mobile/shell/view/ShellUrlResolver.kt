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
package com.mycollab.mobile.shell.view

import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.vaadin.mvp.UrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ShellUrlResolver : UrlResolver() {
    init {
        this.addSubResolver("crm", CrmUrlResolver().build())
        this.addSubResolver("project", ProjectUrlResolver().build())
    }

    fun navigateByFragement(fragement: String?) {
        if (fragement != null && fragement.isNotBlank()) {
            val tokens = fragement.split("/").toTypedArray()
            this.handle(*tokens)
        } else {
            defaultPageErrorHandler()
        }
    }

    override fun defaultPageErrorHandler() {}
}