/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.mobile.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.0
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

		HorizontalLayout header = new HorizontalLayout();
		header.setSpacing(true);
		header.setStyleName("info-hdr");
		header.setWidth("100%");
		Label dateInfoHeader = new Label(
				AppContext.getMessage(TimeTrackingI18nEnum.SUB_INFO_TIME));
		dateInfoHeader.setWidthUndefined();
		header.addComponent(dateInfoHeader);

		if (hasEditPermission()) {
			Button editBtn = new Button(
					"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
							+ IconConstants.OPEN_NEW_VIEW + "\"></span>",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							showEditTimeView(bean);

						}
					});
			editBtn.setStyleName("link");
			editBtn.setHtmlContentAllowed(true);
			header.addComponent(editBtn);
			header.setComponentAlignment(editBtn, Alignment.BOTTOM_LEFT);
			header.setExpandRatio(editBtn, 1.0f);
		}

		this.addComponent(header);

		GridFormLayoutHelper layout = new GridFormLayoutHelper(1, 3, "100%",
				"150px", Alignment.TOP_RIGHT);
		layout.getLayout().setWidth("100%");
		layout.getLayout().setMargin(false);

		double billableHours = getTotalBillableHours(bean);
		double nonBillableHours = getTotalNonBillableHours(bean);
		double remainHours = getRemainedHours(bean);
		layout.addComponent(new Label(billableHours + ""), AppContext
				.getMessage(TimeTrackingI18nEnum.M_FORM_BILLABLE_HOURS), 0, 0);
		layout.addComponent(new Label(nonBillableHours + ""), AppContext
				.getMessage(TimeTrackingI18nEnum.M_FORM_NON_BILLABLE_HOURS), 0,
				1);
		layout.addComponent(
				new Label(remainHours + ""),
				AppContext.getMessage(TimeTrackingI18nEnum.M_FORM_REMAIN_HOURS),
				0, 2);
		this.addComponent(layout.getLayout());
	}

	protected abstract Double getTotalBillableHours(V bean);

	protected abstract Double getTotalNonBillableHours(V bean);

	protected abstract Double getRemainedHours(V bean);

	protected abstract boolean hasEditPermission();

	protected abstract void showEditTimeView(V bean);
}
