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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugRelation;
import com.mycollab.module.tracker.domain.RelatedBug;
import com.mycollab.module.project.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelationService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class LinkIssueWindow extends MWindow {
    private RelatedBugEditForm editForm;
    private BugSelectionField bugSelectionField;
    private SimpleBug hostedBug;
    private RelatedBug relatedBug;

    LinkIssueWindow(SimpleBug bug) {
        super("Dependencies");
        this.hostedBug = bug;
        MVerticalLayout contentLayout = new MVerticalLayout().withMargin(false).withFullWidth();

        editForm = new RelatedBugEditForm();
        relatedBug = new RelatedBug();
        relatedBug.setBugid(bug.getId());
        relatedBug.setRelatetype(BugRelation.Duplicated.name());
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
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);
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
                }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION);

                MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                        .withStyleName(WebThemes.BUTTON_OPTION);

                final MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(false);
                layout.addComponent(controlsBtn);
                layout.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected HasValue<?> onAttachField(Object propertyId, HasValue<?> field) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, "This bug", 0, 0);
                } else if (RelatedBug.Field.relatedid.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(BugI18nEnum.SINGLE), 0, 1);
                } else if (RelatedBug.Field.comment.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.OPT_COMMENT), 0, 2);
                }
                return null;
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<RelatedBug> {
            EditFormFieldFactory(GenericBeanForm<RelatedBug> form) {
                super(form);
            }

            @Override
            protected HasValue<?> onCreateField(Object propertyId) {
                if (RelatedBug.Field.relatetype.equalTo(propertyId)) {
                    I18nValueComboBox<BugRelation> relationSelection = new I18nValueComboBox<>(BugRelation.class,
                            BugRelation.Block, BugRelation.Duplicated, BugRelation.Related);
                    relationSelection.setWidth(WebThemes.FORM_CONTROL_WIDTH);
                    return relationSelection;
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
