package com.mycollab.mobile.module.project.view.message;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.module.project.i18n.MessageI18nEnum;
import com.mycollab.module.project.service.MessageService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class MessageReadPresenter extends AbstractProjectPresenter<MessageReadView> {
    private static final long serialVersionUID = 334720221360960772L;

    public MessageReadPresenter() {
        super(MessageReadView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {
            if (data.getParams() instanceof Integer) {
                MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                SimpleMessage message = messageService.findById((Integer) data.getParams(), AppUI.getAccountId());
                getView().previewItem(message);
                super.onGo(container, data);

                AppUI.addFragment(ProjectLinkGenerator.generateMessagePreviewLink(CurrentProjectVariables.getProjectId(), message.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                                UserUIContext.getMessage(MessageI18nEnum.SINGLE), message.getTitle()));
            } else {
                throw new MyCollabException("Unhanddle this case yet");
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
