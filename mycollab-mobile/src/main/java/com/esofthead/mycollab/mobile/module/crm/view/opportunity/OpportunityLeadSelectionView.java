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
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class OpportunityLeadSelectionView extends
		AbstractRelatedItemSelectionView<SimpleLead, LeadSearchCriteria> {

	private static final long serialVersionUID = 10923261543446355L;

	public OpportunityLeadSelectionView(
			AbstractRelatedListView<SimpleLead, LeadSearchCriteria> relatedListView) {
		super(AppContext.getMessage(LeadI18nEnum.M_TITLE_SELECT_LEADS),
				relatedListView);
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

							private static final long serialVersionUID = -9086838156625853470L;

							@Override
							public void buttonClick(Button.ClickEvent event) {
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
