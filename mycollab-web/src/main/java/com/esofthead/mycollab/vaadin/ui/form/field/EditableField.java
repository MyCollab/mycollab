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
package com.esofthead.mycollab.vaadin.ui.form.field;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public abstract class EditableField<T> extends CustomField<T> {
    private CustomField<T> readField, editField;
    private CssLayout wrapper;
    private boolean isRead;

    public EditableField(final CustomField<T> readField, final CustomField<T> editField) {
        this.readField = readField;
        this.editField = editField;
        wrapper = new CssLayout(readField);
        isRead = true;
        wrapper.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
                isRead = !isRead;
                if (isRead) {
                    wrapper.removeComponent(editField);
                    wrapper.addComponent(readField);
                } else {
                    wrapper.removeComponent(readField);
                    wrapper.addComponent(editField);
                }
            }
        });
    }

    @Override
    protected Component initContent() {
        return wrapper;
    }

    @Override
    public Class<? extends T> getType() {
        return readField.getType();
    }
}
