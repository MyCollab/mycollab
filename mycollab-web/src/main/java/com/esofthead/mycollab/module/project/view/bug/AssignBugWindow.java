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
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextEditField;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.ComponentContainer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class AssignBugWindow extends Window {
    private static final long serialVersionUID = 1L;
    private final SimpleBug bug;
    private final IBugCallbackStatusComp callbackForm;

    AssignBugWindow(IBugCallbackStatusComp callbackForm, SimpleBug bug) {
        super("Assign bug '" + bug.getSummary() + "'");
        this.setWidth("750px");
        this.setResizable(false);
        this.setModal(true);

        this.bug = bug;
        this.callbackForm = callbackForm;

        VerticalLayout contentLayout = new VerticalLayout();

        EditForm editForm = new EditForm();
        contentLayout.addComponent(editForm);
        editForm.setBean(bug);
        contentLayout.setMargin(new MarginInfo(false, false, true, false));

        this.setContent(contentLayout);
        this.center();
    }

    private class EditForm extends AdvancedEditBeanForm<BugWithBLOBs> {
        private static final long serialVersionUID = 1L;
        private RichTextEditField commentArea;

        @Override
        public void setBean(final BugWithBLOBs item) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
            super.setBean(item);
        }

        class FormLayoutFactory implements IFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public ComponentContainer getLayout() {
                final VerticalLayout layout = new VerticalLayout();
                this.informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 2);

                layout.addComponent(this.informationLayout.getLayout());
                MHorizontalLayout controlsBtn = new MHorizontalLayout().withMargin(new MarginInfo(true, true, false, false));

                layout.addComponent(controlsBtn);

                final Button approveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {

                        if (EditForm.this.validateForm()) {
                            // Save bug status and assignee
                            final BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                            bugService.updateSelectiveWithSession(AssignBugWindow.this.bug, AppContext.getUsername());

                            // Save comment
                            final String commentValue = commentArea.getValue();
                            if (StringUtils.isNotBlank(commentValue)) {
                                final CommentWithBLOBs comment = new CommentWithBLOBs();
                                comment.setComment(Jsoup.clean(commentValue, Whitelist.relaxed()));
                                comment.setCreatedtime(new GregorianCalendar().getTime());
                                comment.setCreateduser(AppContext.getUsername());
                                comment.setSaccountid(AppContext.getAccountId());
                                comment.setType(ProjectTypeConstants.BUG);
                                comment.setTypeid("" + bug.getId());
                                comment.setExtratypeid(CurrentProjectVariables.getProjectId());

                                CommentService commentService = ApplicationContextUtil.getSpringBean(CommentService.class);
                                commentService.saveWithSession(comment, AppContext.getUsername());
                            }

                            AssignBugWindow.this.close();
                            AssignBugWindow.this.callbackForm.refreshBugItem();
                        }
                    }
                });
                approveBtn.setStyleName(UIConstants.BUTTON_ACTION);
                approveBtn.setIcon(FontAwesome.SHARE);
                approveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

                Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        AssignBugWindow.this.close();
                    }
                });
                cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
                controlsBtn.with(cancelBtn, approveBtn);

                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);

                return layout;
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("assignuser")) {
                    this.informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 0);
                } else if (propertyId.equals("comment")) {
                    this.informationLayout.addComponent(field, "Comment", 0, 1, 2, "100%");
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
                if (propertyId.equals("assignuser")) {
                    return new ProjectMemberSelectionField();
                } else if (propertyId.equals("comment")) {
                    commentArea = new RichTextEditField();
                    return commentArea;
                }

                return null;
            }
        }
    }
}
