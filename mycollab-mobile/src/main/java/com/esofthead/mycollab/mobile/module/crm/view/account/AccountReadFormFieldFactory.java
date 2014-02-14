package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.mobile.ui.GenericBeanForm;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class AccountReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleAccount> {
	private static final long serialVersionUID = 1L;

	public AccountReadFormFieldFactory(GenericBeanForm<SimpleAccount> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("email")) {
			return new FormViewField(
					attachForm.getBean().getEmail());
		} else if (propertyId.equals("assignuser")) {
			return new FormViewField(attachForm
							.getBean().getAssignUserFullName());
		} else if (propertyId.equals("website")) {
			return new FormViewField(
					attachForm.getBean().getWebsite());
		}

		return null;
	}

}
