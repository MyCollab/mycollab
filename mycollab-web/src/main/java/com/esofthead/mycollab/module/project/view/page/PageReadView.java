package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.IPreviewView;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public interface PageReadView extends IPreviewView<Page> {
	HasPreviewFormHandlers<Page> getPreviewFormHandlers();
}
