/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.event.HasEditFormHandlers;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic attachForm with java bean as datasource. It includes validation
 * against bean input
 *
 * @param <B> java bean as datasource map with attachForm fields
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AdvancedEditBeanForm<B> extends GenericBeanForm<B> implements HasEditFormHandlers<B> {
    private static final long serialVersionUID = 1L;

    private List<IEditFormHandler<B>> editFormHandlers;

    /**
     * Validate attachForm against data
     *
     * @return true if data is valid, otherwise return false and show result to
     * attachForm
     */
    public boolean validateForm() {
        fieldFactory.commit();
        return isValid();
    }

    @Override
    public void addFormHandler(IEditFormHandler<B> editFormHandler) {
        if (editFormHandlers == null) {
            editFormHandlers = new ArrayList<>();
        }

        editFormHandlers.add(editFormHandler);
    }

    public void fireSaveForm() {
        if (editFormHandlers != null) {
            for (IEditFormHandler<B> editFormHandler : editFormHandlers) {
                editFormHandler.onSave(this.getBean());
            }
        }
    }

    public void fireSaveAndNewForm() {
        if (editFormHandlers != null) {
            for (IEditFormHandler<B> editFormHandler : editFormHandlers) {
                editFormHandler.onSaveAndNew(this.getBean());
            }
        }
    }

    public void fireCancelForm() {
        if (editFormHandlers != null) {
            for (IEditFormHandler<B> editFormHandler : editFormHandlers) {
                editFormHandler.onCancel();
            }
        }
    }
}
