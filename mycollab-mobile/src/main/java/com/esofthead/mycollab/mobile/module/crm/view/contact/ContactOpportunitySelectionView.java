/**
 * 
 */
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class ContactOpportunitySelectionView
		extends
		AbstractRelatedItemSelectionView<SimpleOpportunity, OpportunitySearchCriteria> {

	private static final long serialVersionUID = -238551162632570679L;

	public ContactOpportunitySelectionView(
			AbstractRelatedListView<SimpleOpportunity, OpportunitySearchCriteria> relatedListView) {
		super("Select Opportunities", relatedListView);
	}

	@Override
	protected void initUI() {
		this.itemList = new OpportunityListDisplay();
		this.itemList
				.setRowDisplayHandler(new AbstractPagedBeanList.RowDisplayHandler<SimpleOpportunity>() {

					@Override
					public Component generateRow(final SimpleOpportunity obj,
							int rowIndex) {
						final SelectableButton b = new SelectableButton(obj
								.getOpportunityname());
						if (selections.contains(obj))
							b.select();
						b.addClickListener(new Button.ClickListener() {

							private static final long serialVersionUID = 2458940518722524446L;

							@Override
							public void buttonClick(ClickEvent event) {
								if (b.isSelected())
									selections.add(obj);
								else
									selections.remove(obj);
							}

						});
						return b;
					}
				});
	}

}
