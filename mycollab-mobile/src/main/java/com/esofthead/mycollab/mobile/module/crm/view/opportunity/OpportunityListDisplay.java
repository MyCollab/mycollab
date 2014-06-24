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
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class OpportunityListDisplay
		extends
		DefaultPagedBeanList<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
	private static final long serialVersionUID = -2350731660593521985L;

	public OpportunityListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(OpportunityService.class),
				SimpleOpportunity.class, displayColumnId);

		addGeneratedColumn("opportunityname", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleOpportunity opportunity = OpportunityListDisplay.this
						.getBeanByIndex(itemId);

				final NavigationButton b = new NavigationButton(opportunity
						.getOpportunityname());
				b.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							final NavigationButton.NavigationButtonClickEvent event) {
						fireTableEvent(new TableClickEvent(
								OpportunityListDisplay.this, opportunity,
								"opportunityname"));
					}
				});
				b.setWidth("100%");
				return b;
			}
		});
	}

}
