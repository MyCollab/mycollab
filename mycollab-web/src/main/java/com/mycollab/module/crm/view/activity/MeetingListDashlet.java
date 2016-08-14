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

package com.mycollab.module.crm.view.activity;

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.crm.domain.criteria.MeetingSearchCriteria;
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;

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

        tableItem = new MeetingTableDisplay(Arrays.asList(
                new TableViewField(MeetingI18nEnum.FORM_SUBJECT, "subject", WebUIConstants.TABLE_X_LABEL_WIDTH),
                new TableViewField(GenericI18Enum.FORM_START_DATE, "startdate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_S_LABEL_WIDTH)));

        bodyContent.addComponent(tableItem);

        MButton customizeViewBtn = new MButton("", clickEvent -> {}).withIcon(FontAwesome.ADJUST).withStyleName(WebUIConstants.BUTTON_ICON_ONLY)
                .withDescription(AppContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));

        this.addHeaderElement(customizeViewBtn);
    }

    public void display() {
        final MeetingSearchCriteria criteria = new MeetingSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setAssignUsers(new SetSearchField<>(AppContext.getUsername()));
        tableItem.setSearchCriteria(criteria);
    }
}
