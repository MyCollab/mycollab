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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class DateInfoComp extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(DateInfoComp.class);

    public void displayEntryDateTime(ValuedBean bean) {
        this.removeAllComponents();
        this.withMargin(false);
        Label dateInfoHeader = new Label(FontAwesome.CALENDAR.getHtml() + " " +
                AppContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_DATES), ContentMode.HTML);
        dateInfoHeader.setStyleName("info-hdr");
        this.addComponent(dateInfoHeader);

        MVerticalLayout layout = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true))
                .withFullWidth();
        try {
            Date createdDate = (Date) PropertyUtils.getProperty(bean, "createdtime");
            ELabel createdDateLbl = new ELabel(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_DATE,
                    AppContext.formatPrettyTime(createdDate))).withDescription(
                    AppContext.formatDateTime(createdDate));

            layout.addComponent(createdDateLbl);

            Date updatedDate = (Date) PropertyUtils.getProperty(bean, "lastupdatedtime");
            ELabel updatedDateLbl = new ELabel(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_UPDATED_DATE,
                    AppContext.formatPrettyTime(updatedDate))).withDescription(AppContext.formatDateTime(updatedDate));
            layout.addComponent(updatedDateLbl);

            this.addComponent(layout);
        } catch (Exception e) {
            LOG.error("Get date is failed {}", BeanUtility.printBeanObj(bean));
        }
    }
}
