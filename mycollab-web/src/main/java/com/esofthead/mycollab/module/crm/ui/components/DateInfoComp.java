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
package com.esofthead.mycollab.module.crm.ui.components;

import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class DateInfoComp extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(DateInfoComp.class);

	public void displayEntryDateTime(ValuedBean bean) {
		this.removeAllComponents();
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false, false, false, true));
		Label dateInfoHeader = new Label(FontAwesome.CALENDAR.getHtml() + " " +
				AppContext.getMessage(CrmCommonI18nEnum.SUB_INFO_DATES), ContentMode.HTML);
		dateInfoHeader.setStyleName("info-hdr");
		this.addComponent(dateInfoHeader);

		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(false, false, false, true));
		try {
			Date createdDate = (Date) PropertyUtils.getProperty(bean,
					"createdtime");
			Label createdDateLbl = new Label(AppContext.getMessage(
					CrmCommonI18nEnum.ITEM_CREATED_DATE,
					DateTimeUtils.getPrettyDateValue(createdDate,
							AppContext.getUserLocale())));
			createdDateLbl.setDescription(AppContext
					.formatDateTime(createdDate));

			layout.addComponent(createdDateLbl);

			Date updatedDate = (Date) PropertyUtils.getProperty(bean,
					"lastupdatedtime");
			Label updatedDateLbl = new Label(AppContext.getMessage(
					CrmCommonI18nEnum.ITEM_UPDATED_DATE,
					DateTimeUtils.getPrettyDateValue(updatedDate,
							AppContext.getUserLocale())));
			updatedDateLbl.setDescription(AppContext
					.formatDateTime(updatedDate));

			layout.addComponent(updatedDateLbl);
			this.addComponent(layout);
		} catch (Exception e) {
			LOG.error("Get date is failed {}", BeanUtility.printBeanObj(bean));
		}
	}
}
