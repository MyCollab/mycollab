package com.mycollab.module.crm.view.account;

import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface AccountAddView extends IFormAddView<SimpleAccount> {
    HasEditFormHandlers<SimpleAccount> getEditFormHandlers();
}
