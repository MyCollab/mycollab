package com.mycollab.module.project.view.message;

import com.mycollab.module.project.domain.Message;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface MessageListView extends PageView {
    void setCriteria(MessageSearchCriteria criteria);

    HasEditFormHandlers<Message> getEditFormHandlers();
}
