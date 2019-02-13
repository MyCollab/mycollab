/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.reporting;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import org.vaadin.viritin.button.MButton;

import java.util.UUID;

/**
 * @author Mycollab Ltd
 * @since 5.2.11
 */
public class PrintButton<B> extends MButton {
    private FormReportStreamSource<B> formReportStreamSource;

    public PrintButton() {
        this("");
    }

    public PrintButton(String caption) {
        setCaption(caption);
        setIcon(VaadinIcons.PRINT);
        formReportStreamSource = new FormReportStreamSource<>(new FormReportTemplateExecutor<>("", UserUIContext.getUserTimeZone(), UserUIContext.getUserLocale()));
        BrowserWindowOpener printWindowOpener = new BrowserWindowOpener(new StreamResource(formReportStreamSource, UUID.randomUUID().toString() + ".pdf"));
        printWindowOpener.extend(this);
    }

    public void doPrint(B bean, FormReportLayout formReportLayout) {
        formReportStreamSource.setBean(bean);
        formReportStreamSource.setFormLayout(formReportLayout);
    }
}
