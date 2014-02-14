package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.IPreviewView;


/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public interface AccountReadView extends IPreviewView<SimpleAccount> {
	HasPreviewFormHandlers<SimpleAccount> getPreviewFormHandlers();
}
