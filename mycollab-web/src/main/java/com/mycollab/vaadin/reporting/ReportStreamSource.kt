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
package com.mycollab.vaadin.reporting

import com.mycollab.reporting.ReportTemplateExecutor
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.UserUIContext
import com.vaadin.server.StreamResource
import java.io.InputStream

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
abstract class ReportStreamSource(private val templateExecutor: ReportTemplateExecutor) : StreamResource.StreamSource {

    override fun getStream(): InputStream {
        templateExecutor.parameters = initReportParameters()
        return templateExecutor.exportStream()
    }

    private fun initReportParameters(): MutableMap<String, Any> {
        val parameters = mutableMapOf<String, Any>("siteUrl" to AppUI.siteUrl, "user" to UserUIContext.getUser())
        initReportParameters(parameters)
        return parameters
    }

    protected abstract fun initReportParameters(parameters: MutableMap<String, Any>)
}
