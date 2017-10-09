package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.module.crm.domain.SimpleCall;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public interface CallReadView extends IPreviewView<SimpleCall> {
	HasPreviewFormHandlers<SimpleCall> getPreviewFormHandlers();
}
