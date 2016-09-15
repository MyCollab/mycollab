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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.vaadin.viritin.button.MButton;
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
        this.itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);
        this.withMargin(false);

        HorizontalLayout header = new MHorizontalLayout().withStyleName("info-hdr");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        Label dateInfoHeader = new Label(FontAwesome.CLOCK_O.getHtml() + " " +
                UserUIContext.getMessage(TimeTrackingI18nEnum.SUB_INFO_TIME), ContentMode.HTML);
        header.addComponent(dateInfoHeader);

        if (hasEditPermission()) {
            MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent ->
                    showEditTimeWindow(beanItem)).withStyleName(WebUIConstants.BUTTON_LINK);
            header.addComponent(editBtn);
        }
        header.addComponent(ELabel.fontIcon(FontAwesome.QUESTION_CIRCLE).withDescription(UserUIContext.getMessage
                (TimeTrackingI18nEnum.TIME_EXPLAIN_HELP)).withStyleName(WebUIConstants.INLINE_HELP));

        this.addComponent(header);

        MVerticalLayout layout = new MVerticalLayout().withFullWidth().withMargin(new MarginInfo(false, false, false, true));

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
        billableHoursLbl.setValue(String.format("%s: %s", UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS), billableHours));
        nonBillableHoursLbl.setValue(String.format("%s: %s", UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS), nonBillableHours));
        remainHoursLbl.setValue(String.format("%s: %s", UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_REMAIN_HOURS), remainHours));
    }

    protected abstract Double getTotalBillableHours(B bean);

    protected abstract Double getTotalNonBillableHours(B bean);

    protected abstract Double getRemainedHours(B bean);

    protected abstract boolean hasEditPermission();

    protected abstract void showEditTimeWindow(B bean);
}
