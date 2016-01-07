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
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.CaseWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
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
        this.setCaption(AppContext.getMessage(ContactI18nEnum.M_TITLE_RELATED_CONTACTS));
        itemList = new ContactListDisplay();
        this.setContent(itemList);
    }

    public void displayContacts(CaseWithBLOBs cases) {
        this.myCase = cases;
        loadContacts();
    }

    private void loadContacts() {
        ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        criteria.setCaseId(new NumberSearchField(SearchField.AND, myCase.getId()));
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

        MVerticalLayout addBtns = new MVerticalLayout().withWidth("100%");

        Button newContactBtn = new Button(AppContext.getMessage(ContactI18nEnum.VIEW_NEW_TITLE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                fireNewRelatedItem("");
            }
        });
        addBtns.addComponent(newContactBtn);

        Button selectContact = new Button(AppContext.getMessage(ContactI18nEnum.M_TITLE_SELECT_CONTACTS), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                CaseContactSelectionView contactSelectionView = new CaseContactSelectionView(CaseRelatedContactView.this);
                ContactSearchCriteria criteria = new ContactSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                contactSelectionView.setSearchCriteria(criteria);
                EventBusFactory.getInstance().post(new ShellEvent.PushView(CaseRelatedContactView.this, contactSelectionView));
            }
        });
        addBtns.addComponent(selectContact);
        addContact.setContent(addBtns);
        return addContact;
    }
}
