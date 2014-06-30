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

import com.esofthead.mycollab.mobile.ui.AbstractSelectionCustomField;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.data.Property;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
@SuppressWarnings("serial")
public class CampaignSelectionField extends
		AbstractSelectionCustomField<CampaignWithBLOBs> {

	public CampaignSelectionField() {
		super(CampaignSelectionView.class);
	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		Object value = newDataSource.getValue();
		if (value instanceof Integer) {
			setCampaignByVal((Integer) value);

			super.setPropertyDataSource(newDataSource);
		} else {
			super.setPropertyDataSource(newDataSource);
		}
	}

	@Override
	public void setValue(Integer value) {
		this.setCampaignByVal(value);
		super.setValue(value);
	}

	private void setCampaignByVal(Integer campaignId) {
		CampaignService campaignService = ApplicationContextUtil
				.getSpringBean(CampaignService.class);
		SimpleCampaign campaign = campaignService.findById(campaignId,
				AppContext.getAccountId());
		if (campaign != null) {
			setInternalCampaign(campaign);
		}
	}

	private void setInternalCampaign(SimpleCampaign campaign) {
		this.beanItem = campaign;
		navButton.setCaption(beanItem.getCampaignname());
	}

	@Override
	public void fireValueChange(CampaignWithBLOBs data) {
		this.beanItem = data;
		navButton.setCaption(beanItem.getCampaignname());
		setInternalValue(data.getId());
	}

	@Override
	public Class<? extends Integer> getType() {
		return Integer.class;
	}

}
