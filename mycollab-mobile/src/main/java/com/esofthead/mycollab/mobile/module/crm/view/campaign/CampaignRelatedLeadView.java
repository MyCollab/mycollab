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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignRelatedLeadView extends AbstractRelatedListView<SimpleLead, LeadSearchCriteria> {
    private static final long serialVersionUID = -4503624862562854777L;
    private SimpleCampaign campaign;

    public CampaignRelatedLeadView() {
        super();
        setCaption(AppContext.getMessage(LeadI18nEnum.M_TITLE_RELATED_LEADS));
        this.itemList = new LeadListDisplay();
        this.setContent(itemList);
    }

    private void loadLeads() {
        final LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        searchCriteria.setCampaignId(new NumberSearchField(SearchField.AND, this.campaign.getId()));
        this.itemList.setSearchCriteria(searchCriteria);
    }

    public void displayLeads(SimpleCampaign campaign) {
        this.campaign = campaign;
        loadLeads();
    }

    @Override
    public void refresh() {
        loadLeads();
    }

    @Override
    protected Component createRightComponent() {
        final NavigationBarQuickMenu addLead = new NavigationBarQuickMenu();
        addLead.setStyleName("add-btn");

        MVerticalLayout addBtns = new MVerticalLayout().withWidth("100%");

        Button newLead = new Button(AppContext.getMessage(LeadI18nEnum.VIEW_NEW_TITLE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                fireNewRelatedItem("");
            }
        });
        addBtns.addComponent(newLead);

        Button selectLead = new Button(AppContext.getMessage(LeadI18nEnum.M_TITLE_SELECT_LEADS), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                CampaignLeadSelectionView leadSelectionView = new CampaignLeadSelectionView(CampaignRelatedLeadView.this);
                LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                leadSelectionView.setSearchCriteria(criteria);
                EventBusFactory.getInstance().post(new ShellEvent.PushView(CampaignRelatedLeadView.this, leadSelectionView));
            }
        });
        addBtns.addComponent(selectLead);
        addLead.setContent(addBtns);
        return addLead;
    }
}
