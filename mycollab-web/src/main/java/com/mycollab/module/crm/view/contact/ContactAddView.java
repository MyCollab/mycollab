package com.mycollab.module.crm.view.contact;

import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface ContactAddView extends IFormAddView<SimpleContact> {

    HasEditFormHandlers<SimpleContact> getEditFormHandlers();
}
