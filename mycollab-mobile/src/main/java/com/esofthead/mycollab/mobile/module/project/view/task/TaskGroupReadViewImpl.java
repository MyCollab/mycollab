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
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectCommentListDisplay;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */

@ViewComponent
public class TaskGroupReadViewImpl extends AbstractPreviewItemComp<SimpleTaskList> implements TaskGroupReadView {
    private static final long serialVersionUID = 8303226753169728418L;

    private ProjectCommentListDisplay associateComments;
    private Button relatedComments;

    @Override
    protected void afterPreviewItem() {
        associateComments.loadComments("" + beanItem.getId());
    }

    @Override
    protected String initFormTitle() {
        return this.beanItem.getName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleTaskList> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected void initRelatedComponents() {
        associateComments = new ProjectCommentListDisplay(
                ProjectTypeConstants.TASK,
                CurrentProjectVariables.getProjectId(), true,
                ProjectTaskGroupRelayEmailNotificationAction.class);
        if (associateComments.getNumComments() > 0) {
            relatedComments
                    .setCaption("<span aria-hidden=\"true\" data-icon=\""
                            + IconConstants.PROJECT_MESSAGE
                            + "\" data-count=\""
                            + associateComments.getNumComments()
                            + "\"></span><div class=\"screen-reader-text\">"
                            + AppContext
                            .getMessage(GenericI18Enum.TAB_COMMENT)
                            + "</div>");
        } else {
            relatedComments
                    .setCaption("<span aria-hidden=\"true\" data-icon=\""
                            + IconConstants.PROJECT_MESSAGE
                            + "\"></span><div class=\"screen-reader-text\">"
                            + AppContext
                            .getMessage(GenericI18Enum.TAB_COMMENT)
                            + "</div>");
        }
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new TaskGroupFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList> initBeanFormFieldFactory() {
        return new TaskGroupBeanFieldGroupFactory(this.previewForm);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new ProjectPreviewFormControlsGenerator<>(this.previewForm)
                .createButtonControls(ProjectRolePermissionCollections.TASKS);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        HorizontalLayout toolbarLayout = new HorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        toolbarLayout.setSpacing(true);

        relatedComments = new Button();
        relatedComments.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.PROJECT_MESSAGE
                + "\"></span><div class=\"screen-reader-text\">"
                + AppContext.getMessage(GenericI18Enum.TAB_COMMENT)
                + "</div>");
        relatedComments.setHtmlContentAllowed(true);
        relatedComments.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 2276465280812964681L;

            @Override
            public void buttonClick(ClickEvent arg0) {
                EventBusFactory.getInstance().post(
                        new ShellEvent.PushView(this, associateComments));
            }
        });
        toolbarLayout.addComponent(relatedComments);

        return toolbarLayout;
    }

    private class TaskGroupBeanFieldGroupFactory extends
            AbstractBeanFieldGroupViewFieldFactory<SimpleTaskList> {

        private static final long serialVersionUID = 4554258685587024348L;

        public TaskGroupBeanFieldGroupFactory(
                GenericBeanForm<SimpleTaskList> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(Object propertyId) {
            if (propertyId.equals("milestoneid")) {
                return new DefaultViewField(beanItem.getMilestoneName());
            } else if (propertyId.equals("owner")) {
                return new DefaultViewField(beanItem.getOwnerFullName());
            } else if (propertyId.equals("percentageComplete")) {
                final DefaultViewField progressField = new DefaultViewField(((int) (beanItem.getPercentageComplete() * 100)) / 100 + "%");
                return progressField;
            } else if (propertyId.equals("description")) {
                return new DefaultViewField(beanItem.getDescription(), ContentMode.HTML);
            } else if (propertyId.equals("numOpenTasks")) {
                final DefaultViewField fieldContainer = new DefaultViewField("(" + beanItem.getNumOpenTasks() + "/"
                        + beanItem.getNumAllTasks() + ")");
                return fieldContainer;
            }

            return null;
        }

    }

    @Override
    public HasPreviewFormHandlers<SimpleTaskList> getPreviewFormHandlers() {
        return this.previewForm;
    }

}
