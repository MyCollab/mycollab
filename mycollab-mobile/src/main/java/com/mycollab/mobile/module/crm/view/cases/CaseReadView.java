package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public interface CaseReadView extends IPreviewView<SimpleCase> {
    HasPreviewFormHandlers<SimpleCase> getPreviewFormHandlers();
}
