/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.reporting;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;

import java.util.UUID;

/**
 * @author Mycollab Ltd
 * @since 5.2.11
 */
public class PrintButton extends Button {
    private BrowserWindowOpener printWindowOpener;
    private FormReportStreamSource formReportStreamSource;

    public PrintButton() {
        setIcon(FontAwesome.PRINT);
        formReportStreamSource = new FormReportStreamSource(new FormReportTemplateExecutor(""));
        printWindowOpener = new BrowserWindowOpener(new StreamResource(formReportStreamSource, UUID.randomUUID().toString() + ".pdf"));
        printWindowOpener.extend(this);
    }

    public void doPrint(Object bean, FormReportLayout formReportLayout) {
        formReportStreamSource.setBean(bean);
        formReportStreamSource.setFormLayout(formReportLayout);
    }
}
