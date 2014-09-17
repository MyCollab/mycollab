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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

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

	public OpportunityListDisplay() {
		super(ApplicationContextUtil.getSpringBean(OpportunityService.class),
				new OpportunityRowDisplayHandler());
	}

	static public class OpportunityRowDisplayHandler implements
			RowDisplayHandler<SimpleOpportunity> {

		@Override
		public Component generateRow(final SimpleOpportunity opportunity,
				int rowIndex) {
			final Button b = new Button(opportunity.getOpportunityname());
			b.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					EventBusFactory.getInstance().post(
							new OpportunityEvent.GotoRead(this, opportunity
									.getId()));
				}
			});
			b.setWidth("100%");
			b.addStyleName("list-item");
			return b;
		}

	}

}
