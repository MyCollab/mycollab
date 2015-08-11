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
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.view.bug.components.BugResolutionComboBox;
import com.esofthead.mycollab.module.project.view.bug.components.VersionMultiSelectField;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugRelatedItemService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
class WontFixExplainWindow extends Window {
    private final SimpleBug bug;
    private VersionMultiSelectField fixedVersionSelect;
    private final IBugCallbackStatusComp callbackForm;

    WontFixExplainWindow(IBugCallbackStatusComp callbackForm, SimpleBug bug) {
        super("Won't fix bug '" + bug.getSummary() + "'");
        this.bug = bug;
        this.callbackForm = callbackForm;
        this.setWidth("750px");
        this.setResizable(false);
        this.setModal(true);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setMargin(new MarginInfo(false, false, true, false));

        EditForm editForm = new EditForm();
        contentLayout.addComponent(editForm);
        editForm.setBean(bug);
        this.setContent(contentLayout);
        this.center();
    }

    private class EditForm extends AdvancedEditBeanForm<BugWithBLOBs> {
        private static final long serialVersionUID = 1L;
        private RichTextArea commentArea;

        @Override
        public void setBean(BugWithBLOBs newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory implements IFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
                layout.addComponent(informationLayout.getLayout());

                MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true, false, false));
                layout.addComponent(controlsBtn);

                final Button wonFixBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_WONT_FIX), new Button.ClickListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void buttonClick(ClickEvent event) {
                        if (EditForm.this.validateForm()) {
                            bug.setStatus(BugStatus.Resolved.name());

                            final String commentValue = commentArea.getValue();
                            if (commentValue != null && !commentValue.trim().equals("")) {
                                BugRelatedItemService bugRelatedItemService = ApplicationContextUtil.
                                        getSpringBean(BugRelatedItemService.class);
                                bugRelatedItemService.updateFixedVersionsOfBug(bug.getId(), fixedVersionSelect.getSelectedItems());

                                // Save bug status and assignee
                                BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                                bugService.updateSelectiveWithSession(bug, AppContext.getUsername());

                                // Save comment
                                final CommentWithBLOBs comment = new CommentWithBLOBs();
                                comment.setComment(commentValue);
                                comment.setCreatedtime(new GregorianCalendar().getTime());
                                comment.setCreateduser(AppContext.getUsername());
                                comment.setSaccountid(AppContext.getAccountId());
                                comment.setType(ProjectTypeConstants.BUG);
                                comment.setTypeid("" + bug.getId());
                                comment.setExtratypeid(CurrentProjectVariables.getProjectId());

                                CommentService commentService = ApplicationContextUtil.getSpringBean(CommentService.class);
                                commentService.saveWithSession(comment, AppContext.getUsername());
                                WontFixExplainWindow.this.close();
                                callbackForm.refreshBugItem();
                            } else {
                                NotificationUtil.showErrorNotification(AppContext.getMessage(BugI18nEnum.ERROR_WONT_FIX_EXPLAIN_REQUIRE_MSG));
                                return;
                            }

                            WontFixExplainWindow.this.close();
                        }
                    }
                });
                wonFixBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
                wonFixBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
                controlsBtn.with(wonFixBtn).withAlign(wonFixBtn, Alignment.MIDDLE_RIGHT);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        WontFixExplainWindow.this.close();
                    }
                });
                cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
                controlsBtn.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_RIGHT);

                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

                return layout;
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("resolution")) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION), 0, 0);
                } else if (propertyId.equals("assignuser")) {
                    informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 1);
                } else if (propertyId.equals("fixedVersions")) {
                    informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS), 0, 2);
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
                    return BugResolutionComboBox.getInstanceForWontFixWindow();
                } else if (propertyId.equals("assignuser")) {
                    return new ProjectMemberSelectionField();
                } else if (propertyId.equals("fixedVersions")) {
                    fixedVersionSelect = new VersionMultiSelectField();
                    return fixedVersionSelect;
                } else if (propertyId.equals("comment")) {
                    commentArea = new RichTextArea();
                    commentArea.setNullRepresentation("");
                    return commentArea;
                }

                return null;
            }
        }
    }
}
