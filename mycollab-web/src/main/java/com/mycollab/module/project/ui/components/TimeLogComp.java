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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
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
    private GridLayout layout;

    protected TimeLogComp() {
        this.itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);
        this.withMargin(false);

        HorizontalLayout header = new MHorizontalLayout().withStyleName("info-hdr");
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        ELabel dateInfoHeader = ELabel.html(VaadinIcons.CLOCK.getHtml() + " " +
                UserUIContext.getMessage(TimeTrackingI18nEnum.SUB_INFO_TIME));
        header.addComponent(dateInfoHeader);

        if (hasEditPermission()) {
            MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent ->
                    showEditTimeWindow(beanItem)).withStyleName(WebThemes.BUTTON_LINK);
            header.addComponent(editBtn);
        }
        header.addComponent(ELabel.fontIcon(VaadinIcons.QUESTION_CIRCLE).withDescription(UserUIContext.getMessage
                (TimeTrackingI18nEnum.TIME_EXPLAIN_HELP)).withStyleName(WebThemes.INLINE_HELP));

        this.addComponent(header);

        layout = new GridLayout(2, 3);
        layout.setSpacing(true);
        layout.setWidth("100%");
        layout.setMargin(new MarginInfo(false, false, false, true));
        layout.setColumnExpandRatio(1, 1.0f);
        this.addComponent(layout);
    }

    public void displayTime(B bean) {
        layout.removeAllComponents();
        this.beanItem = bean;
        Double billableHours = getTotalBillableHours(beanItem);
        Double nonBillableHours = getTotalNonBillableHours(beanItem);
        Double remainHours = getRemainedHours(beanItem);

        ELabel billableHoursLbl = new ELabel(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS)).withStyleName(WebThemes.META_COLOR)
                .withUndefinedWidth();
        layout.addComponent(billableHoursLbl, 0, 0);
        layout.addComponent(new ELabel(billableHours + ""), 1, 0);

        ELabel nonBillableHoursLbl = new ELabel(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS)).withStyleName(WebThemes.META_COLOR)
                .withUndefinedWidth();
        layout.addComponent(nonBillableHoursLbl, 0, 1);
        layout.addComponent(new ELabel(nonBillableHours + ""), 1, 1);

        ELabel remainHoursLbl = new ELabel(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_REMAIN_HOURS)).withStyleName(WebThemes.META_COLOR)
                .withUndefinedWidth();
        layout.addComponent(remainHoursLbl, 0, 2);
        layout.addComponent(new ELabel(remainHours + ""), 1, 2);
    }

    protected abstract Double getTotalBillableHours(B bean);

    protected abstract Double getTotalNonBillableHours(B bean);

    protected abstract Double getRemainedHours(B bean);

    protected abstract boolean hasEditPermission();

    protected abstract void showEditTimeWindow(B bean);
}
