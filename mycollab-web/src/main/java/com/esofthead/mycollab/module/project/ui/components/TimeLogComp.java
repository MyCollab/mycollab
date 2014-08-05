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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public abstract class TimeLogComp<V extends ValuedBean> extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	protected ItemTimeLoggingService itemTimeLoggingService;

	protected TimeLogComp() {
		this.itemTimeLoggingService = ApplicationContextUtil
				.getSpringBean(ItemTimeLoggingService.class);
	}

	public void displayTime(final V bean) {
		this.removeAllComponents();
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false, false, false, true));

		HorizontalLayout header = new HorizontalLayout();
		header.setSpacing(true);
		Label dateInfoHeader = new Label(
				AppContext.getMessage(TimeTrackingI18nEnum.SUB_INFO_TIME));
		dateInfoHeader.setStyleName("info-hdr");
		header.addComponent(dateInfoHeader);

		if (hasEditPermission()) {
			Button editBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							showEditTimeWindow(bean);

						}
					});
			editBtn.setStyleName("link");
			editBtn.addStyleName("info-hdr");
			header.addComponent(editBtn);
		}

		this.addComponent(header);

		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(false, false, false, true));

		double billableHours = getTotalBillableHours(bean);
		double nonBillableHours = getTotalNonBillableHours(bean);
		double remainHours = getRemainedHours(bean);
		layout.addComponent(new Label(String.format(
				AppContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS),
				billableHours)));
		layout.addComponent(new Label(String.format(AppContext
				.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS),
				nonBillableHours)));
		layout.addComponent(new Label(String.format(
				AppContext.getMessage(TimeTrackingI18nEnum.OPT_REMAIN_HOURS),
				remainHours)));
		this.addComponent(layout);
	}

	protected abstract Double getTotalBillableHours(V bean);

	protected abstract Double getTotalNonBillableHours(V bean);

	protected abstract Double getRemainedHours(V bean);

	protected abstract boolean hasEditPermission();

	protected abstract void showEditTimeWindow(V bean);
}
