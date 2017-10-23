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

import com.mycollab.core.MyCollabException

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
class FormReportStreamSource<B>(templateExecutor: FormReportTemplateExecutor<B>) : ReportStreamSource(templateExecutor) {
    var bean: B? = null
    private lateinit var formReportLayout: FormReportLayout

    fun setFormLayout(formReportLayout: FormReportLayout) {
        this.formReportLayout = formReportLayout
    }

    override fun initReportParameters(parameters: MutableMap<String, Any>) {
        if (bean != null) {
            parameters.put("bean", bean!!)
        } else {
            throw MyCollabException("Bean must be not null")
        }

        parameters.put("layout", formReportLayout)
    }
}
