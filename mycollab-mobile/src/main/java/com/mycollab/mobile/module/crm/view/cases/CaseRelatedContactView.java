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
package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.CaseWithBLOBs;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CaseRelatedContactView extends AbstractRelatedListView<SimpleContact, ContactSearchCriteria> {
    private static final long serialVersionUID = 5099516420497442125L;
    private CaseWithBLOBs myCase;

    public CaseRelatedContactView() {
        this.setCaption(UserUIContext.getMessage(ContactI18nEnum.M_TITLE_RELATED_CONTACTS));
        itemList = new ContactListDisplay();
        this.setContent(itemList);
    }

    public void displayContacts(CaseWithBLOBs cases) {
        this.myCase = cases;
        loadContacts();
    }

    private void loadContacts() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        criteria.setCaseId(new NumberSearchField(myCase.getId()));
        this.setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadContacts();
    }

    @Override
    protected Component createRightComponent() {
        final NavigationBarQuickMenu addContact = new NavigationBarQuickMenu();
        addContact.setStyleName("add-btn");

        MVerticalLayout addBtns = new MVerticalLayout().withFullWidth();

        Button newContactBtn = new Button(UserUIContext.getMessage(ContactI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""));
        addBtns.addComponent(newContactBtn);

        Button selectContact = new Button(UserUIContext.getMessage(ContactI18nEnum.M_TITLE_SELECT_CONTACTS), clickEvent -> {
            CaseContactSelectionView contactSelectionView = new CaseContactSelectionView(CaseRelatedContactView.this);
            ContactSearchCriteria criteria = new ContactSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
            contactSelectionView.setSearchCriteria(criteria);
            EventBusFactory.getInstance().post(new ShellEvent.PushView(CaseRelatedContactView.this, contactSelectionView));
        });
        addBtns.addComponent(selectContact);
        addContact.setContent(addBtns);
        return addContact;
    }
}
