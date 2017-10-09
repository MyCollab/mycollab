package com.mycollab.mobile.module.project.view.message;

import com.mycollab.mobile.module.project.view.ProjectListPresenter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class MessageListPresenter extends ProjectListPresenter<MessageListView, MessageSearchCriteria, SimpleMessage> {
    private static final long serialVersionUID = -4299885147378046501L;

    public MessageListPresenter() {
        super(MessageListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {
            super.onGo(container, data);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
