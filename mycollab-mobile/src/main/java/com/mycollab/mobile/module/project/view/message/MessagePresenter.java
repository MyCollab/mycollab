package com.mycollab.mobile.module.project.view.message;

import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.mobile.module.project.view.parameters.MessageScreenData;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.mobile.mvp.view.PresenterOptionUtil;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class MessagePresenter extends AbstractPresenter<MessageContainer> {
    private static final long serialVersionUID = 2423914044838645060L;

    public MessagePresenter() {
        super(MessageContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {
            if (data instanceof MessageScreenData.Read) {
                MessageReadPresenter presenter = PresenterResolver.getPresenter(MessageReadPresenter.class);
                presenter.go(container, data);
            } else if (data instanceof MessageScreenData.Search) {
                MessageListPresenter presenter = PresenterResolver.getPresenter(MessageListPresenter.class);
                presenter.go(container, data);
            } else if (data instanceof MessageScreenData.Add) {
                IPresenter presenter = PresenterOptionUtil.getPresenter(IMessageAddPresenter.class);
                presenter.go(container, data);
            } else if (data == null) {
                MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
                searchCriteria.setProjectids(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                MessageListPresenter presenter = PresenterResolver.getPresenter(MessageListPresenter.class);
                presenter.go(container, new ScreenData.Preview<>(searchCriteria));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
