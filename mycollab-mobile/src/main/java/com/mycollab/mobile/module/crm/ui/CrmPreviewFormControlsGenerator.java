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
package com.mycollab.mobile.module.crm.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmPreviewFormControlsGenerator<T> {
    public static final int EDIT_BTN_PRESENTED = 2;
    public static final int DELETE_BTN_PRESENTED = 4;
    public static final int CLONE_BTN_PRESENTED = 8;

    private AdvancedPreviewBeanForm<T> previewForm;

    private MVerticalLayout editButtons;

    public CrmPreviewFormControlsGenerator(final AdvancedPreviewBeanForm<T> editForm) {
        this.previewForm = editForm;
        editButtons = new MVerticalLayout().withFullWidth();
    }

    public void insertToControlBlock(Button button) {
        editButtons.addComponent(button, 0);
    }

    public VerticalLayout createButtonControls(final String permissionItem) {
        return createButtonControls(EDIT_BTN_PRESENTED | DELETE_BTN_PRESENTED
                | CLONE_BTN_PRESENTED, permissionItem);
    }

    public VerticalLayout createButtonControls(int buttonEnableFlags, final String permissionItem) {
        boolean canWrite = true;
        boolean canAccess = true;
        if (permissionItem != null) {
            canWrite = UserUIContext.canWrite(permissionItem);
            canAccess = UserUIContext.canAccess(permissionItem);
        }

        if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
            MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
                final T item = previewForm.getBean();
                previewForm.fireEditForm(item);
            }).withVisible(canWrite);
            editButtons.addComponent(editBtn);
        }

        if ((buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
            Button deleteBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                final T item = previewForm.getBean();
                previewForm.fireDeleteForm(item);
            });
            editButtons.addComponent(deleteBtn);
            deleteBtn.setEnabled(canAccess);
        }

        if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
            Button cloneBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLONE), clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireCloneForm(item);
            });
            editButtons.addComponent(cloneBtn);
            cloneBtn.setEnabled(canWrite);
        }

        return editButtons;
    }
}
