/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.message;

import com.mycollab.core.SecureAccessException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Message;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.module.project.service.MessageService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultEditFormHandler;
import com.mycollab.vaadin.mvp.*;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class MessageListPresenter extends ProjectGenericPresenter<MessageListView> implements ListCommand<MessageSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private MessageSearchCriteria searchCriteria;

    public MessageListPresenter() {
        super(MessageListView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<Message>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(Message message) {
                MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                messageService.saveWithSession(message, UserUIContext.getUsername());
                doSearch(searchCriteria);
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {
            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoMessageList();

            MessageContainer messageContainer = (MessageContainer) container;
            messageContainer.navigateToContainer(ProjectTypeConstants.MESSAGE);
            messageContainer.setContent(view);
            doSearch((MessageSearchCriteria) data.getParams());
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    public void doSearch(MessageSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        view.setCriteria(searchCriteria);
    }
}
