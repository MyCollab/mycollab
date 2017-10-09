package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public interface LeadReadView extends IPreviewView<SimpleLead> {
    HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers();
}
