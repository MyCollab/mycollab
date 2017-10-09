package com.mycollab.vaadin.reporting;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import org.vaadin.viritin.button.MButton;

import java.util.UUID;

/**
 * @author Mycollab Ltd
 * @since 5.2.11
 */
public class PrintButton extends MButton {
    private FormReportStreamSource formReportStreamSource;

    public PrintButton() {
        setIcon(FontAwesome.PRINT);
        formReportStreamSource = new FormReportStreamSource(new FormReportTemplateExecutor(""));
        BrowserWindowOpener printWindowOpener = new BrowserWindowOpener(new StreamResource(formReportStreamSource, UUID.randomUUID().toString() + ".pdf"));
        printWindowOpener.extend(this);
    }

    public void doPrint(Object bean, FormReportLayout formReportLayout) {
        formReportStreamSource.setBean(bean);
        formReportStreamSource.setFormLayout(formReportLayout);
    }
}
