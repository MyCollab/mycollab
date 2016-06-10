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

package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.Depot;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadListDashlet extends Depot {

    private LeadTableDisplay tableItem;

    public LeadListDashlet() {
        super("My Leads", new VerticalLayout());
        this.setMargin(new MarginInfo(true, false, false, false));

        tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name(),
                LeadTableFieldDef.email(), LeadTableFieldDef.phoneoffice()));
        bodyContent.addComponent(tableItem);

        Button customizeViewBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(new LeadListCustomizeWindow(tableItem));

            }
        });
        customizeViewBtn.setIcon(FontAwesome.ADJUST);
        customizeViewBtn.setDescription("Layout Options");
        customizeViewBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);

        this.addHeaderElement(customizeViewBtn);
    }

    public void display() {
        final LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setAssignUsers(new SetSearchField<>(AppContext.getUsername()));
        tableItem.setSearchCriteria(criteria);
    }
}
