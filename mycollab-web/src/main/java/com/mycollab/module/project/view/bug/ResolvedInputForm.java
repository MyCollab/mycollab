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

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.module.project.view.settings.component.VersionMultiSelectField;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.RelatedBug;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelatedItemService;
import com.mycollab.module.tracker.service.BugRelationService;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDateTime;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 4.5.5
 */
public class ResolvedInputForm extends AdvancedEditBeanForm<SimpleBug> {
    private static final long serialVersionUID = 1L;
    private SimpleBug bug;
    private RichTextArea commentArea;
    private BugSelectionField bugSelectionField;
    private VersionMultiSelectField fixedVersionSelect;

    ResolvedInputForm(SimpleBug bugValue) {
        setBean(bugValue);
    }

    @Override
    public void setBean(SimpleBug bug) {
        this.bug = bug;
        this.setFormLayoutFactory(new FormLayoutFactory());
        this.setBeanFormFieldFactory(new EditFormFieldFactory(this));
        super.setBean(bug);
    }

    protected void postExecution() {
    }

    class FormLayoutFactory extends AbstractFormLayoutFactory {
        private GridFormLayoutHelper informationLayout;

        @Override
        public AbstractComponent getLayout() {
            MVerticalLayout layout = new MVerticalLayout();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.TWO_COLUMN);
            layout.addComponent(informationLayout.getLayout());

            MButton resolveBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.BUTTON_RESOLVED), clickEvent -> {
                if (validateForm()) {
                    String commentValue = commentArea.getValue();
                    if (BugResolution.Duplicate.name().equals(bug.getResolution())) {
                        if (bugSelectionField != null && bugSelectionField.getSelectedBug() != null) {
                            SimpleBug selectedBug = bugSelectionField.getSelectedBug();
                            if (selectedBug.getId().equals(bug.getId())) {
                                throw new UserInvalidInputException("The relation is invalid since the both entries are the same");
                            }
                            BugRelationService relatedBugService = AppContextUtil.getSpringBean(BugRelationService.class);
                            RelatedBug relatedBug = new RelatedBug();
                            relatedBug.setBugid(bug.getId());
                            relatedBug.setRelatetype(OptionI18nEnum.BugRelation.Duplicated.name());
                            relatedBug.setRelatedid(selectedBug.getId());
                            relatedBugService.saveWithSession(relatedBug, UserUIContext.getUsername());
                        } else {
                            NotificationUtil.showErrorNotification(UserUIContext.getMessage(BugI18nEnum.ERROR_DUPLICATE_BUG_SELECT));
                            return;
                        }
                    } else if (BugResolution.InComplete.name().equals(bug.getResolution()) ||
                            BugResolution.CannotReproduce.name().equals(bug.getResolution()) ||
                            BugResolution.Invalid.name().equals(bug.getResolution())) {
                        if (StringUtils.isBlank(commentValue)) {
                            NotificationUtil.showErrorNotification(UserUIContext.getMessage(BugI18nEnum.ERROR_COMMENT_NOT_BLANK_FOR_RESOLUTION, bug.getResolution()));
                            return;
                        }
                    }
                    bug.setStatus(StatusI18nEnum.Resolved.name());

                    BugRelatedItemService bugRelatedItemService = AppContextUtil.getSpringBean(BugRelatedItemService.class);
                    bugRelatedItemService.updateFixedVersionsOfBug(bug.getId(), fixedVersionSelect.getSelectedItems());

                    // Save bug status and assignee
                    BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                    bugService.updateSelectiveWithSession(bug, UserUIContext.getUsername());

                    // Save comment
                    if (StringUtils.isNotBlank(commentValue)) {
                        CommentWithBLOBs comment = new CommentWithBLOBs();
                        comment.setComment(commentValue);
                        comment.setCreatedtime(LocalDateTime.now());
                        comment.setCreateduser(UserUIContext.getUsername());
                        comment.setSaccountid(AppUI.getAccountId());
                        comment.setType(ProjectTypeConstants.BUG);
                        comment.setTypeid("" + bug.getId());
                        comment.setExtratypeid(CurrentProjectVariables.getProjectId());

                        CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                        commentService.saveWithSession(comment, UserUIContext.getUsername());
                    }

                    postExecution();
                    EventBusFactory.getInstance().post(new BugEvent.BugChanged(this, bug.getId()));
                }
            }).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(KeyCode.ENTER);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> postExecution())
                    .withStyleName(WebThemes.BUTTON_OPTION);

            final MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, resolveBtn);
            layout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);

            return layout;
        }

        @Override
        protected HasValue<?> onAttachField(Object propertyId, HasValue<?> field) {
            if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_RESOLUTION),
                        UserUIContext.getMessage(BugI18nEnum.FORM_RESOLUTION_HELP), 0, 0);
            } else if (propertyId.equals("assignuser")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 1);
            } else if (propertyId.equals("fixedVersions")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS),
                        UserUIContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS_HELP), 0, 2);
            } else if (propertyId.equals("comment")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.OPT_COMMENT), 0, 3, 2);
            }
            return null;
        }
    }

    private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleBug> {
        private static final long serialVersionUID = 1L;

        EditFormFieldFactory(GenericBeanForm<SimpleBug> form) {
            super(form);
        }

        @Override
        protected HasValue<?> onCreateField(final Object propertyId) {
            if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                if (StringUtils.isBlank(bean.getResolution()) || UserUIContext.getMessage(BugResolution.None).equals(bug.getResolution())) {
                    bean.setResolution(BugResolution.Fixed.name());
                }
                return new ResolutionField();
            } else if (BugWithBLOBs.Field.assignuser.equalTo(propertyId)) {
                bug.setAssignuser(bug.getCreateduser());
                return new ProjectMemberSelectionField();
            } else if (propertyId.equals("fixedVersions")) {
                fixedVersionSelect = new VersionMultiSelectField();
                if (CollectionUtils.isEmpty(bug.getFixedVersions()) && CollectionUtils.isNotEmpty(bug.getAffectedVersions())) {
                    bug.setFixedVersions(bug.getAffectedVersions());
                }
                return fixedVersionSelect;
            } else if (propertyId.equals("comment")) {
                commentArea = new RichTextArea();
                return commentArea;
            }

            return null;
        }

        private class ResolutionField extends CustomField<String> {
            private MHorizontalLayout layout;
            private BugResolutionComboBox resolutionComboBox;

            ResolutionField() {
                resolutionComboBox = BugResolutionComboBox.getInstanceForResolvedBugWindow();
                resolutionComboBox.setValueByString(bean.getResolution());
            }

            @Override
            protected Component initContent() {
                layout = new MHorizontalLayout(resolutionComboBox);
                resolutionComboBox.addValueChangeListener(valueChangeEvent -> {
                    BugResolution value = resolutionComboBox.getValue();
                    if (BugResolution.Duplicate == value) {
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
            protected void doSetValue(String value) {
                resolutionComboBox.setValueByString(value);
            }

            @Override
            public String getValue() {
                return resolutionComboBox.getValue().name();
            }
        }
    }
}
