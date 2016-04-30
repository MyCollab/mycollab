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

import com.esofthead.mycollab.common.domain.CommentWithBLOBs;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.view.bug.components.BugResolutionComboBox;
import com.esofthead.mycollab.module.project.view.bug.components.BugSelectionField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.project.view.settings.component.VersionMultiSelectField;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.RelatedBug;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugRelatedItemService;
import com.esofthead.mycollab.module.tracker.service.BugRelationService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ResolvedInputWindow extends Window {
    private static final long serialVersionUID = 1L;

    private final SimpleBug bug;
    private BugSelectionField bugSelectionField;
    private VersionMultiSelectField fixedVersionSelect;

    public ResolvedInputWindow(SimpleBug bugValue) {
        super("Resolve bug '" + bugValue.getSummary() + "'");
        this.bug = BeanUtility.deepClone(bugValue);
        this.setWidth("900px");
        this.setResizable(false);
        this.setModal(true);
        EditForm editForm = new EditForm();
        editForm.setBean(bug);
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(new MarginInfo(false, false, true, false));
        contentLayout.addComponent(editForm);
        this.setContent(contentLayout);
        this.center();
    }

    private class EditForm extends AdvancedEditBeanForm<BugWithBLOBs> {
        private static final long serialVersionUID = 1L;
        private RichTextArea commentArea;

        @Override
        public void setBean(BugWithBLOBs newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory implements IFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                final VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
                layout.addComponent(informationLayout.getLayout());

                final MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true, false, false));
                layout.addComponent(controlsBtn);

                Button resolveBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (EditForm.this.validateForm()) {
                            String commentValue = commentArea.getValue();
                            if (BugResolution.Duplicate.name().equals(bug.getResolution())) {
                                if (bugSelectionField != null && bugSelectionField.getSelectedBug() != null) {
                                    SimpleBug selectedBug = bugSelectionField.getSelectedBug();
                                    if (selectedBug.getId().equals(bug.getId())) {
                                        throw new UserInvalidInputException("The relation is invalid since the both entries are " + "the same");
                                    }
                                    BugRelationService relatedBugService = ApplicationContextUtil.getSpringBean(BugRelationService.class);
                                    RelatedBug relatedBug = new RelatedBug();
                                    relatedBug.setBugid(bug.getId());
                                    relatedBug.setRelatetype(OptionI18nEnum.BugRelation.Duplicated.name());
                                    relatedBug.setRelatedid(selectedBug.getId());
                                    relatedBugService.saveWithSession(relatedBug, AppContext.getUsername());
                                } else {
                                    NotificationUtil.showErrorNotification("You must select the duplicated bug for " +
                                            "the resolution 'Duplicate'");
                                    return;
                                }
                            } else if (BugResolution.InComplete.name().equals(bug.getResolution()) ||
                                    BugResolution.CannotReproduce.name().equals(bug.getResolution()) ||
                                    BugResolution.Invalid.name().equals(bug.getResolution())) {
                                if (StringUtils.isBlank(commentValue)) {
                                    NotificationUtil.showErrorNotification("Comment must be not blank for the " +
                                            "resolution " + bug.getResolution());
                                    return;
                                }
                            }
                            bug.setStatus(BugStatus.Resolved.name());

                            BugRelatedItemService bugRelatedItemService = ApplicationContextUtil.getSpringBean(BugRelatedItemService.class);
                            bugRelatedItemService.updateFixedVersionsOfBug(bug.getId(), fixedVersionSelect.getSelectedItems());

                            // Save bug status and assignee
                            BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                            bugService.updateSelectiveWithSession(bug, AppContext.getUsername());

                            // Save comment
                            if (StringUtils.isNotBlank(commentValue)) {
                                CommentWithBLOBs comment = new CommentWithBLOBs();
                                comment.setComment(commentValue);
                                comment.setCreatedtime(new GregorianCalendar().getTime());
                                comment.setCreateduser(AppContext.getUsername());
                                comment.setSaccountid(AppContext.getAccountId());
                                comment.setType(ProjectTypeConstants.BUG);
                                comment.setTypeid("" + bug.getId());
                                comment.setExtratypeid(CurrentProjectVariables.getProjectId());

                                CommentService commentService = ApplicationContextUtil.getSpringBean(CommentService.class);
                                commentService.saveWithSession(comment, AppContext.getUsername());
                            }

                            close();
                            EventBusFactory.getInstance().post(new BugEvent.BugChanged(this, bug.getId()));
                        }

                    }
                });
                resolveBtn.setStyleName(UIConstants.BUTTON_ACTION);
                resolveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        close();
                    }
                });
                cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);
                controlsBtn.with(cancelBtn, resolveBtn);

                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

                return layout;
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("resolution")) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION),
                            AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION_HELP), 0, 0);
                } else if (propertyId.equals("assignuser")) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 1);
                } else if (propertyId.equals("fixedVersions")) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS),
                            AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS_HELP), 0, 2);
                } else if (propertyId.equals("comment")) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_COMMENT), 0, 3, 2, "100%");
                }
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<BugWithBLOBs> {
            private static final long serialVersionUID = 1L;

            public EditFormFieldFactory(GenericBeanForm<BugWithBLOBs> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (propertyId.equals("resolution")) {
                    if (StringUtils.isBlank(bean.getResolution()) || AppContext.getMessage(BugResolution.None).equals(bug.getResolution())) {
                        bean.setResolution(BugResolution.Fixed.name());
                    }
                    return new ResolutionField();
                } else if (propertyId.equals("assignuser")) {
                    bug.setAssignuser(bug.getLogby());
                    return new ProjectMemberSelectionField();
                } else if (propertyId.equals("fixedVersions")) {
                    fixedVersionSelect = new VersionMultiSelectField();
                    if (CollectionUtils.isEmpty(bug.getFixedVersions()) && CollectionUtils.isNotEmpty(bug.getAffectedVersions())) {
                        bug.setFixedVersions(bug.getAffectedVersions());
                    }
                    return fixedVersionSelect;
                } else if (propertyId.equals("comment")) {
                    commentArea = new RichTextArea();
                    commentArea.setNullRepresentation("");
                    return commentArea;
                }

                return null;
            }

            private class ResolutionField extends CompoundCustomField<BugWithBLOBs> {
                private MHorizontalLayout layout;
                private BugResolutionComboBox resolutionComboBox;

                ResolutionField() {
                    resolutionComboBox = BugResolutionComboBox.getInstanceForResolvedBugWindow();
                }

                @Override
                protected Component initContent() {
                    layout = new MHorizontalLayout(resolutionComboBox);
                    fieldGroup.bind(resolutionComboBox, BugWithBLOBs.Field.resolution.name());
                    resolutionComboBox.addValueChangeListener(new ValueChangeListener() {
                        @Override
                        public void valueChange(Property.ValueChangeEvent event) {
                            String value = (String) resolutionComboBox.getValue();
                            if (OptionI18nEnum.BugResolution.Duplicate.name().equals(value)) {
                                bugSelectionField = new BugSelectionField();
                                layout.with(new Label(" with "), bugSelectionField);
                            } else {
                                if (layout.getComponentCount() > 1) {
                                    layout.removeComponent(layout.getComponent(1));
                                    layout.removeComponent(layout.getComponent(1));
                                }
                            }
                        }
                    });
                    return layout;
                }

                @Override
                public Class<? extends BugWithBLOBs> getType() {
                    return BugWithBLOBs.class;
                }
            }
        }
    }
}
