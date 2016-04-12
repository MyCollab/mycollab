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

package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.MeetingSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.Depot;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class MeetingListDashlet extends Depot {

    private MeetingTableDisplay tableItem;

    public MeetingListDashlet() {
        super("My Meetings", new VerticalLayout());
        this.setMargin(new MarginInfo(true, false, false, false));

        tableItem = new MeetingTableDisplay(Arrays.asList(new TableViewField(
                MeetingI18nEnum.FORM_SUBJECT, "subject",
                UIConstants.TABLE_X_LABEL_WIDTH), new TableViewField(
                TaskI18nEnum.FORM_START_DATE, "startdate",
                UIConstants.TABLE_DATE_TIME_WIDTH), new TableViewField(
                ActivityI18nEnum.FORM_STATUS, "status",
                UIConstants.TABLE_S_LABEL_WIDTH)));

        bodyContent.addComponent(tableItem);

        Button customizeViewBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {

            }
        });
        customizeViewBtn.setIcon(FontAwesome.ADJUST);
        customizeViewBtn.setDescription("Layout Options");
        customizeViewBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);

        this.addHeaderElement(customizeViewBtn);
    }

    public void display() {
        final MeetingSearchCriteria criteria = new MeetingSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setAssignUsers(new SetSearchField<>(new String[]{AppContext.getUsername()}));
        tableItem.setSearchCriteria(criteria);
    }
}
