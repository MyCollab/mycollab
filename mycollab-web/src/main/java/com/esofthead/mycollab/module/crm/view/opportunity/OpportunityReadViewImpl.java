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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class OpportunityReadViewImpl extends AbstractPageView implements
		OpportunityReadView {

	private static final long serialVersionUID = 1L;

	private OpportunityReadComp opportunityPreview;

	public OpportunityReadViewImpl() {
		super();
		opportunityPreview = new OpportunityReadComp();
		this.addComponent(opportunityPreview);
	}

	@Override
	public void previewItem(SimpleOpportunity item) {
		opportunityPreview.previewItem(item);
	}

	@Override
	public HasPreviewFormHandlers<SimpleOpportunity> getPreviewFormHandlers() {
		return opportunityPreview.getPreviewForm();
	}

	@Override
	public SimpleOpportunity getItem() {
		return opportunityPreview.getOpportunity();
	}

	@Override
	public IRelatedListHandlers getRelatedActivityHandlers() {
		return opportunityPreview.getAssociateActivityList();
	}

	@Override
	public IRelatedListHandlers<SimpleContactOpportunityRel> getRelatedContactHandlers() {
		return opportunityPreview.getAssociateContactList();
	}

	@Override
	public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
		return opportunityPreview.getAssociateLeadList();
	}
}
