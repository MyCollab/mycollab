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

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.events.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.view.bug.components.BugResolutionComboBox;
import com.mycollab.module.project.view.bug.components.BugSelectionField;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.module.project.view.settings.component.VersionMultiSelectField;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.RelatedBug;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelatedItemService;
import com.mycollab.module.tracker.service.BugRelationService;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ResolvedInputWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private final SimpleBug bug;
    private BugSelectionField bugSelectionField;
    private VersionMultiSelectField fixedVersionSelect;

    public ResolvedInputWindow(SimpleBug bugValue) {
        super(AppContext.getMessage(BugI18nEnum.OPT_RESOLVE_BUG, bugValue.getSummary()));
        this.bug = BeanUtility.deepClone(bugValue);
        EditForm editForm = new EditForm();
        editForm.setBean(bug);
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(new MarginInfo(false, false, true, false));
        contentLayout.addComponent(editForm);
        withWidth("900px").withModal(true).withResizable(false).withContent(contentLayout).withCenter();
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

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                final VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
                layout.addComponent(informationLayout.getLayout());

                MButton resolveBtn = new MButton(AppContext.getMessage(BugI18nEnum.BUTTON_RESOLVED), clickEvent -> {
                    if (EditForm.this.validateForm()) {
                        String commentValue = commentArea.getValue();
                        if (BugResolution.Duplicate.name().equals(bug.getResolution())) {
                            if (bugSelectionField != null && bugSelectionField.getSelectedBug() != null) {
                                SimpleBug selectedBug = bugSelectionField.getSelectedBug();
                                if (selectedBug.getId().equals(bug.getId())) {
                                    throw new UserInvalidInputException("The relation is invalid since the both entries are " + "the same");
                                }
                                BugRelationService relatedBugService = AppContextUtil.getSpringBean(BugRelationService.class);
                                RelatedBug relatedBug = new RelatedBug();
                                relatedBug.setBugid(bug.getId());
                                relatedBug.setRelatetype(OptionI18nEnum.BugRelation.Duplicated.name());
                                relatedBug.setRelatedid(selectedBug.getId());
                                relatedBugService.saveWithSession(relatedBug, AppContext.getUsername());
                            } else {
                                NotificationUtil.showErrorNotification(AppContext.getMessage(BugI18nEnum.ERROR_DUPLICATE_BUG_SELECT));
                                return;
                            }
                        } else if (BugResolution.InComplete.name().equals(bug.getResolution()) ||
                                BugResolution.CannotReproduce.name().equals(bug.getResolution()) ||
                                BugResolution.Invalid.name().equals(bug.getResolution())) {
                            if (StringUtils.isBlank(commentValue)) {
                                NotificationUtil.showErrorNotification(AppContext.getMessage(BugI18nEnum.ERROR_COMMENT_NOT_BLANK_FOR_RESOLUTION, bug.getResolution()));
                                return;
                            }
                        }
                        bug.setStatus(BugStatus.Resolved.name());

                        BugRelatedItemService bugRelatedItemService = AppContextUtil.getSpringBean(BugRelatedItemService.class);
                        bugRelatedItemService.updateFixedVersionsOfBug(bug.getId(), fixedVersionSelect.getSelectedItems());

                        // Save bug status and assignee
                        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
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

                            CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                            commentService.saveWithSession(comment, AppContext.getUsername());
                        }

                        close();
                        EventBusFactory.getInstance().post(new BugEvent.BugChanged(this, bug.getId()));
                    }
                }).withStyleName(WebUIConstants.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);

                MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                        .withStyleName(WebUIConstants.BUTTON_OPTION);

                final MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, resolveBtn).withMargin(new MarginInfo(true, true, false, false));
                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

                return layout;
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("resolution")) {
                    return informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION),
                            AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION_HELP), 0, 0);
                } else if (propertyId.equals("assignuser")) {
                    return informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 1);
                } else if (propertyId.equals("fixedVersions")) {
                    return informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS),
                            AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS_HELP), 0, 2);
                } else if (propertyId.equals("comment")) {
                    return informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.OPT_COMMENT), 0, 3, 2, "100%");
                }
                return null;
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
                    resolutionComboBox.addValueChangeListener(valueChangeEvent -> {
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
