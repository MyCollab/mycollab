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
