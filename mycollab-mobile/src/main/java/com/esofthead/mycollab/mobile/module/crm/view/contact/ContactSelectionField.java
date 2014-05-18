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

import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.data.Property;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class ContactSelectionField extends CustomField<Integer> implements
		FieldSelection<SimpleContact> {
	private static final long serialVersionUID = 1L;

	private HorizontalLayout layout;

	private TextField contactName;

	private SimpleContact contact;

	private Image browseBtn;
	private Image clearBtn;

	public ContactSelectionField() {
		contactName = new TextField();
		contactName.setNullRepresentation("");
		contactName.setWidth("100%");
		browseBtn = new Image(null,
				MyCollabResource.newResource("icons/16/browseItem.png"));
		browseBtn.addClickListener(new MouseEvents.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void click(ClickEvent event) {
				ContactSelectionView contactView = new ContactSelectionView(
						ContactSelectionField.this);
				EventBus.getInstance().fireEvent(
						new CrmEvent.PushView(ContactSelectionField.this,
								contactView));
			}
		});

		clearBtn = new Image(null,
				MyCollabResource.newResource("icons/16/clearItem.png"));

		clearBtn.addClickListener(new MouseEvents.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void click(ClickEvent event) {
				contactName.setValue("");
				contact = null;
			}
		});
	}

	@Override
	public void fireValueChange(SimpleContact data) {
		contact = data;
		if (contact != null) {
			contactName.setValue(contact.getContactName());
			setInternalValue(contact.getId());
		}

	}

	@Override
	public void setPropertyDataSource(Property newDataSource) {
		final Object value = newDataSource.getValue();
		if (value instanceof Integer) {
			setContactByVal((Integer) value);
			super.setPropertyDataSource(newDataSource);
		} else {
			super.setPropertyDataSource(newDataSource);
		}
		/*
		 * else if (value instanceof SimpleContact) {
		 * setInternalContact((SimpleContact) value);
		 * super.setPropertyDataSource(new AbstractField<Integer>() { private
		 * static final long serialVersionUID = 1L;
		 * 
		 * @Override public Integer getValue() { return ((SimpleContact)
		 * value).getId(); }
		 * 
		 * @Override public Class<? extends Integer> getType() { return
		 * Integer.class; } }); } else { throw new MyCollabException(
		 * "Do not support property source different than int or SimpleContact"
		 * ); }
		 */
	}

	@Override
	public void setValue(Integer value) {
		this.setContactByVal(value);
		super.setValue(value);
	}

	private void setContactByVal(Integer contactId) {
		ContactService contactService = ApplicationContextUtil
				.getSpringBean(ContactService.class);
		SimpleContact contactVal = contactService.findById(contactId,
				AppContext.getAccountId());
		if (contactVal != null) {
			setInternalContact(contactVal);
		}
	}

	private void setInternalContact(SimpleContact contact) {
		this.contact = contact;
		contactName.setValue(contact.getContactName());
	}

	public SimpleContact getContact() {
		return this.contact;
	}

	@Override
	protected Component initContent() {
		layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setWidth("100%");

		layout.addComponent(contactName);
		layout.setComponentAlignment(contactName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(contactName, 1.0f);

		layout.addComponent(browseBtn);
		layout.setComponentAlignment(browseBtn, Alignment.MIDDLE_LEFT);

		layout.addComponent(clearBtn);
		layout.setComponentAlignment(clearBtn, Alignment.MIDDLE_LEFT);

		return layout;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
}
