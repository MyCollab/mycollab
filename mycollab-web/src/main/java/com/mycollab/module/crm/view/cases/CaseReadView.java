package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;
import com.mycollab.vaadin.ui.IRelatedListHandlers;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface CaseReadView extends IPreviewView<SimpleCase> {
    HasPreviewFormHandlers<SimpleCase> getPreviewFormHandlers();

    IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers();

    IRelatedListHandlers<SimpleContact> getRelatedContactHandlers();
}
