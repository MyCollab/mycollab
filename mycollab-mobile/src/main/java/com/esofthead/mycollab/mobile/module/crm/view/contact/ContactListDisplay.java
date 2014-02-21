package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ContactListDisplay extends DefaultPagedBeanList<ContactService, ContactSearchCriteria, SimpleContact> {
	private static final long serialVersionUID = -2234454107835680053L;

	public ContactListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(ContactService.class),
				SimpleContact.class, displayColumnId);

		addGeneratedColumn("contactName", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					final Object columnId) {
				final SimpleContact contact = ContactListDisplay.this
						.getBeanByIndex(itemId);
				final Button b = new Button(contact.getContactName(),
						new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						fireTableEvent(new TableClickEvent(
								ContactListDisplay.this, contact,
								"contactName"));
					}
				});
				b.setWidth("100%");
				return b;
			}
		});
	}
}
