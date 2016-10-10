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
/**
 *
 */
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.mycollab.mobile.module.crm.view.campaign.CampaignListDisplay;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class LeadCampaignSelectionView extends AbstractRelatedItemSelectionView<SimpleCampaign, CampaignSearchCriteria> {
    private static final long serialVersionUID = -3201084210895713763L;

    public LeadCampaignSelectionView(AbstractRelatedListView<SimpleCampaign, CampaignSearchCriteria> relatedListView) {
        super(UserUIContext.getMessage(CampaignI18nEnum.M_TITLE_SELECT_CAMPAIGNS), relatedListView);
    }

    @Override
    protected void initUI() {
        this.itemList = new CampaignListDisplay();
        this.itemList.setRowDisplayHandler((host, campaign, rowIndex) -> {
            final SelectableButton b = new SelectableButton(campaign.getCampaignname());
            if (selections.contains(campaign))
                b.select();
            b.addClickListener(clickEvent -> {
                if (b.isSelected())
                    selections.add(campaign);
                else
                    selections.remove(campaign);
            });
            return b;
        });
    }

}
