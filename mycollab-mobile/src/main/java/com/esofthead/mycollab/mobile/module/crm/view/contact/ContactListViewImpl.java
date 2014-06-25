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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.MobileNavigationButton;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;

/**
 * 
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */

@ViewComponent
public class ContactListViewImpl extends
		AbstractListViewComp<ContactSearchCriteria, SimpleContact> implements
		ContactListView {
	private static final long serialVersionUID = 8271856163726726780L;

	public ContactListViewImpl() {
		super();

		setCaption(AppContext.getMessage(ContactI18nEnum.VIEW_LIST_TITLE));
	}

	@Override
	protected AbstractPagedBeanList<ContactSearchCriteria, SimpleContact> createBeanTable() {
		ContactListDisplay contactListDisplay = new ContactListDisplay(
				"contactName");
		contactListDisplay
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = 2099605014707508671L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(TableClickEvent event) {
						SimpleContact simpleContact = (SimpleContact) event
								.getData();
						if ("contactName".equals(event.getFieldName())) {
							EventBus.getInstance().fireEvent(
									new ContactEvent.GotoRead(
											ContactListViewImpl.this,
											simpleContact.getId()));
						}
					}
				});
		return contactListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		MobileNavigationButton addContact = new MobileNavigationButton();
		addContact.setTargetViewCaption(AppContext
				.getMessage(ContactI18nEnum.VIEW_NEW_TITLE));
		addContact
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent arg0) {
						EventBus.getInstance().fireEvent(
								new ContactEvent.GotoAdd(this, null));
					}
				});
		addContact.setStyleName("add-btn");
		return addContact;
	}
}
