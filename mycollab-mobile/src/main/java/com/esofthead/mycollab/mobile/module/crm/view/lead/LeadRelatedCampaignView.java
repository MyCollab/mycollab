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
package com.esofthead.mycollab.mobile.module.crm.view.lead;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignListDisplay;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class LeadRelatedCampaignView extends AbstractRelatedListView<SimpleCampaign, CampaignSearchCriteria> {
    private static final long serialVersionUID = 3836477709565175561L;
    private SimpleLead lead;

    public LeadRelatedCampaignView() {
        super();
        setCaption(AppContext.getMessage(CampaignI18nEnum.M_TITLE_RELATED_CAMPAIGNS));
        this.itemList = new CampaignListDisplay();
        this.setContent(this.itemList);
    }

    private void loadCampaigns() {
        final CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        searchCriteria.setLeadId(new NumberSearchField(SearchField.AND, this.lead.getId()));
        this.itemList.setSearchCriteria(searchCriteria);
    }

    public void displayCampaign(SimpleLead lead) {
        this.lead = lead;
        loadCampaigns();
    }

    @Override
    public void refresh() {
        loadCampaigns();
    }

    @Override
    protected Component createRightComponent() {
        NavigationBarQuickMenu addCampaign = new NavigationBarQuickMenu();
        addCampaign.setStyleName("add-btn");

        MVerticalLayout addBtns = new MVerticalLayout().withWidth("100%");

        Button newCampaignBtn = new Button(AppContext.getMessage(CampaignI18nEnum.VIEW_NEW_TITLE), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                fireNewRelatedItem("");
            }
        });
        addBtns.addComponent(newCampaignBtn);

        Button selectCampaignBtn = new Button(AppContext.getMessage(CampaignI18nEnum.M_TITLE_SELECT_CAMPAIGNS), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                final LeadCampaignSelectionView campaignSelectionView = new LeadCampaignSelectionView(LeadRelatedCampaignView.this);
                CampaignSearchCriteria criteria = new CampaignSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                campaignSelectionView.setSearchCriteria(criteria);
                EventBusFactory.getInstance().post(new ShellEvent.PushView(LeadRelatedCampaignView.this, campaignSelectionView));
            }
        });
        addBtns.addComponent(selectCampaignBtn);
        addCampaign.setContent(addBtns);
        return addCampaign;
    }

}
