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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.mycollab.module.project.ui.form.ProjectItemViewField;
import com.mycollab.module.project.view.bug.field.ComponentsViewField;
import com.mycollab.module.project.view.bug.field.VersionsViewField;
import com.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.mycollab.module.project.domain.BugWithBLOBs;
import com.mycollab.module.project.domain.SimpleBug;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.field.*;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class BugPreviewForm extends AdvancedPreviewBeanForm<SimpleBug> {
    @Override
    public void setBean(SimpleBug bean) {
        this.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.BUG, BugDefaultFormLayoutFactory.getReadForm()));
        this.setBeanFormFieldFactory(new PreviewFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {
        private static final long serialVersionUID = 1L;

        PreviewFormFieldFactory(GenericBeanForm<SimpleBug> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(final Object propertyId) {
            SimpleBug beanItem = attachForm.getBean();
            if (SimpleBug.Field.assignuserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getProjectid(), beanItem.getAssignuser(),
                        beanItem.getAssignUserAvatarId(), beanItem.getAssignuserFullName());
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getProjectid(), beanItem.getCreateduser(),
                        beanItem.getLoguserAvatarId(), beanItem.getLoguserFullName());
            } else if ("section-attachments".equals(propertyId)) {
                return new ProjectFormAttachmentDisplayField(
                        beanItem.getProjectid(), ProjectTypeConstants.BUG, beanItem.getId());
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                return new ComponentsViewField();
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)
                    || SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                return new VersionsViewField();
            } else if (BugWithBLOBs.Field.milestoneid.equalTo(propertyId)) {
                return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid(),
                        beanItem.getMilestoneName());
            } else if (BugWithBLOBs.Field.description.equalTo(propertyId) || BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                return new RichTextViewField();
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(StatusI18nEnum.class).withStyleName(WebThemes.FIELD_NOTE);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    String priorityLink = ProjectAssetsManager.getPriority(beanItem.getPriority()).getHtml() + " "
                            + UserUIContext.getMessage(OptionI18nEnum.Priority.class, beanItem.getPriority());
                    StyleViewField field = new StyleViewField(priorityLink);
                    field.addStyleName("priority-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getSeverity())) {
                    String severityLink = VaadinIcons.STAR.getHtml() + " " +
                            UserUIContext.getMessage(BugSeverity.class, beanItem.getSeverity());
                    StyleViewField lbPriority = new StyleViewField(severityLink);
                    lbPriority.addStyleName("bug-severity-" + beanItem.getSeverity().toLowerCase());
                    return lbPriority;
                }
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                return new I18nFormViewField(BugResolution.class, GenericI18Enum.OPT_UNDEFINED).withStyleName(WebThemes.FIELD_NOTE);
            }
            return null;
        }
    }
}
