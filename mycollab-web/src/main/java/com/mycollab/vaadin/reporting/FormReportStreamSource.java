package com.mycollab.vaadin.reporting;

import com.mycollab.core.MyCollabException;

import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
public class FormReportStreamSource<B> extends ReportStreamSource {
    private B bean;
    private FormReportLayout formReportLayout;

    public FormReportStreamSource(FormReportTemplateExecutor<B> templateExecutor) {
        super(templateExecutor);
    }

    public B getBean() {
        return bean;
    }

    public void setBean(B bean) {
        this.bean = bean;
    }

    public void setFormLayout(FormReportLayout formReportLayout) {
        this.formReportLayout = formReportLayout;
    }

    @Override
    protected void initReportParameters(Map<String, Object> parameters) {
        if (bean != null) {
            parameters.put("bean", bean);
        } else {
            throw new MyCollabException("Bean must be not null");
        }

        parameters.put("layout", formReportLayout);
    }
}
