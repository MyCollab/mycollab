package com.mycollab.mobile.ui;

import com.mycollab.vaadin.event.HasPreviewFormHandlers;

/**
 * MyCollab Ltd
 *
 * @param <B>
 * @since 1.0
 */
public interface PreviewBeanForm<B> extends HasPreviewFormHandlers<B> {

    B getBean();

    void setBean(B bean);

    void fireAssignForm(B bean);

    void fireEditForm(B bean);

    void fireCancelForm(B bean);

    void fireDeleteForm(B bean);

    void fireCloneForm(B bean);

    void fireExtraAction(String action, B bean);
}
