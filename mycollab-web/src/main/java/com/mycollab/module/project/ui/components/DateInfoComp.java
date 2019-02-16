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
package com.mycollab.module.project.ui.components;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDateTime;

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
        Label dateInfoHeader = ELabel.html(VaadinIcons.CALENDAR.getHtml() + " " +
                UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_DATES)).withStyleName("info-hdr");
        this.addComponent(dateInfoHeader);

        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.setWidth("100%");
        layout.setMargin(new MarginInfo(false, false, false, true));
        layout.setColumnExpandRatio(1, 1.0f);

        try {
            LocalDateTime createdDate = (LocalDateTime) PropertyUtils.getProperty(bean, "createdtime");
            layout.addComponent(new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_DATE)).withStyleName(WebThemes.META_COLOR).withUndefinedWidth(), 0, 0);
            layout.addComponent(new ELabel(UserUIContext.formatPrettyTime(createdDate)).withDescription(UserUIContext.formatDateTime(createdDate)), 1, 0);


            LocalDateTime updatedDate = (LocalDateTime) PropertyUtils.getProperty(bean, "lastupdatedtime");
            layout.addComponent(new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_UPDATED_DATE)).withStyleName(WebThemes.META_COLOR).withUndefinedWidth(), 0, 1);
            layout.addComponent(new ELabel(UserUIContext.formatPrettyTime(updatedDate)).withDescription(UserUIContext.formatDateTime(updatedDate)), 1, 1);

            this.addComponent(layout);
        } catch (Exception e) {
            LOG.error("Get date is failed {}", BeanUtility.printBeanObj(bean));
        }
    }
}
