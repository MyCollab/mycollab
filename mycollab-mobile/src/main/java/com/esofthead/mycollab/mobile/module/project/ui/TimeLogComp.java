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
import com.esofthead.mycollab.mobile.ui.FormSectionBuilder;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public abstract class TimeLogComp<V extends ValuedBean> extends VerticalLayout {
    private static final long serialVersionUID = 1L;

    protected ItemTimeLoggingService itemTimeLoggingService;

    protected TimeLogComp() {
        this.itemTimeLoggingService = ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class);
        this.setWidth("100%");
        this.setStyleName(UIConstants.FULL_WIDTH_COMP);
    }

    public void displayTime(final V bean) {
        this.removeAllComponents();

        Label dateInfoHeader = new Label(AppContext.getMessage(TimeTrackingI18nEnum.SUB_INFO_TIME));
        MHorizontalLayout header = FormSectionBuilder.build(FontAwesome.CLOCK_O, dateInfoHeader);

//        if (hasEditPermission()) {
//            Button editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void buttonClick(ClickEvent event) {
//                    showEditTimeView(bean);
//                }
//            });
//            editBtn.setStyleName(UIConstants.BUTTON_LINK);
//            header.addComponent(editBtn);
//            header.setComponentAlignment(editBtn, Alignment.TOP_RIGHT);
//        }

        this.addComponent(header);

        GridFormLayoutHelper layout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

        double billableHours = getTotalBillableHours(bean);
        double nonBillableHours = getTotalNonBillableHours(bean);
        double remainHours = getRemainedHours(bean);
        layout.addComponent(new Label(billableHours + ""), AppContext.getMessage(TimeTrackingI18nEnum.M_FORM_BILLABLE_HOURS), 0, 0);
        layout.addComponent(new Label(nonBillableHours + ""), AppContext.getMessage(TimeTrackingI18nEnum.M_FORM_NON_BILLABLE_HOURS), 0, 1);
        layout.addComponent(new Label(remainHours + ""), AppContext.getMessage(TimeTrackingI18nEnum.M_FORM_REMAIN_HOURS), 0, 2);
        this.addComponent(layout.getLayout());
    }

    protected abstract Double getTotalBillableHours(V bean);

    protected abstract Double getTotalNonBillableHours(V bean);

    protected abstract Double getRemainedHours(V bean);

    protected abstract boolean hasEditPermission();

    protected abstract void showEditTimeView(V bean);
}
