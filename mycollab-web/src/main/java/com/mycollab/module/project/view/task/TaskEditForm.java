/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.task;

import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.core.MyCollabException;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
// TODO
public class TaskEditForm extends AdvancedEditBeanForm<SimpleTask> {

    @Override
    public void setBean(final SimpleTask item) {
        this.setFormLayoutFactory(new FormLayoutFactory());
        this.setBeanFormFieldFactory(new TaskEditFormFieldFactory(this, CurrentProjectVariables.getProjectId()));
        super.setBean(item);
    }

    protected void postExecution() {

    }

    class FormLayoutFactory extends AbstractFormLayoutFactory {
        private IFormLayoutFactory formLayoutFactory;

        @Override
        public AbstractComponent getLayout() {
            VerticalLayout layout = new VerticalLayout();
            formLayoutFactory = new DefaultDynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getForm(),
                    Task.Field.parenttaskid.name());
            AbstractComponent gridLayout = formLayoutFactory.getLayout();
            gridLayout.addStyleName(WebThemes.SCROLLABLE_CONTAINER);
//            new Restrain(gridLayout).setMaxHeight((UIUtils.getBrowserHeight() - 180) + "px");
            layout.addComponent(gridLayout);
            layout.setExpandRatio(gridLayout, 1.0f);

            MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                if (validateForm()) {
                    ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                    Integer taskId;
                    if (bean.getId() == null) {
                        taskId = taskService.saveWithSession(bean, UserUIContext.getUsername());
                    } else {
                        taskService.updateWithSession(bean, UserUIContext.getUsername());
                        taskId = bean.getId();
                    }

                    TaskEditFormFieldFactory taskEditFormFieldFactory = (TaskEditFormFieldFactory) fieldFactory;

                    AttachmentUploadField uploadField = taskEditFormFieldFactory.getAttachmentUploadField();
                    String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(), bean.getProjectid(),
                            ProjectTypeConstants.TASK, "" + taskId);
                    uploadField.saveContentsToRepo(attachPath);

                    ProjectSubscribersComp subscribersComp = taskEditFormFieldFactory.getSubscribersComp();
                    List<String> followers = subscribersComp.getFollowers();
                    if (followers.size() > 0) {
                        List<MonitorItem> monitorItems = new ArrayList<>();
                        for (String follower : followers) {
                            MonitorItem monitorItem = new MonitorItem();
                            monitorItem.setMonitorDate(LocalDateTime.now());
                            monitorItem.setSaccountid(AppUI.getAccountId());
                            monitorItem.setType(ProjectTypeConstants.TASK);
                            monitorItem.setTypeid(taskId);
                            monitorItem.setUser(follower);
                            monitorItem.setExtratypeid(bean.getProjectid());
                            monitorItems.add(monitorItem);
                        }
                        MonitorItemService monitorItemService = AppContextUtil.getSpringBean(MonitorItemService.class);
                        monitorItemService.saveMonitorItems(monitorItems);
                    }

                    postExecution();
                    EventBusFactory.getInstance().post(new TaskEvent.NewTaskAdded(TaskEditForm.this, taskId));
                    EventBusFactory.getInstance().post(new TicketEvent.NewTicketAdded(TaskEditForm.this,
                            ProjectTypeConstants.TASK, taskId));
                }
            }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.CLIPBOARD);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> postExecution())
                    .withStyleName(WebThemes.BUTTON_OPTION);

            MHorizontalLayout buttonControls = new MHorizontalLayout(cancelBtn, saveBtn)
                    .withMargin(new MarginInfo(true, true, true, false)).alignAll(Alignment.MIDDLE_RIGHT);

            layout.addComponent(buttonControls);
            layout.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);
            return layout;
        }

        @Override
        protected HasValue<?> onAttachField(Object propertyId, HasValue<?> field) {
            try {
                return formLayoutFactory.attachField(propertyId, field);
            } catch (Exception e) {
                throw new MyCollabException("Exception " + propertyId);
            }
        }
    }
}
