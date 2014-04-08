package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.IFormAddView;

public interface AccountAddView extends IFormAddView<SimpleAccount> {

	@Override
	HasEditFormHandlers<SimpleAccount> getEditFormHandlers();

}
