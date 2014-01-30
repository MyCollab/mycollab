/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.view.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class CampaignReadViewImpl extends AbstractPageView implements
		CampaignReadView {

	private static final long serialVersionUID = 1L;
	private CampaignReadComp campaignPreview;

	public CampaignReadViewImpl() {
		super();
		campaignPreview = new CampaignReadComp();
		this.addComponent(campaignPreview);
	}

	@Override
	public void previewItem(SimpleCampaign campaign) {
		campaignPreview.previewItem(campaign);
	}

	@Override
	public HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers() {
		return campaignPreview.getPreviewForm();
	}

	@Override
	public SimpleCampaign getItem() {
		return campaignPreview.getCampaign();
	}

	@Override
	public IRelatedListHandlers getRelatedActivityHandlers() {
		return campaignPreview.getAssociateActivityList();
	}

	@Override
	public IRelatedListHandlers<SimpleAccount> getRelatedAccountHandlers() {
		return campaignPreview.getAssociateAccountList();
	}

	@Override
	public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
		return campaignPreview.getAssociateContactList();
	}

	@Override
	public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
		return campaignPreview.getAssociateLeadList();
	}
}
