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
package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
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
        setCaption(UserUIContext.getMessage(LeadI18nEnum.M_TITLE_RELATED_LEADS));
        this.itemList = new LeadListDisplay();
        this.setContent(itemList);
    }

    private void loadLeads() {
        final LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        searchCriteria.setCampaignId(new NumberSearchField(campaign.getId()));
        this.itemList.search(searchCriteria);
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

        MVerticalLayout addBtns = new MVerticalLayout().withFullWidth();

        Button newLead = new Button(UserUIContext.getMessage(LeadI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""));
        addBtns.addComponent(newLead);

        Button selectLead = new Button(UserUIContext.getMessage(LeadI18nEnum.M_TITLE_SELECT_LEADS), clickEvent -> {
            CampaignLeadSelectionView leadSelectionView = new CampaignLeadSelectionView(CampaignRelatedLeadView.this);
            LeadSearchCriteria criteria = new LeadSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
            leadSelectionView.setSearchCriteria(criteria);
            EventBusFactory.getInstance().post(new ShellEvent.PushView(CampaignRelatedLeadView.this, leadSelectionView));
        });
        addBtns.addComponent(selectLead);
        addLead.setContent(addBtns);
        return addLead;
    }
}
