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
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.esofthead.mycollab.module.crm.view.contact.ContactSimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.contact.ContactTableDisplay;
import com.esofthead.mycollab.module.crm.view.contact.ContactTableFieldDef;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.ui.Button;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignContactSelectionWindow extends RelatedItemSelectionWindow<SimpleContact, ContactSearchCriteria> {

    public CampaignContactSelectionWindow(CampaignContactListComp associateContactList) {
        super("Select Contacts", associateContactList);
        this.setWidth("900px");
    }

    @Override
    protected void initUI() {
        tableItem = new ContactTableDisplay(ContactTableFieldDef.selected(),
                Arrays.asList(ContactTableFieldDef.name(), ContactTableFieldDef.email(),
                        ContactTableFieldDef.phoneOffice(), ContactTableFieldDef.account()));

        Button selectBtn = new Button("Select", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        selectBtn.setStyleName(UIConstants.BUTTON_ACTION);

        ContactSimpleSearchPanel contactSimpleSearchPanel = new ContactSimpleSearchPanel();
        contactSimpleSearchPanel.addSearchHandler(new SearchHandler<ContactSearchCriteria>() {

            @Override
            public void onSearch(ContactSearchCriteria criteria) {
                tableItem.setSearchCriteria(criteria);
            }

        });

        bodyContent.addComponent(contactSimpleSearchPanel);
        bodyContent.addComponent(selectBtn);
        bodyContent.addComponent(tableItem);
    }

}
