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
package com.mycollab.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugComponentEvent;
import com.mycollab.module.project.event.BugVersionEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.mycollab.module.project.ui.form.ProjectItemViewField;
import com.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DateTimeOptionViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.field.ContainerViewField;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class BugPreviewForm extends AdvancedPreviewBeanForm<SimpleBug> {
    @Override
    public void setBean(SimpleBug bean) {
        this.setFormLayoutFactory(new FormLayoutFactory());
        this.setBeanFormFieldFactory(new PreviewFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class FormLayoutFactory extends AbstractFormLayoutFactory {
        private GridFormLayoutHelper informationLayout;

        @Override
        protected com.vaadin.ui.Component onAttachField(Object propertyId, final Field<?> field) {
            if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 0, 2, "100%");
            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0, 1, 2, "100%");
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_STATUS),
                        UserUIContext.getMessage(BugI18nEnum.FORM_STATUS_HELP), 0, 2);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY),
                        UserUIContext.getMessage(GenericI18Enum.FORM_PRIORITY_HELP), 1, 2);
            } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE), 0, 3);
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_SEVERITY), 1, 3);
            } else if (BugWithBLOBs.Field.enddate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE), 0, 4);
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_RESOLUTION),
                        UserUIContext.getMessage(BugI18nEnum.FORM_RESOLUTION_HELP), 1, 4);
            } else if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE), 0, 5);
            } else if (SimpleBug.Field.milestoneName.equalTo(propertyId)) {
                informationLayout.addComponent(field, UserUIContext.getMessage(MilestoneI18nEnum.SINGLE), 1, 5);
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_COMPONENTS), UserUIContext
                        .getMessage(BugI18nEnum.FORM_COMPONENTS_HELP), 0, 6, 2, "100%");
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS),
                        UserUIContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP), 0, 7, 2, "100%");
            } else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS),
                        UserUIContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS_HELP), 0, 8, 2, "100%");
            } else if (BugWithBLOBs.Field.originalestimate.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE),
                        UserUIContext.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE_HELP), 0, 9);
            } else if (BugWithBLOBs.Field.remainestimate.equalTo(propertyId)) {
                informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE),
                        UserUIContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE_HELP), 1, 9);
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS), 0, 10, 2, "100%");
            }
            return null;
        }

        @Override
        public AbstractComponent getLayout() {
            final VerticalLayout layout = new VerticalLayout();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 11);
            layout.addComponent(informationLayout.getLayout());
            layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);
            return layout;
        }
    }

    private static class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {
        private static final long serialVersionUID = 1L;

        PreviewFormFieldFactory(GenericBeanForm<SimpleBug> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleBug beanItem = attachForm.getBean();
            if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                return new DateTimeOptionViewField(beanItem.getDuedate());
            } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId)) {
                return new DateTimeOptionViewField(beanItem.getStartdate());
            } else if (BugWithBLOBs.Field.enddate.equalTo(propertyId)) {
                return new DateTimeOptionViewField(beanItem.getEnddate());
            } else if (SimpleBug.Field.assignuserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getAssignuser(), beanItem.getAssignUserAvatarId(),
                        beanItem.getAssignuserFullName());
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getCreateduser(), beanItem.getLoguserAvatarId(), beanItem.getLoguserFullName());
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(
                        beanItem.getProjectid(), ProjectTypeConstants.BUG, beanItem.getId());
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                final List<Component> components = beanItem.getComponents();
                if (CollectionUtils.isNotEmpty(components)) {
                    ContainerViewField componentContainer = new ContainerViewField();
                    for (final Component component : beanItem.getComponents()) {
                        MButton componentLink = new MButton(StringUtils.trim(component.getName(), 25, true),
                                clickEvent -> EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, component.getId())))
                                .withDescription(component.getName()).withStyleName(UIConstants.BLOCK, ValoTheme.BUTTON_SMALL);
                        componentContainer.addComponentField(componentLink);
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                List<Version> affectedVersions = beanItem.getAffectedVersions();
                if (CollectionUtils.isNotEmpty(affectedVersions)) {
                    ContainerViewField componentContainer = new ContainerViewField();
                    for (final Version version : beanItem.getAffectedVersions()) {
                        MButton versionLink = new MButton(StringUtils.trim(version.getName(), 25, true),
                                clickEvent -> EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId())))
                                .withDescription(version.getName()).withStyleName(UIConstants.BLOCK, ValoTheme.BUTTON_SMALL);
                        componentContainer.addComponentField(versionLink);
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }
            } else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                List<Version> fixedVersions = beanItem.getFixedVersions();
                if (CollectionUtils.isNotEmpty(fixedVersions)) {
                    ContainerViewField componentContainer = new ContainerViewField();
                    for (final Version version : beanItem.getFixedVersions()) {
                        MButton versionLink = new MButton(StringUtils.trim(version.getName(), 25, true),
                                clickEvent -> EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId())))
                                .withDescription(version.getName()).withStyleName(UIConstants.BLOCK, ValoTheme.BUTTON_SMALL);
                        componentContainer.addComponentField(versionLink);
                    }
                    return componentContainer;
                } else {
                    return new DefaultViewField("");
                }

            } else if (SimpleBug.Field.milestoneName.equalTo(propertyId)) {
                return new ProjectItemViewField(ProjectTypeConstants.MILESTONE, beanItem.getMilestoneid() + "",
                        beanItem.getMilestoneName());
            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getEnvironment());
            } else if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(beanItem.getDescription());
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getStatus(), BugStatus.class).withStyleName(UIConstants.FIELD_NOTE);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    String priorityLink = ProjectAssetsManager.getPriority(beanItem.getPriority()).getHtml() + " "
                            + UserUIContext.getMessage(OptionI18nEnum.Priority.class, beanItem.getPriority());
                    DefaultViewField field = new DefaultViewField(priorityLink, ContentMode.HTML);
                    field.addStyleName("priority-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getSeverity())) {
                    String severityLink = FontAwesome.STAR.getHtml() + " " +
                            UserUIContext.getMessage(BugSeverity.class, beanItem.getSeverity());
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
