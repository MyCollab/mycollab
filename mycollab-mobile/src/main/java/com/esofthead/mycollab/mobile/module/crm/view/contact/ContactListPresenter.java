package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.mobile.ui.ListPresenter;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;

/**
 * 
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */

public class ContactListPresenter extends
		ListPresenter<ContactListView, ContactSearchCriteria, SimpleContact> {
	private static final long serialVersionUID = -8607598374388692503L;

	public ContactListPresenter() {
		super(ContactListView.class);
	}
}
