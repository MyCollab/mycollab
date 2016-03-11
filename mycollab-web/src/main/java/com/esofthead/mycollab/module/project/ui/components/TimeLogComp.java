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
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public abstract class TimeLogComp<B extends ValuedBean> extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    protected ItemTimeLoggingService itemTimeLoggingService;
    protected B beanItem;
    private Label billableHoursLbl, nonBillableHoursLbl, remainHoursLbl;

    protected TimeLogComp() {
        this.itemTimeLoggingService = ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class);
        this.withMargin(false);

        HorizontalLayout header = new MHorizontalLayout().withStyleName("info-hdr");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        Label dateInfoHeader = new Label(FontAwesome.CLOCK_O.getHtml() + " " +
                AppContext.getMessage(TimeTrackingI18nEnum.SUB_INFO_TIME), ContentMode.HTML);
        header.addComponent(dateInfoHeader);

        if (hasEditPermission()) {
            Button editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    showEditTimeWindow(beanItem);
                }
            });
            editBtn.setStyleName(UIConstants.BUTTON_LINK);
            header.addComponent(editBtn);
        }

        this.addComponent(header);

        MVerticalLayout layout = new MVerticalLayout().withWidth("100%").withMargin(new MarginInfo
                (false, false, false, true));

        billableHoursLbl = new Label();
        nonBillableHoursLbl = new Label();
        remainHoursLbl = new Label();
        layout.addComponent(billableHoursLbl);
        layout.addComponent(nonBillableHoursLbl);
        layout.addComponent(remainHoursLbl);
        this.addComponent(layout);
    }

    public void displayTime(final B bean) {
        this.beanItem = bean;
        Double billableHours = getTotalBillableHours(beanItem);
        Double nonBillableHours = getTotalNonBillableHours(beanItem);
        Double remainHours = getRemainedHours(beanItem);
        billableHoursLbl.setValue(String.format(AppContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS), billableHours));
        nonBillableHoursLbl.setValue(String.format(AppContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS), nonBillableHours));
        remainHoursLbl.setValue(String.format(AppContext.getMessage(TimeTrackingI18nEnum.OPT_REMAIN_HOURS), remainHours));
    }

    protected abstract Double getTotalBillableHours(B bean);

    protected abstract Double getTotalNonBillableHours(B bean);

    protected abstract Double getRemainedHours(B bean);

    protected abstract boolean hasEditPermission();

    protected abstract void showEditTimeWindow(B bean);
}
