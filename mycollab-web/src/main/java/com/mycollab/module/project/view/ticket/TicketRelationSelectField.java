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

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class TicketRelationSelectField extends CustomField<Integer> implements FieldSelection<ProjectTicket> {
    private ProjectTicket selectedTicket;
    private TextField ticketField;

    public ProjectTicket getSelectedTicket() {
        return selectedTicket;
    }

    @Override
    protected Component initContent() {
        ticketField = new MTextField().withReadOnly(true).withWidth(WebThemes.FORM_CONTROL_WIDTH);
        MHorizontalLayout layout = new MHorizontalLayout();
        MButton browseBtn = new MButton(VaadinIcons.ELLIPSIS_H)
                .withListener(clickEvent -> UI.getCurrent().addWindow(new TicketSelectionWindow(TicketRelationSelectField.this)))
                .withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);
        layout.with(ticketField, browseBtn);
        return layout;
    }

    @Override
    protected void doSetValue(Integer ticketId) {

    }

    @Override
    public Integer getValue() {
        return (selectedTicket != null) ? selectedTicket.getTypeId() : null;
    }

    @Override
    public void fireValueChange(ProjectTicket data) {
        selectedTicket = data;
        ticketField.setValue(selectedTicket.getName());
    }
}
