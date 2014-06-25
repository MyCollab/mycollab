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
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.CaseEvent;
import com.esofthead.mycollab.mobile.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.MobileNavigationButton;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */

@ViewComponent
public class CaseListViewImpl extends
		AbstractListViewComp<CaseSearchCriteria, SimpleCase> implements
		CaseListView {
	private static final long serialVersionUID = -2790165346072368795L;

	public CaseListViewImpl() {
		super();

		setCaption(AppContext.getMessage(CaseI18nEnum.VIEW_NEW_TITLE));
	}

	@Override
	protected AbstractPagedBeanList<CaseSearchCriteria, SimpleCase> createBeanTable() {
		CaseListDisplay caseListDisplay = new CaseListDisplay("subject");
		caseListDisplay
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = -8696917338193717521L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(TableClickEvent event) {
						final SimpleCase myCase = (SimpleCase) event.getData();
						if ("subject".equals(event.getFieldName())) {
							EventBus.getInstance().fireEvent(
									new CaseEvent.GotoRead(
											CaseListViewImpl.this, myCase
													.getId()));
						}
					}
				});
		return caseListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		MobileNavigationButton addCase = new MobileNavigationButton();
		addCase.setTargetViewCaption(AppContext
				.getMessage(CaseI18nEnum.VIEW_NEW_TITLE));
		addCase.addClickListener(new NavigationButton.NavigationButtonClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(
					NavigationButton.NavigationButtonClickEvent arg0) {
				EventBus.getInstance().fireEvent(
						new CaseEvent.GotoAdd(this, null));
			}
		});
		addCase.setStyleName("add-btn");
		return addCase;
	}

}
