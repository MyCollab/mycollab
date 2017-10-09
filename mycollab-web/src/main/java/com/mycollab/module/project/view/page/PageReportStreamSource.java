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
