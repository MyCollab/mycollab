/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.milestone;

import com.hp.gagawa.java.elements.Span;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.project.event.MilestoneEvent;
import com.mycollab.mobile.module.project.ui.CommentNavigationButton;
import com.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.mycollab.mobile.module.project.view.ticket.TicketNavigatorButton;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
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
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class MilestoneReadViewImpl extends AbstractPreviewItemComp<SimpleMilestone> implements MilestoneReadView {
    private static final long serialVersionUID = -2466318105833801922L;

    private CommentNavigationButton relatedComments;
    private TicketNavigatorButton ticketNavigatorButton;

    @Override
    public HasPreviewFormHandlers<SimpleMilestone> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    protected void afterPreviewItem() {
        relatedComments.displayTotalComments(beanItem.getId() + "");
        ticketNavigatorButton.displayTotalIssues(beanItem.getId());
    }

    @Override
    protected String initFormHeader() {
        Span beanTitle = new Span().appendText(beanItem.getName());
        if (beanItem.isCompleted()) {
            beanTitle.setCSSClass(MobileUIConstants.LINK_COMPLETED);
        } else if (beanItem.isOverdue()) {
            beanTitle.setCSSClass(MobileUIConstants.LINK_OVERDUE);
        }
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml() + " " + beanTitle.write();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected void initRelatedComponents() {

    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.MILESTONE;
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.MILESTONE, MilestoneDefaultFormLayoutFactory.getForm(),
                Milestone.Field.name.name());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
        return new MilestoneFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleMilestone> formControlsGenerator = new ProjectPreviewFormControlsGenerator<>(previewForm);
        final VerticalLayout formControls = formControlsGenerator.createButtonControls(
                ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED,
                ProjectRolePermissionCollections.MILESTONES);
        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoEdit(this, beanItem)))
                .withIcon(FontAwesome.EDIT).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));
        return new MHorizontalLayout(editBtn, new NavigationBarQuickMenu(formControls));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MVerticalLayout toolbarLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        toolbarLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        ticketNavigatorButton = new TicketNavigatorButton();
        Component issueSection = FormSectionBuilder.build(FontAwesome.TICKET, ticketNavigatorButton);
        toolbarLayout.addComponent(issueSection);
        relatedComments = new CommentNavigationButton(ProjectTypeConstants.MILESTONE, beanItem.getId() + "");
        Component commentSection = FormSectionBuilder.build(FontAwesome.COMMENT, relatedComments);
        toolbarLayout.addComponent(commentSection);
        return toolbarLayout;
    }

    private class MilestoneFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {
        private static final long serialVersionUID = 1L;

        MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (Milestone.Field.startdate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getStartdate());
            } else if (Milestone.Field.enddate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getEnddate());
            } else if (Milestone.Field.assignuser.equalTo(propertyId)) {
                return new DefaultViewField(ProjectLinkBuilder.generateProjectMemberHtmlLink(CurrentProjectVariables.getProjectId(),
                        beanItem.getAssignuser(), beanItem.getOwnerFullName(), beanItem.getOwnerAvatarId(), false), ContentMode.HTML);
            } else if (Milestone.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (Milestone.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), MilestoneStatus.class).withStyleName(UIConstants.FIELD_NOTE);
            }
            return null;
        }
    }

}
