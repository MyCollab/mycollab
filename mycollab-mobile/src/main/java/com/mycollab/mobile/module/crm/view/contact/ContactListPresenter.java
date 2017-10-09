package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactListPresenter extends CrmListPresenter<ContactListView, ContactSearchCriteria, SimpleContact> {
    private static final long serialVersionUID = -8607598374388692503L;

    public ContactListPresenter() {
        super(ContactListView.class);
    }
}
