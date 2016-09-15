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
package com.mycollab.module.project.ui.components;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
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
        Label dateInfoHeader = ELabel.html(FontAwesome.CALENDAR.getHtml() + " " +
                UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_DATES));
        dateInfoHeader.setStyleName("info-hdr");
        this.addComponent(dateInfoHeader);

        MVerticalLayout layout = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true))
                .withFullWidth();
        try {
            Date createdDate = (Date) PropertyUtils.getProperty(bean, "createdtime");
            ELabel createdDateLbl = new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_DATE,
                    UserUIContext.formatPrettyTime(createdDate))).withDescription(
                    UserUIContext.formatDateTime(createdDate));

            layout.addComponent(createdDateLbl);

            Date updatedDate = (Date) PropertyUtils.getProperty(bean, "lastupdatedtime");
            ELabel updatedDateLbl = new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_UPDATED_DATE,
                    UserUIContext.formatPrettyTime(updatedDate))).withDescription(UserUIContext.formatDateTime(updatedDate));
            layout.addComponent(updatedDateLbl);

            this.addComponent(layout);
        } catch (Exception e) {
            LOG.error("Get date is failed {}", BeanUtility.printBeanObj(bean));
        }
    }
}
