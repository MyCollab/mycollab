package com.mycollab.module.project.view.message;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.module.project.event.MessageEvent;
import com.mycollab.module.project.service.MessageService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class MessageReadPresenter extends ProjectGenericPresenter<MessageReadView> {
    private static final long serialVersionUID = 1L;

    public MessageReadPresenter() {
        super(MessageReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleMessage>() {

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {
            MessageContainer messageContainer = (MessageContainer) container;
            messageContainer.navigateToContainer(ProjectTypeConstants.MESSAGE);
            messageContainer.setContent(view);

            if (data.getParams() instanceof Integer) {
                MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                SimpleMessage message = messageService.findById((Integer) data.getParams(), AppUI.getAccountId());
                view.previewItem(message);

                ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                breadCrumb.gotoMessage(message);
            } else {
                throw new MyCollabException("Unhanddle this case yet");
            }
        } else {
            throw new SecureAccessException();
        }
    }
}
