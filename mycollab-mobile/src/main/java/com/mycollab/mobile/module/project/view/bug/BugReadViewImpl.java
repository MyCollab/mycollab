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
package com.mycollab.mobile.module.project.view.bug;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.project.events.BugEvent;
import com.mycollab.mobile.module.project.ui.CommentNavigationButton;
import com.mycollab.mobile.module.project.ui.ProjectAttachmentDisplayComp;
import com.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DateViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class BugReadViewImpl extends AbstractPreviewItemComp<SimpleBug> implements BugReadView {
    private static final long serialVersionUID = 579279560838174387L;

    private CommentNavigationButton relatedComments;
    private MVerticalLayout bugWorkFlowControl;
    private BugTimeLogComp bugTimeLogComp;
    private ProjectAttachmentDisplayComp attachmentComp;

    @Override
    public HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers() {
        return previewForm;
    }

    private void displayWorkflowControl() {
        bugWorkFlowControl.removeAllComponents();
        if (BugStatus.Open.name().equals(beanItem.getStatus()) || BugStatus.ReOpen.name().equals(beanItem.getStatus())) {
            final Button resolveBtn = new Button(UserUIContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.PushView(this,
                            new ResolvedInputView(BugReadViewImpl.this, beanItem))));
            resolveBtn.setWidth("100%");
            bugWorkFlowControl.addComponent(resolveBtn);
        } else if (BugStatus.Verified.name().equals(beanItem.getStatus())) {
            final Button reopenBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.PushView(this,
                            new ReOpenView(BugReadViewImpl.this, beanItem))));
            reopenBtn.setWidth("100%");
            bugWorkFlowControl.addComponent(reopenBtn);
        } else if (BugStatus.Resolved.name().equals(beanItem.getStatus())) {
            final Button reopenBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.PushView(this,
                            new ReOpenView(BugReadViewImpl.this, beanItem))));
            reopenBtn.setWidth("100%");
            bugWorkFlowControl.addComponent(reopenBtn);

            final Button approveNCloseBtn = new Button(UserUIContext.getMessage(BugI18nEnum.BUTTON_APPROVE_CLOSE),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.PushView(this,
                            new ApproveInputView(BugReadViewImpl.this, beanItem))));
            approveNCloseBtn.setWidth("100%");
            bugWorkFlowControl.addComponent(approveNCloseBtn);
        }
        bugWorkFlowControl.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
    }

    @Override
    protected void afterPreviewItem() {
        relatedComments.displayTotalComments(beanItem.getId() + "");

        displayWorkflowControl();
        if (!SiteConfiguration.isCommunityEdition()) {
            bugTimeLogComp.displayTime(beanItem);
        }

        ResourceService resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        List<Content> attachments = resourceService.getContents(AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(),
                beanItem.getProjectid(), ProjectTypeConstants.BUG, "" + beanItem.getId()));
        if (CollectionUtils.isNotEmpty(attachments)) {
            attachmentComp = new ProjectAttachmentDisplayComp(attachments);
            previewForm.addComponent(attachmentComp);
        } else {
            if (attachmentComp != null) {
                previewForm.removeComponent(attachmentComp);
            }
        }
    }

    @Override
    protected String initFormHeader() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG).getHtml() + " " + beanItem.getName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleBug> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }


    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.BUG, BugDefaultFormLayoutFactory.getForm(), BugWithBLOBs.Field.name.name());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleBug> initBeanFormFieldFactory() {
        return new BugPreviewBeanFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleBug> formControlsGenerator = new ProjectPreviewFormControlsGenerator<>(previewForm);
        bugWorkFlowControl = new MVerticalLayout().withMargin(false).withFullWidth();
        formControlsGenerator.insertToControlBlock(bugWorkFlowControl);
        VerticalLayout formControls = formControlsGenerator.createButtonControls(ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
                | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED, ProjectRolePermissionCollections.BUGS);
        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new BugEvent.GotoEdit(this,
                beanItem))).withIcon(FontAwesome.EDIT).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
        return new MHorizontalLayout(editBtn, new NavigationBarQuickMenu(formControls));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MVerticalLayout toolbarLayout = new MVerticalLayout().withFullWidth().withSpacing(false).withMargin(false);
        toolbarLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        if (!SiteConfiguration.isCommunityEdition()) {
            bugTimeLogComp = new BugTimeLogComp();
            toolbarLayout.addComponent(bugTimeLogComp);
        }

        relatedComments = new CommentNavigationButton(ProjectTypeConstants.BUG, beanItem.getId() + "");
        Component section = FormSectionBuilder.build(FontAwesome.COMMENT, relatedComments);
        toolbarLayout.addComponent(section);
        return toolbarLayout;
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG;
    }

    private class BugPreviewBeanFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {
        private static final long serialVersionUID = -288972730658409446L;

        BugPreviewBeanFormFieldFactory(GenericBeanForm<SimpleBug> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getDuedate());
            } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getStartdate());
            } else if (BugWithBLOBs.Field.enddate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getEnddate());
            } else if (BugWithBLOBs.Field.assignuser.equalTo(propertyId)) {
                return new DefaultViewField(ProjectLinkBuilder.generateProjectMemberHtmlLink
                        (CurrentProjectVariables.getProjectId(), beanItem.getAssignuser(), beanItem.getAssignuserFullName(),
                                beanItem.getAssignUserAvatarId(), false), ContentMode.HTML);
            } else if (BugWithBLOBs.Field.milestoneid.equalTo(propertyId)) {
                if (beanItem.getMilestoneid() != null) {
                    A milestoneLink = new A(ProjectLinkBuilder.generateMilestonePreviewFullLink
                            (CurrentProjectVariables.getProjectId(), beanItem.getMilestoneid())).appendText(beanItem.getMilestoneName());
                    Div milestoneDiv = new Div().appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants
                            .MILESTONE).getHtml()).appendChild(DivLessFormatter.EMPTY_SPACE(), milestoneLink);
                    return new DefaultViewField(milestoneDiv.write(), ContentMode.HTML);
                } else {
                    return new DefaultViewField("", ContentMode.HTML);
                }

            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getEnvironment());
            } else if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), BugStatus.class).withStyleName(UIConstants.FIELD_NOTE);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    String priorityLink = ProjectAssetsManager.getPriority(beanItem.getPriority()).getHtml() + " "
                            + UserUIContext.getMessage(Priority.class, beanItem.getPriority());
                    DefaultViewField field = new DefaultViewField(priorityLink, ContentMode.HTML);
                    field.addStyleName("priority-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getSeverity())) {
                    String severityLink = FontAwesome.STAR.getHtml() + " " + UserUIContext.getMessage(BugSeverity.class, beanItem.getSeverity());
                    DefaultViewField lbPriority = new DefaultViewField(severityLink, ContentMode.HTML);
                    lbPriority.addStyleName("bug-severity-" + beanItem.getSeverity().toLowerCase());
                    return lbPriority;
                }
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getResolution(), BugResolution.class);
            }
            return null;
        }
    }
}
