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
package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.MilestoneEvent;
import com.mycollab.mobile.module.project.ui.CommentNavigationButton;
import com.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.mycollab.mobile.module.project.view.ticket.TicketNavigatorButton;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.field.DateViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
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
    protected String initFormTitle() {
        return UserUIContext.getMessage(MilestoneI18nEnum.SINGLE);
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected void initRelatedComponents() {

    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new MilestoneFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
        return new MilestoneFormFieldFactory(previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new ProjectPreviewFormControlsGenerator<>(previewForm).createButtonControls(ProjectRolePermissionCollections.MILESTONES);
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

    @Override
    protected String getBackTitle() {
        return UserUIContext.getMessage(MilestoneI18nEnum.LIST);
    }

    @Override
    protected void doBackAction() {
        EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
    }

    private class MilestoneFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {
        private static final long serialVersionUID = 1L;

        MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (propertyId.equals("startdate")) {
                return new DateViewField(beanItem.getStartdate());
            } else if (propertyId.equals("enddate")) {
                return new DateViewField(beanItem.getEnddate());
            } else if (propertyId.equals("owner")) {
                return new DefaultViewField(ProjectLinkBuilder.generateProjectMemberHtmlLink(CurrentProjectVariables.getProjectId(),
                        beanItem.getAssignuser(), beanItem.getOwnerFullName(), beanItem.getOwnerAvatarId(), false), ContentMode.HTML);
            } else if (propertyId.equals("description")) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (propertyId.equals("status")) {
                return new DefaultViewField(UserUIContext.getMessage(MilestoneStatus.class, beanItem.getStatus()));
            }
            return null;
        }
    }

}
