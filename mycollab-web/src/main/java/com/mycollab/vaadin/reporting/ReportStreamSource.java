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
package com.mycollab.vaadin.reporting;

import com.mycollab.reporting.ReportTemplateExecutor;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.StreamResource;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public abstract class ReportStreamSource implements StreamResource.StreamSource {

    private ReportTemplateExecutor templateExecutor;

    public ReportStreamSource(ReportTemplateExecutor templateExecutor) {
        this.templateExecutor = templateExecutor;
    }

    @Override
    public InputStream getStream() {
        templateExecutor.setParameters(initReportParameters());
        return templateExecutor.exportStream();
    }

    private Map<String, Object> initReportParameters() {
        Map<String, Object> parameters = new ConcurrentHashMap<>();
        parameters.put("siteUrl", AppUI.getSiteUrl());
        parameters.put("user", UserUIContext.getUser());
        initReportParameters(parameters);
        return parameters;
    }

    protected abstract void initReportParameters(Map<String, Object> parameters);
}
