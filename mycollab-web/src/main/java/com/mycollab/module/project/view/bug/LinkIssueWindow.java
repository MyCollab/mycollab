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
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.tracker.domain.RelatedBug;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelationService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class LinkIssueWindow extends MWindow {
    private RelatedBugEditForm editForm;
    private BugSelectionField bugSelectionField;
    private SimpleBug hostedBug;
    private RelatedBug relatedBug;

    public LinkIssueWindow(SimpleBug bug) {
        super("Dependencies");
        this.hostedBug = bug;
        MVerticalLayout contentLayout = new MVerticalLayout().withMargin(false).withFullWidth();

        editForm = new RelatedBugEditForm();
        relatedBug = new RelatedBug();
        relatedBug.setBugid(bug.getId());
        relatedBug.setRelatetype(OptionI18nEnum.BugRelation.Duplicated.name());
        editForm.setBean(relatedBug);
        contentLayout.add(editForm);

        this.withWidth("750px").withModal(true).withResizable(false).withContent(contentLayout).withCenter();
    }

    private class RelatedBugEditForm extends AdvancedEditBeanForm<RelatedBug> {
        @Override
        public void setBean(RelatedBug newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(RelatedBugEditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private GridFormLayoutHelper informationLayout;

            @Override
            public AbstractComponent getLayout() {
                final VerticalLayout layout = new VerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);
                layout.addComponent(informationLayout.getLayout());

                MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                    if (editForm.validateForm()) {
                        BugRelationService relatedBugService = AppContextUtil.getSpringBean(BugRelationService.class);

                        SimpleBug selectedBug = bugSelectionField.getSelectedBug();
                        if (selectedBug == null) {
                            throw new UserInvalidInputException("The related bug must be not null");
                        }

                        if (selectedBug.getId().equals(hostedBug.getId())) {
                            throw new UserInvalidInputException("The relation is invalid since the both entries are the same");
                        }

                        relatedBug.setRelatedid(selectedBug.getId());
                        relatedBugService.saveWithSession(relatedBug, UserUIContext.getUsername());
                        close();
                        EventBusFactory.getInstance().post(new BugEvent.BugChanged(this, hostedBug.getId()));
                    }
                }).withIcon(FontAwesome.SAVE).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);

                MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                        .withStyleName(WebThemes.BUTTON_OPTION);

                final MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(new MarginInfo(true, true, true, false));
                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, "This bug", 0, 0);
                } else if (RelatedBug.Field.relatedid.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, "Bug", 0, 1);
                } else if (RelatedBug.Field.comment.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, "Comment", 0, 2);
                }
                return null;
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<RelatedBug> {
            EditFormFieldFactory(GenericBeanForm<RelatedBug> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    return new BugRelationComboBox();
                } else if (RelatedBug.Field.relatedid.equalTo(propertyId)) {
                    bugSelectionField = new BugSelectionField();
                    return bugSelectionField;
                } else if (RelatedBug.Field.comment.equalTo(propertyId)) {
                    return new RichTextArea();
                }
                return null;
            }
        }
    }
}
