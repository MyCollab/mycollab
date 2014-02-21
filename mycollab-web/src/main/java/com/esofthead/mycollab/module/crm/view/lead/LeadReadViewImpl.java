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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class LeadReadViewImpl extends AbstractPageView implements LeadReadView {
	private static final long serialVersionUID = 1L;

	private LeadReadComp leadPreview;

	public LeadReadViewImpl() {
		super();
		leadPreview = new LeadReadComp();
		this.addComponent(leadPreview);
	}

	@Override
	public void previewItem(SimpleLead lead) {
		leadPreview.previewItem(lead);
	}

	@Override
	public HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers() {
		return leadPreview.getPreviewForm();
	}

	@Override
	public SimpleLead getItem() {
		return leadPreview.getBeanItem();
	}

	@Override
	public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
		return leadPreview.getAssociateActivityList();
	}

	@Override
	public IRelatedListHandlers<SimpleCampaign> getRelatedCampaignHandlers() {
		return leadPreview.getAssociateCampaignList();
	}
}
