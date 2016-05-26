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
package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.PreviewBeanForm;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class ProjectPreviewFormControlsGenerator<T> {
    public static final int EDIT_BTN_PRESENTED = 2;
    public static final int DELETE_BTN_PRESENTED = 4;
    public static final int CLONE_BTN_PRESENTED = 8;
    public static final int ASSIGN_BTN_PRESENTED = 16;

    private PreviewBeanForm<T> previewForm;

    private MVerticalLayout editButtons;

    public ProjectPreviewFormControlsGenerator(final PreviewBeanForm<T> editForm) {
        this.previewForm = editForm;
        editButtons = new MVerticalLayout().withFullWidth();
    }

    public void insertToControlBlock(Component button) {
        editButtons.addComponent(button, 0);
    }

    public VerticalLayout createButtonControls(final String permissionItem) {
        return createButtonControls(EDIT_BTN_PRESENTED | DELETE_BTN_PRESENTED | CLONE_BTN_PRESENTED, permissionItem);
    }

    public VerticalLayout createButtonControls(int buttonEnableFlags, final String permissionItem) {
        boolean canWrite = true;
        boolean canAccess = true;
        if (permissionItem != null) {
            canWrite = AppContext.canWrite(permissionItem);
            canAccess = AppContext.canAccess(permissionItem);
        }

        if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
            Button editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    T item = previewForm.getBean();
                    previewForm.fireEditForm(item);
                }
            });
            editButtons.addComponent(editBtn);
            editBtn.setEnabled(canWrite);
        }

        if ((buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
            Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    T item = previewForm.getBean();
                    previewForm.fireDeleteForm(item);
                }
            });
            editButtons.addComponent(deleteBtn);
            deleteBtn.setEnabled(canAccess);
        }

        if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
            Button cloneBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLONE), new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    T item = previewForm.getBean();
                    previewForm.fireCloneForm(item);
                }
            });
            editButtons.addComponent(cloneBtn);
            cloneBtn.setEnabled(canWrite);
        }

        if ((buttonEnableFlags & ASSIGN_BTN_PRESENTED) == ASSIGN_BTN_PRESENTED) {
            Button assignBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN), new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    T item = previewForm.getBean();
                    previewForm.fireAssignForm(item);
                }
            });
            editButtons.addComponent(assignBtn);
            editButtons.setEnabled(canWrite);
        }

        return editButtons;
    }
}
