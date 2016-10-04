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
package com.mycollab.mobile.module.project.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public abstract class TimeLogComp<V extends ValuedBean> extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    protected ItemTimeLoggingService itemTimeLoggingService;

    protected TimeLogComp() {
        this.itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);
        this.withFullWidth();
    }

    public void displayTime(final V bean) {
        this.removeAllComponents();

        Label dateInfoHeader = new Label(UserUIContext.getMessage(TimeTrackingI18nEnum.SUB_INFO_TIME));
        MHorizontalLayout header = FormSectionBuilder.build(FontAwesome.CLOCK_O, dateInfoHeader);

        if (hasEditPermission()) {
            MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> showEditTimeView(bean))
                    .withStyleName(MobileUIConstants.BUTTON_LINK);
            header.addComponent(editBtn);
            header.setComponentAlignment(editBtn, Alignment.TOP_RIGHT);
        }

        this.addComponent(header);

        GridFormLayoutHelper layout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

        double billableHours = getTotalBillableHours(bean);
        double nonBillableHours = getTotalNonBillableHours(bean);
        double remainHours = getRemainedHours(bean);
        layout.addComponent(new Label(billableHours + ""), UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS), 0, 0);
        layout.addComponent(new Label(nonBillableHours + ""), UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS), 0, 1);
        layout.addComponent(new Label(remainHours + ""), UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_REMAIN_HOURS), 0, 2);
        this.addComponent(layout.getLayout());
    }

    protected abstract Double getTotalBillableHours(V bean);

    protected abstract Double getTotalNonBillableHours(V bean);

    protected abstract Double getRemainedHours(V bean);

    protected abstract boolean hasEditPermission();

    protected abstract void showEditTimeView(V bean);
}
