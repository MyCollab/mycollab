package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public interface ContactReadView extends IPreviewView<SimpleContact> {

    HasPreviewFormHandlers<SimpleContact> getPreviewFormHandlers();
}
