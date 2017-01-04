/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.view.settings.ProjectMemberListSelect;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.*;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
class ResolvedInputView extends AbstractMobilePageView {
    private static final long serialVersionUID = 1L;

    private final SimpleBug bug;
    private final EditForm editForm;
    private final BugReadView callbackForm;

    ResolvedInputView(final BugReadView callbackForm, final SimpleBug bug) {
        this.setCaption(UserUIContext.getMessage(BugI18nEnum.OPT_RESOLVE_BUG, bug.getName()));
        this.bug = bug;
        this.callbackForm = callbackForm;

        this.editForm = new EditForm();
        this.editForm.setBean(bug);
        constructUI();
    }

    private void constructUI() {
        final Button resolvedBtn = new Button(UserUIContext.getMessage(BugI18nEnum.BUTTON_RESOLVED), clickEvent -> {
            if (editForm.validateForm()) {
                ResolvedInputView.this.bug.setStatus(BugStatus.Resolved.name());

                // Save bug status and assignee
                final BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                bugService.updateSelectiveWithSession(ResolvedInputView.this.bug, UserUIContext.getUsername());

                // Save comment
                final String commentValue = editForm.commentArea.getValue();
                if (StringUtils.isNotBlank(commentValue)) {
                    final CommentWithBLOBs comment = new CommentWithBLOBs();
                    comment.setComment(commentValue);
                    comment.setCreatedtime(new GregorianCalendar().getTime());
                    comment.setCreateduser(UserUIContext.getUsername());
                    comment.setSaccountid(MyCollabUI.getAccountId());
                    comment.setType(ProjectTypeConstants.BUG);
                    comment.setTypeid("" + bug.getId());
                    comment.setExtratypeid(CurrentProjectVariables.getProjectId());

                    final CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                    commentService.saveWithSession(comment, UserUIContext.getUsername());
                }
                callbackForm.previewItem(bug);
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }
        });
        this.setRightComponent(resolvedBtn);
        this.setContent(this.editForm);
    }

    private class EditForm extends AdvancedEditBeanForm<BugWithBLOBs> {
        private static final long serialVersionUID = 1L;
        private TextArea commentArea;

        @Override
        public void setBean(final BugWithBLOBs newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(EditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;
            private GridFormLayoutHelper informationLayout;

            @Override
            public AbstractComponent getLayout() {
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);
                return informationLayout.getLayout();
            }

            @Override
            public Component onAttachField(Object propertyId, final Field<?> field) {
                if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_RESOLUTION), 0, 0);
                } else if (BugWithBLOBs.Field.assignuser.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 1);
                } else if (propertyId.equals("comment")) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.OPT_COMMENT), 0, 2);
                }
                return null;
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<BugWithBLOBs> {
            private static final long serialVersionUID = 1L;

            EditFormFieldFactory(GenericBeanForm<BugWithBLOBs> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                if (BugWithBLOBs.Field.resolution.equalTo(propertyId)) {
                    bug.setResolution(BugResolution.Fixed.name());
                    return BugResolutionListSelect.getInstanceForResolvedBugWindow();
                } else if (BugWithBLOBs.Field.assignuser.equalTo(propertyId)) {
                    bug.setAssignuser(bug.getCreateduser());
                    return new ProjectMemberListSelect();
                } else if (propertyId.equals("comment")) {
                    commentArea = new TextArea();
                    commentArea.setNullRepresentation("");
                    return commentArea;
                }

                return null;
            }
        }
    }
}