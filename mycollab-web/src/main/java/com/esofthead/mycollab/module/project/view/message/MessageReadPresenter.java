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

package com.esofthead.mycollab.module.project.view.message;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.events.MessageEvent;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectGenericPresenter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.PreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.ComponentContainer;

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
        view.getPreviewFormHandlers().addFormHandler(new PreviewFormHandler<SimpleMessage>() {

            @Override
            public void onEdit(SimpleMessage data) {
            }

            @Override
            public void onDelete(SimpleMessage data) {
            }

            @Override
            public void onClone(SimpleMessage data) {
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new MessageEvent.GotoList(this, null));
            }

            @Override
            public void onAssign(SimpleMessage data) {
            }

            @Override
            public void gotoPrevious(SimpleMessage data) {
            }

            @Override
            public void gotoNext(SimpleMessage data) {
            }

            @Override
            public void onExtraAction(String action, SimpleMessage data) {

            }

            @Override
            public void onAdd(SimpleMessage data) {
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {
            MessageContainer messageContainer = (MessageContainer) container;
            messageContainer.navigateToContainer(ProjectTypeConstants.MESSAGE);
            messageContainer.removeAllComponents();
            messageContainer.addComponent(view.getWidget());

            if (data.getParams() instanceof Integer) {
                MessageService messageService = ApplicationContextUtil.getSpringBean(MessageService.class);
                SimpleMessage message = messageService.findById((Integer) data.getParams(), AppContext.getAccountId());
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
