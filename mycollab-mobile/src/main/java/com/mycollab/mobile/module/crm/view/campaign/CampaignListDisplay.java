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

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.CampaignEvent;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class CampaignListDisplay extends DefaultPagedBeanList<CampaignService, CampaignSearchCriteria, SimpleCampaign> {
    private static final long serialVersionUID = -2234454107835680053L;

    public CampaignListDisplay() {
        super(AppContextUtil.getSpringBean(CampaignService.class), new CampaignRowDisplayHandler());
    }

    static public class CampaignRowDisplayHandler implements RowDisplayHandler<SimpleCampaign> {

        @Override
        public Component generateRow(final SimpleCampaign campaign, int rowIndex) {
            final Button b = new Button(campaign.getCampaignname(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new CampaignEvent.GotoRead(this, campaign.getId()));
                }
            });
            b.setWidth("100%");
            return b;
        }

    }
}
