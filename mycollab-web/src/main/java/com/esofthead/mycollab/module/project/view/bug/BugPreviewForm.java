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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentDisplayField;
import com.esofthead.mycollab.module.project.ui.form.ProjectItemViewField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.field.*;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections.CollectionUtils;

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

    private static class FormLayoutFactory implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public void attachField(Object propertyId, final Field<?> field) {
            if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 0, 2, "100%");
            } else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0, 1, 2, "100%");
            } else if (BugWithBLOBs.Field.status.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_STATUS),
                        AppContext.getMessage(BugI18nEnum.FORM_STATUS_HELP), 0, 2);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PRIORITY),
                        AppContext.getMessage(BugI18nEnum.FORM_PRIORITY_HELP), 1, 2);
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 3);
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION),
                        AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION_HELP), 1, 3);
            } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_START_DATE), 0, 4);
            } else if (BugWithBLOBs.Field.createdtime.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME), 1, 4);
            } else if (BugWithBLOBs.Field.enddate.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_END_DATE), 0, 5);
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_LOG_BY), 1, 5);
            } else if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE), 0, 6);
            } else if (SimpleBug.Field.assignuserFullName.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 6);
            } else if (SimpleBug.Field.milestoneName.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PHASE), 0, 7, 2, "100%");
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS), AppContext
                        .getMessage(BugI18nEnum.FORM_COMPONENTS_HELP), 0, 8, 2, "100%");
            } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS),
                        AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP), 0, 9, 2, "100%");
            } else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS),
                        AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS_HELP), 0, 10, 2, "100%");
            } else if (BugWithBLOBs.Field.estimatetime.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE),
                        AppContext.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE_HELP), 0, 11);
            } else if (BugWithBLOBs.Field.estimateremaintime.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE),
                        AppContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE_HELP), 1, 11);
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS), 0, 12, 2, "100%");
            }
        }

        @Override
        public ComponentContainer getLayout() {
            final VerticalLayout layout = new VerticalLayout();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 13);
            layout.addComponent(informationLayout.getLayout());
            layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);
            return layout;
        }
    }

    private static class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleBug> {
        private static final long serialVersionUID = 1L;

        public PreviewFormFieldFactory(GenericBeanForm<SimpleBug> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleBug beanItem = attachForm.getBean();
            if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getDueDateRoundPlusOne());
            } else if (BugWithBLOBs.Field.createdtime.equalTo(propertyId)) {
                return new DateViewField(beanItem.getCreatedtime());
            } else if (BugWithBLOBs.Field.startdate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getStartdate());
            } else if (BugWithBLOBs.Field.enddate.equalTo(propertyId)) {
                return new DateViewField(beanItem.getEnddate());
            } else if (SimpleBug.Field.assignuserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getAssignuser(), beanItem.getAssignUserAvatarId(),
                        beanItem.getAssignuserFullName());
            } else if (SimpleBug.Field.loguserFullName.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(beanItem.getLogby(), beanItem.getLoguserAvatarId(), beanItem.getLoguserFullName());
            } else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {
                return new ProjectFormAttachmentDisplayField(
                        beanItem.getProjectid(), ProjectTypeConstants.BUG, beanItem.getId());
            } else if (SimpleBug.Field.components.equalTo(propertyId)) {
                final List<Component> components = beanItem.getComponents();
                if (CollectionUtils.isNotEmpty(components)) {
                    ContainerViewField componentContainer = new ContainerViewField();
                    for (final Component component : beanItem.getComponents()) {
                        Button componentLink = new Button(StringUtils.trim(component.getComponentname(), 25, true), new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                EventBusFactory.getInstance().post(new BugComponentEvent.GotoRead(this, component.getId()));
                            }
                        });
                        componentLink.setDescription(component.getComponentname());
                        componentContainer.addComponentField(componentLink);
                        componentLink.setStyleName(ValoTheme.BUTTON_SMALL);
                        componentLink.addStyleName(UIConstants.BUTTON_BLOCK);
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
                        Button versionLink = new Button(StringUtils.trim(version.getVersionname(), 25, true), new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId()));
                            }
                        });
                        versionLink.setDescription(version.getVersionname());
                        componentContainer.addComponentField(versionLink);
                        versionLink.setStyleName(ValoTheme.BUTTON_SMALL);
                        versionLink.addStyleName(UIConstants.BUTTON_BLOCK);
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
                        Button versionLink = new Button(StringUtils.trim(version.getVersionname(), 25, true), new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, version.getId()));
                            }
                        });
                        versionLink.setDescription(version.getVersionname());
                        componentContainer.addComponentField(versionLink);
                        versionLink.setStyleName(ValoTheme.BUTTON_SMALL);
                        versionLink.addStyleName(UIConstants.BUTTON_BLOCK);
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
                return new I18nFormViewField(beanItem.getStatus(), OptionI18nEnum.BugStatus.class).withStyleName
                        (UIConstants.FIELD_NOTE);
            } else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getPriority())) {
                    String priorityLink = ProjectAssetsManager.getBugPriority(beanItem.getPriority()).getHtml() + " " + beanItem.getPriority();
                    DefaultViewField field = new DefaultViewField(priorityLink, ContentMode.HTML);
                    field.addStyleName("bug-" + beanItem.getPriority().toLowerCase());
                    return field;
                }
            } else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
                if (StringUtils.isNotBlank(beanItem.getSeverity())) {
                    String severityLink = FontAwesome.STAR.getHtml() + " " +
                            AppContext.getMessage(OptionI18nEnum.BugSeverity.class, beanItem.getSeverity());
                    DefaultViewField lbPriority = new DefaultViewField(severityLink, ContentMode.HTML);
                    lbPriority.addStyleName("bug-severity-" + beanItem.getSeverity().toLowerCase());
                    return lbPriority;
                }
            } else if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                return new I18nFormViewField(beanItem.getResolution(), OptionI18nEnum.BugResolution.class);
            }
            return null;
        }
    }
}
