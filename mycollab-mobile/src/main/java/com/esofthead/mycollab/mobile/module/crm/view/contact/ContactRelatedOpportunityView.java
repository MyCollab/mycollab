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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityListDisplay;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactRelatedOpportunityView extends AbstractRelatedListView<SimpleOpportunity, OpportunitySearchCriteria> {
    private static final long serialVersionUID = -2085487353401924075L;
    private Contact contact;

    public ContactRelatedOpportunityView() {
        initUI();
    }

    private void initUI() {
        this.setCaption(AppContext.getMessage(OpportunityI18nEnum.M_TITLE_RELATED_OPPORTUNITIES));
        itemList = new OpportunityListDisplay();
        this.setContent(itemList);
    }

    public void displayOpportunities(final Contact contact) {
        this.contact = contact;
        loadOpportunities();
    }

    private void loadOpportunities() {
        final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        criteria.setContactId(new NumberSearchField(SearchField.AND, contact.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadOpportunities();
    }

    @Override
    protected Component createRightComponent() {
        final NavigationBarQuickMenu addOpportunity = new NavigationBarQuickMenu();
        addOpportunity.setStyleName("add-btn");

        MVerticalLayout addBtns = new MVerticalLayout().withWidth("100%");

        Button newOpportunityBtn = new Button(AppContext.getMessage(OpportunityI18nEnum.NEW), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                fireNewRelatedItem("");
            }
        });
        addBtns.addComponent(newOpportunityBtn);

        Button selectOpportunityBtn = new Button(AppContext.getMessage(OpportunityI18nEnum.M_TITLE_SELECT_OPPORTUNITIES), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ContactOpportunitySelectionView opportunitySelectionView = new ContactOpportunitySelectionView(ContactRelatedOpportunityView.this);
                OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                opportunitySelectionView.setSearchCriteria(criteria);
                EventBusFactory.getInstance().post(new ShellEvent.PushView(ContactRelatedOpportunityView.this, opportunitySelectionView));
            }
        });
        addBtns.addComponent(selectOpportunityBtn);
        addOpportunity.setContent(addBtns);
        return addOpportunity;
    }

}
