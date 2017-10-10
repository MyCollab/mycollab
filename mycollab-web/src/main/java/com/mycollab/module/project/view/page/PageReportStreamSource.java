/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.page;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.page.domain.Page;
import com.mycollab.vaadin.reporting.ReportStreamSource;

import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.4.0
 */
class PageReportStreamSource extends ReportStreamSource {

    private Page page;

    PageReportStreamSource(Page page) {
        super(new PageReportTemplateExecutor("Page"));
        this.page = page;
    }

    @Override
    protected void initReportParameters(Map<String, Object> parameters) {
        if (page != null) {
            parameters.put("bean", page);
        } else {
            throw new MyCollabException("Bean must be not null");
        }
    }
}
