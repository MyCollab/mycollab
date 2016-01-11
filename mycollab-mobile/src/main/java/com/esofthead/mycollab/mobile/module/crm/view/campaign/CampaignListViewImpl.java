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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */

@ViewComponent
public class CampaignListViewImpl extends
		AbstractListViewComp<CampaignSearchCriteria, SimpleCampaign> implements
		CampaignListView {
	private static final long serialVersionUID = -8743010493576179868L;

	public CampaignListViewImpl() {
		super();

		setCaption(AppContext.getMessage(CampaignI18nEnum.VIEW_LIST_TITLE));
	}

	@Override
	protected AbstractPagedBeanList<CampaignSearchCriteria, SimpleCampaign> createBeanTable() {
		CampaignListDisplay campaignListDisplay = new CampaignListDisplay();
		return campaignListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		Button addCampaign = new Button();
		addCampaign.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new CampaignEvent.GotoAdd(this, null));
			}
		});
		addCampaign.setStyleName("add-btn");
		return addCampaign;
	}

}
