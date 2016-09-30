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
package com.mycollab.module.project.view.task;

import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.components.ProjectSubscribersComp;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
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
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout layout = new VerticalLayout();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 7);
            layout.addComponent(informationLayout.getLayout());

            MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
            buttonControls.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

            MButton updateAllBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_UPDATE_OTHER_FIELDS), clickEvent -> {
                EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(TaskEditForm.this, bean));
                postExecution();
            }).withStyleName(WebUIConstants.BUTTON_LINK);

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
                    String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(), bean.getProjectid(),
                            ProjectTypeConstants.TASK, "" + taskId);
                    uploadField.saveContentsToRepo(attachPath);

                    ProjectSubscribersComp subcribersComp = taskEditFormFieldFactory.getSubscribersComp();
                    List<String> followers = subcribersComp.getFollowers();
                    if (followers.size() > 0) {
                        List<MonitorItem> monitorItems = new ArrayList<>();
                        for (String follower : followers) {
                            MonitorItem monitorItem = new MonitorItem();
                            monitorItem.setMonitorDate(new GregorianCalendar().getTime());
                            monitorItem.setSaccountid(MyCollabUI.getAccountId());
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
            }).withStyleName(WebUIConstants.BUTTON_ACTION).withIcon(FontAwesome.SAVE);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> postExecution())
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            buttonControls.with(updateAllBtn, cancelBtn, saveBtn);

            layout.addComponent(buttonControls);
            layout.setComponentAlignment(buttonControls, Alignment.MIDDLE_RIGHT);
            layout.addStyleName(WebUIConstants.SCROLLABLE_CONTAINER);
            new Restrain(layout).setMaxHeight("600px");
            return layout;
        }

        @Override
        protected Component onAttachField(Object propertyId, Field<?> field) {
            if (Task.Field.name.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0, 2, "100%");
            } else if (Task.Field.startdate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE), 0, 1);
            } else if (Task.Field.enddate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE), 1, 1);
            } else if (Task.Field.duedate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE), 0, 2);
            } else if (Task.Field.assignuser.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 2);
            } else if (Task.Field.priority.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY),
                        UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY_HELP), 0, 3);
            } else if (Task.Field.milestoneid.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), 1, 3);
            } else if (Task.Field.description.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 4, 2, "100%");
            } else if (Task.Field.id.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS), 0, 5, 2, "100%");
            } else if (SimpleTask.Field.selected.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(FollowerI18nEnum.OPT_SUB_INFO_WATCHERS),
                        UserUIContext.getMessage(FollowerI18nEnum.FOLLOWER_EXPLAIN_HELP), 0, 6, 2, "100%");
            }
            return null;
        }
    }
}
