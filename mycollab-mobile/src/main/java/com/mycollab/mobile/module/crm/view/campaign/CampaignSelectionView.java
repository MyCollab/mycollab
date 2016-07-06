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

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignSelectionView extends AbstractSelectionView<CampaignWithBLOBs> {
    private static final long serialVersionUID = 1L;

    private CampaignListDisplay itemList;

    private CampaignRowDisplayHandler rowHandler = new CampaignRowDisplayHandler();

    public CampaignSelectionView() {
        super();
        createUI();
        this.setCaption(AppContext.getMessage(CampaignI18nEnum.M_VIEW_CAMPAIGN_NAME_LOOKUP));
    }

    private void createUI() {
        itemList = new CampaignListDisplay();
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        itemList.search(searchCriteria);
        SimpleCampaign clearCampaign = new SimpleCampaign();
        itemList.getListContainer().addComponentAsFirst(rowHandler.generateRow(clearCampaign, 0));
    }

    private class CampaignRowDisplayHandler implements RowDisplayHandler<SimpleCampaign> {

        @Override
        public Component generateRow(final SimpleCampaign campaign, int rowIndex) {
            return new Button(campaign.getCampaignname(), new Button.ClickListener() {
                private static final long serialVersionUID = -2772809360324017746L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    selectionField.fireValueChange(campaign);
                    CampaignSelectionView.this.getNavigationManager().navigateBack();
                }
            });
        }

    }
}
