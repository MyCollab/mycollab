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
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.mycollab.module.project.view.settings.component.VersionMultiSelectField;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelatedItemService;
import com.mycollab.module.tracker.service.BugRelationService;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ReOpenWindow extends MWindow {
    private final SimpleBug bug;
    private VersionMultiSelectField affectedVersionsSelect;

    public ReOpenWindow(final SimpleBug bugValue) {
        super(UserUIContext.getMessage(BugI18nEnum.OPT_REOPEN_BUG, bugValue.getName()));
        this.bug = BeanUtility.deepClone(bugValue);

        MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, false, true, false));
        EditForm editForm = new EditForm();
        editForm.setBean(bug);
        contentLayout.addComponent(editForm);

        withWidth("800px").withModal(true).withResizable(false).withContent(contentLayout).withCenter();
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
            public AbstractComponent getLayout() {
                VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
                layout.addComponent(informationLayout.getLayout());

                MButton reOpenBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN), clickEvent -> {
                    if (EditForm.this.validateForm()) {
                        bug.setStatus(BugStatus.ReOpen.name());
                        bug.setResolution(BugResolution.None.name());

                        // Save bug status and assignee
                        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                        bugService.updateSelectiveWithSession(bug, UserUIContext.getUsername());

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
                            comment.setCreateduser(UserUIContext.getUsername());
                            comment.setSaccountid(MyCollabUI.getAccountId());
                            comment.setType(ProjectTypeConstants.BUG);
                            comment.setTypeid("" + bug.getId());
                            comment.setExtratypeid(CurrentProjectVariables.getProjectId());

                            CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                            commentService.saveWithSession(comment, UserUIContext.getUsername());
                        }

                        close();
                        EventBusFactory.getInstance().post(new BugEvent.BugChanged(this, bug.getId()));
                    }
                }).withStyleName(WebThemes.BUTTON_ACTION);
                reOpenBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

                MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                        .withStyleName(WebThemes.BUTTON_OPTION);

                MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, reOpenBtn).withMargin(new MarginInfo(true, true, true, false));

                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("assignuser")) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 0);
                } else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS),
                            UserUIContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS_HELP), 0, 1);
                } else if (propertyId.equals("comment")) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.OPT_COMMENT), 0, 2, 2, "100%");
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
