/**
 * 
 */
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class CampaignLeadSelectionView extends
		AbstractRelatedItemSelectionView<SimpleLead, LeadSearchCriteria> {

	private static final long serialVersionUID = -7266079544811933378L;

	public CampaignLeadSelectionView(
			AbstractRelatedListView<SimpleLead, LeadSearchCriteria> relatedListView) {
		super("Select Leads", relatedListView);
	}

	@Override
	protected void initUI() {
		this.itemList = new LeadListDisplay();
		this.itemList
				.setRowDisplayHandler(new AbstractPagedBeanList.RowDisplayHandler<SimpleLead>() {

					@Override
					public Component generateRow(final SimpleLead obj,
							int rowIndex) {
						final SelectableButton b = new SelectableButton(obj
								.getLeadName());
						if (selections.contains(obj))
							b.select();
						b.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 7154832577113973331L;

							@Override
							public void buttonClick(ClickEvent event) {
								if (b.isSelected())
									selections.add(obj);
								else
									selections.remove(obj);

							}
						});
						return null;
					}
				});

	}

}
