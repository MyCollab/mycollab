package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public interface AccountReadView extends IPreviewView<SimpleAccount> {
    HasPreviewFormHandlers<SimpleAccount> getPreviewFormHandlers();
}
