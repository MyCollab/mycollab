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
package com.mycollab.module.project.view.page

import com.mycollab.core.MyCollabException
import com.mycollab.module.page.domain.Page
import com.mycollab.vaadin.reporting.ReportStreamSource

/**
 * @author MyCollab Ltd
 * @since 5.4.0
 */
internal class PageReportStreamSource(private val page: Page?) : ReportStreamSource(PageReportTemplateExecutor("Page")) {

    override fun initReportParameters(parameters: MutableMap<String, Any>) {
        if (page != null) {
            parameters.put("bean", page)
        } else {
            throw MyCollabException("Bean must be not null")
        }
    }
}
