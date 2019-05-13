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

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.form.ComponentsViewField;
import com.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.mycollab.module.project.ui.form.ProjectItemViewField;
import com.mycollab.module.project.ui.form.VersionsViewField;
import com.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.mycollab.module.project.view.ticket.SubTicketsComp;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.BooleanViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.ui.field.StyleViewField;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class TaskPreviewForm extends AdvancedPreviewBeanForm<SimpleTask> {
    @Override
    public void setBean(SimpleTask bean) {
        this.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getReadForm(),
                Task.Field.name.name(), SimpleTask.Field.selected.name()));
        this.setBeanFormFieldFactory(new PreviewFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleTask> {
        private static final long serialVersionUID = 1L;

        PreviewFormFieldFactory(GenericBeanForm<SimpleTask> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(final Object propertyId) {
            SimpleTask beanItem = attachForm.getBean();
            if (Task.Field.assignuser.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getProjectid(), beanItem.getAssignuser(),
                        beanItem.getAssignUserAvatarId(), beanItem.getAssignUserFullName());
            } else if (Task.Field.milestoneid.equalTo(propertyId)) {
                return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid(), beanItem.getMilestoneName());
            } else if ("section-attachments".equals(propertyId)) {
                return new ProjectFormAttachmentDisplayField(beanItem.getProjectid(), ProjectTypeConstants.TASK, beanItem.getId());
            } else if (SimpleTask.Field.components.equalTo(propertyId)) {
                return new ComponentsViewField();
            } else if (SimpleTask.Field.affectedVersions.equalTo(propertyId)) {
                return new VersionsViewField();
            } else if (Task.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    VaadinIcons fontPriority = ProjectAssetsManager.getPriority(beanItem.getPriority());
                    String priorityLbl = fontPriority.getHtml() + " " + UserUIContext.getMessage(Priority.class, beanItem.getPriority());
                    StyleViewField field = new StyleViewField(priorityLbl);
                    field.addStyleName("priority-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (Task.Field.isestimated.equalTo(propertyId)) {
                return new BooleanViewField();
            } else if (Task.Field.description.equalTo(propertyId)) {
                return new RichTextViewField();
            } else if ("section-subTickets".equals(propertyId)) {
                return new SubTicketsComp(ProjectTicket.buildTicketByTask(beanItem));
            } else if (Task.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(StatusI18nEnum.class).withStyleName(WebThemes.FIELD_NOTE);
            }
            return null;
        }
    }
}
