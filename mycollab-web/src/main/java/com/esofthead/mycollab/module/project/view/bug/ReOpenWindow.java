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
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.project.view.settings.component.VersionMultiSelectField;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugRelatedItemService;
import com.esofthead.mycollab.module.tracker.service.BugRelationService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ReOpenWindow extends Window {
    private final SimpleBug bug;
    private VersionMultiSelectField affectedVersionsSelect;

    public ReOpenWindow(final SimpleBug bugValue) {
        super("Reopen bug '" + bugValue.getSummary() + "'");
        this.bug = BeanUtility.deepClone(bugValue);
        this.setResizable(false);
        this.setModal(true);
        this.setWidth("800px");

        MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, false, true, false));
        EditForm editForm = new EditForm();
        editForm.setBean(bug);
        contentLayout.addComponent(editForm);

        this.setContent(contentLayout);
        this.center();
    }

    private class EditForm extends AdvancedEditBeanForm<SimpleBug> {
        private static final long serialVersionUID = 1L;
        private RichTextArea commentArea;

        @Override
        public void setBean(final SimpleBug newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);

                layout.addComponent(informationLayout.getLayout());

                MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false));
                layout.addComponent(controlsBtn);

                Button reOpenBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_REOPEN), new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        if (EditForm.this.validateForm()) {
                            bug.setStatus(BugStatus.ReOpen.name());
                            bug.setResolution(OptionI18nEnum.BugResolution.None.name());

                            // Save bug status and assignee
                            BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                            bugService.updateSelectiveWithSession(bug, AppContext.getUsername());

                            BugRelatedItemService bugRelatedItemService = AppContextUtil.getSpringBean(BugRelatedItemService.class);
                            bugRelatedItemService.updateAffectedVersionsOfBug(bug.getId(), affectedVersionsSelect.getSelectedItems());
                            bugRelatedItemService.updateFixedVersionsOfBug(bug.getId(), null);

                            BugRelationService bugRelationService = AppContextUtil.getSpringBean(BugRelationService.class);
                            bugRelationService.removeDuplicatedBugs(bug.getId());

                            // Save comment
                            String commentValue = commentArea.getValue();
                            if (StringUtils.isNotBlank(commentValue)) {
                                CommentWithBLOBs comment = new CommentWithBLOBs();
                                comment.setComment(Jsoup.clean(commentValue, Whitelist.relaxed()));
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

                    }
                });
                reOpenBtn.setStyleName(UIConstants.BUTTON_ACTION);
                reOpenBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        close();
                    }
                });
                cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);
                controlsBtn.with(cancelBtn, reOpenBtn);

                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("assignuser")) {
                    return informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 0);
                } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS),
                            AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP), 1, 0);
                } else if (propertyId.equals("comment")) {
                    return informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_COMMENT), 0, 1, 2, "100%");
                }
                return null;
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<SimpleBug> {
            private static final long serialVersionUID = 1L;

            public EditFormFieldFactory(GenericBeanForm<SimpleBug> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (propertyId.equals("assignuser")) {
                    return new ProjectMemberSelectionField();
                } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                    affectedVersionsSelect = new VersionMultiSelectField();
                    return affectedVersionsSelect;
                } else if (propertyId.equals("comment")) {
                    commentArea = new RichTextArea();
                    return commentArea;
                }

                return null;
            }
        }
    }
}
