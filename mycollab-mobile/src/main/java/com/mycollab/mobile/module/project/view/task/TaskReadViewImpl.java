/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.task;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.project.ui.CommentNavigationButton;
import com.mycollab.mobile.module.project.ui.ProjectAttachmentDisplayComp;
import com.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
@ViewComponent
public class TaskReadViewImpl extends AbstractPreviewItemComp<SimpleTask> implements TaskReadView {
    private static final long serialVersionUID = 9021783098267883004L;

    private Button quickActionStatusBtn;
    private CommentNavigationButton relatedComments;
    private TaskTimeLogComp taskTimeLogComp;
    private ProjectAttachmentDisplayComp attachmentComp;

    @Override
    public HasPreviewFormHandlers<SimpleTask> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    protected void afterPreviewItem() {
        if (StatusI18nEnum.Open.name().equals(beanItem.getStatus())) {
            quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
            this.removeStyleName(MobileUIConstants.STATUS_DISABLED);
        } else {
            quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
            this.addStyleName(MobileUIConstants.STATUS_DISABLED);
        }
        relatedComments.displayTotalComments(beanItem.getId() + "");

        if (!SiteConfiguration.isCommunityEdition()) {
            taskTimeLogComp.displayTime(beanItem);
            previewForm.addComponent(taskTimeLogComp);
        }

        ResourceService resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        List<Content> attachments = resourceService.getContents(AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(),
                beanItem.getProjectid(), ProjectTypeConstants.TASK, "" + beanItem.getId()));
        if (CollectionUtils.isNotEmpty(attachments)) {
            attachmentComp = new ProjectAttachmentDisplayComp(attachments);
            previewForm.addComponent(attachmentComp);
        } else if (attachmentComp != null && attachmentComp.getParent().equals(previewForm)) {
            previewForm.removeComponent(attachmentComp);
        }
    }

    @Override
    protected String initFormTitle() {
        return UserUIContext.getMessage(TaskI18nEnum.SINGLE);
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleTask> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleTask> initBeanFormFieldFactory() {
        return new ReadFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleTask> taskPreviewForm = new ProjectPreviewFormControlsGenerator<>(previewForm);
        final VerticalLayout topPanel = taskPreviewForm.createButtonControls(
                ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED,
                ProjectRolePermissionCollections.TASKS);

        quickActionStatusBtn = new Button("", clickEvent -> {
            if (beanItem.getStatus() != null && beanItem.getStatus().equals(StatusI18nEnum.Closed.name())) {
                beanItem.setStatus(StatusI18nEnum.Open.name());
                beanItem.setPercentagecomplete(0d);
                TaskReadViewImpl.this.removeStyleName(MobileUIConstants.STATUS_DISABLED);
                quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLOSE));
            } else {
                beanItem.setStatus(StatusI18nEnum.Closed.name());
                beanItem.setPercentagecomplete(100d);
                TaskReadViewImpl.this.addStyleName(MobileUIConstants.STATUS_DISABLED);
                quickActionStatusBtn.setCaption(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN));
            }

            ProjectTaskService service = AppContextUtil.getSpringBean(ProjectTaskService.class);
            service.updateWithSession(beanItem, UserUIContext.getUsername());
        });
        quickActionStatusBtn.setWidth("100%");

        taskPreviewForm.insertToControlBlock(quickActionStatusBtn);

        if (!CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            quickActionStatusBtn.setEnabled(false);
        }

        return topPanel;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MVerticalLayout toolbarLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        toolbarLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        relatedComments = new CommentNavigationButton(ProjectTypeConstants.TASK, beanItem.getId() + "");
        Component section = FormSectionBuilder.build(FontAwesome.COMMENT, relatedComments);
        toolbarLayout.addComponent(section);

        if (!SiteConfiguration.isCommunityEdition()) {
            taskTimeLogComp = new TaskTimeLogComp();
            toolbarLayout.addComponent(taskTimeLogComp);
        }

        return toolbarLayout;
    }

    private class ReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
        private static final long serialVersionUID = 1L;

        ReadFormFieldFactory(GenericBeanForm<SimpleTask> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (Task.Field.assignuser.equalTo(propertyId)) {
                return new DefaultViewField(ProjectLinkBuilder.generateProjectMemberHtmlLink(CurrentProjectVariables.getProjectId(),
                        beanItem.getAssignuser(), beanItem.getAssignUserFullName(), beanItem.getAssignUserAvatarId(), false), ContentMode.HTML);
            } else if (Task.Field.startdate.equalTo(propertyId)) {
                return new DefaultViewField(UserUIContext.formatDate(beanItem.getStartdate()));
            } else if (Task.Field.enddate.equalTo(propertyId)) {
                return new DefaultViewField(UserUIContext.formatDate(beanItem.getEnddate()));
            } else if (Task.Field.duedate.equalTo(propertyId)) {
                return new DefaultViewField(UserUIContext.formatDate(beanItem.getDuedate()));
            } else if (Task.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    FontAwesome fontPriority = ProjectAssetsManager.getPriority(beanItem.getPriority());
                    String priorityLbl = fontPriority.getHtml() + " " + UserUIContext.getMessage(Priority.class, beanItem.getPriority());
                    DefaultViewField field = new DefaultViewField(priorityLbl, ContentMode.HTML);
                    field.addStyleName("priority-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (Task.Field.milestoneid.equalTo(propertyId)) {
                if (beanItem.getMilestoneid() != null) {
                    A milestoneLink = new A(ProjectLinkBuilder.generateMilestonePreviewFullLink
                            (CurrentProjectVariables.getProjectId(), beanItem.getMilestoneid())).appendText(beanItem.getMilestoneName());
                    Div milestoneDiv = new Div().appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants
                            .MILESTONE).getHtml()).appendChild(DivLessFormatter.EMPTY_SPACE(), milestoneLink);
                    return new DefaultViewField(milestoneDiv.write(), ContentMode.HTML);
                }
            } else if (Task.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (Task.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), StatusI18nEnum.class).withStyleName(UIConstants.FIELD_NOTE);
            }
            return null;
        }
    }
}
