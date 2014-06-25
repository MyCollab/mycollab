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
package com.esofthead.mycollab.mobile.module.crm.view.lead;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.MobileNavigationButton;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */

@ViewComponent
public class LeadListViewImpl extends
		AbstractListViewComp<LeadSearchCriteria, SimpleLead> implements
		LeadListView {
	private static final long serialVersionUID = -5311139413938817084L;

	public LeadListViewImpl() {
		super();

		setCaption(AppContext.getMessage(LeadI18nEnum.VIEW_LIST_TITLE));
	}

	@Override
	protected AbstractPagedBeanList<LeadSearchCriteria, SimpleLead> createBeanTable() {
		LeadListDisplay leadListDisplay = new LeadListDisplay("leadName");
		leadListDisplay
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = 9195179759480776147L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(TableClickEvent event) {
						final SimpleLead lead = (SimpleLead) event.getData();
						if ("leadName".equals(event.getFieldName())) {
							EventBus.getInstance()
									.fireEvent(
											new LeadEvent.GotoRead(
													LeadListViewImpl.this, lead
															.getId()));
						}

					}
				});
		return leadListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		MobileNavigationButton addLead = new MobileNavigationButton();
		addLead.setTargetViewCaption(AppContext
				.getMessage(LeadI18nEnum.VIEW_NEW_TITLE));
		addLead.addClickListener(new NavigationButton.NavigationButtonClickListener() {
			private static final long serialVersionUID = -6024437571619598638L;

			@Override
			public void buttonClick(
					NavigationButton.NavigationButtonClickEvent event) {
				EventBus.getInstance().fireEvent(
						new LeadEvent.GotoAdd(this, null));
			}
		});
		addLead.setStyleName("add-btn");
		return addLead;
	}

}
