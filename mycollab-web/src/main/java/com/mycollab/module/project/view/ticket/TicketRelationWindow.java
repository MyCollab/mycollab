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
package com.mycollab.module.project.view.ticket;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.TicketRelation;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum.TicketRel;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.TicketRelationService;
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
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class TicketRelationWindow extends MWindow {
    private RelatedBugEditForm editForm;
    private TicketRelationSelectField ticketRelationSelectField;
    private ProjectTicket hostedTicket;
    private TicketRelation ticketRelation;

    public TicketRelationWindow(ProjectTicket ticket) {
        super(UserUIContext.getMessage(TicketI18nEnum.OPT_DEPENDENCIES));
        this.hostedTicket = ticket;
        MVerticalLayout contentLayout = new MVerticalLayout().withMargin(false).withFullWidth();

        editForm = new RelatedBugEditForm();
        ticketRelation = new TicketRelation();
        ticketRelation.setTicketid(ticket.getTypeId());
        ticketRelation.setTickettype(ticket.getType());
        ticketRelation.setRel(TicketRel.Duplicated.name());
        editForm.setBean(ticketRelation);
        contentLayout.add(editForm);

        this.withWidth("750px").withModal(true).withResizable(false).withContent(contentLayout).withCenter();
    }

    private class RelatedBugEditForm extends AdvancedEditBeanForm<TicketRelation> {
        @Override
        public void setBean(TicketRelation newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new EditFormFieldFactory(RelatedBugEditForm.this));
            super.setBean(newDataSource);
        }

        class FormLayoutFactory extends AbstractFormLayoutFactory {
            private GridFormLayoutHelper informationLayout;

            @Override
            public AbstractComponent getLayout() {
                MVerticalLayout layout = new MVerticalLayout();
                informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);
                layout.addComponent(informationLayout.getLayout());

                MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                    ProjectTicket relatedTicket = ticketRelationSelectField.getSelectedTicket();
                    ticketRelation.setType(relatedTicket.getType());
                    ticketRelation.setTypeid(relatedTicket.getTypeId());

                    if (editForm.validateForm()) {
                        TicketRelationService relatedBugService = AppContextUtil.getSpringBean(TicketRelationService.class);

                        ProjectTicket relationTicket = ticketRelationSelectField.getSelectedTicket();
                        if (relationTicket == null) {
                            throw new UserInvalidInputException("The related ticket must be not null");
                        }

                        if (relationTicket.getTypeId().equals(hostedTicket.getTypeId()) && relationTicket.getType().equals(hostedTicket.getType())) {
                            throw new UserInvalidInputException("The relation is invalid since the both entries are the same");
                        }

                        ticketRelation.setTypeid(relationTicket.getTypeId());
                        ticketRelation.setType(relationTicket.getType());
                        relatedBugService.saveWithSession(ticketRelation, UserUIContext.getUsername());
                        close();

                        EventBusFactory.getInstance().post(new TicketEvent.DependencyChange(this, hostedTicket.getTypeId()));
                    }
                }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION);

                MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                        .withStyleName(WebThemes.BUTTON_OPTION);

                MHorizontalLayout controlsBtn = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(false);
                layout.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
                return layout;
            }

            @Override
            protected HasValue<?> onAttachField(Object propertyId, HasValue<?> field) {
                if (TicketRelation.Field.rel.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, "This ticket", 0, 0);
                } else if (TicketRelation.Field.typeid.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(TicketI18nEnum.SINGLE), 0, 1);
                } else if (TicketRelation.Field.comment.equalTo(propertyId)) {
                    return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.OPT_COMMENT), 0, 2);
                }
                return null;
            }
        }

        private class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<TicketRelation> {
            EditFormFieldFactory(GenericBeanForm<TicketRelation> form) {
                super(form);
            }

            @Override
            protected HasValue<?> onCreateField(Object propertyId) {
                if (TicketRelation.Field.rel.equalTo(propertyId)) {
                    I18nValueComboBox<TicketRel> relationSelection = new I18nValueComboBox<>(TicketRel.class,
                            TicketRel.Block, TicketRel.Duplicated, TicketRel.DependsOn, TicketRel.Relation, TicketRel.Duplicate);
                    relationSelection.setWidth(WebThemes.FORM_CONTROL_WIDTH);
                    return relationSelection;
                } else if (TicketRelation.Field.typeid.equalTo(propertyId)) {
                    ticketRelationSelectField = new TicketRelationSelectField();
                    return ticketRelationSelectField;
                } else if (TicketRelation.Field.comment.equalTo(propertyId)) {
                    return new RichTextArea();
                }
                return null;
            }
        }
    }
}
