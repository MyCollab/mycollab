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
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.view.campaign.CampaignListDisplay;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
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
        setCaption(UserUIContext.getMessage(CampaignI18nEnum.M_TITLE_RELATED_CAMPAIGNS));
        this.itemList = new CampaignListDisplay();
        this.setContent(this.itemList);
    }

    private void loadCampaigns() {
        final CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        searchCriteria.setLeadId(new NumberSearchField(lead.getId()));
        this.itemList.search(searchCriteria);
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

        MVerticalLayout addBtns = new MVerticalLayout().withFullWidth();

        Button newCampaignBtn = new Button(UserUIContext.getMessage(CampaignI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""));
        addBtns.addComponent(newCampaignBtn);

        Button selectCampaignBtn = new Button(UserUIContext.getMessage(CampaignI18nEnum.M_TITLE_SELECT_CAMPAIGNS), clickEvent -> {
            final LeadCampaignSelectionView campaignSelectionView = new LeadCampaignSelectionView(LeadRelatedCampaignView.this);
            CampaignSearchCriteria criteria = new CampaignSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
            campaignSelectionView.setSearchCriteria(criteria);
            EventBusFactory.getInstance().post(new ShellEvent.PushView(LeadRelatedCampaignView.this, campaignSelectionView));
        });
        addBtns.addComponent(selectCampaignBtn);
        addCampaign.setContent(addBtns);
        return addCampaign;
    }

}
