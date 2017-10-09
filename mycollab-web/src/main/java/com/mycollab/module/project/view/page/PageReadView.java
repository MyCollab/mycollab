package com.mycollab.module.project.view.page;

import com.mycollab.module.page.domain.Page;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public interface PageReadView extends IPreviewView<Page> {
    HasPreviewFormHandlers<Page> getPreviewFormHandlers();
}
