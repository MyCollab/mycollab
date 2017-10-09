package com.mycollab.mobile.module.project.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.PreviewBeanForm;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Button;
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
            canWrite = UserUIContext.canWrite(permissionItem);
            canAccess = UserUIContext.canAccess(permissionItem);
        }

        if (canWrite && (buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
            Button editBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireEditForm(item);
            });
            editButtons.addComponent(editBtn);
        }

        if (canAccess && (buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
            Button deleteBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireDeleteForm(item);
            });
            editButtons.addComponent(deleteBtn);
        }

        if (canWrite && (buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
            Button cloneBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLONE), clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireCloneForm(item);
            });
            editButtons.addComponent(cloneBtn);
        }

        if (canWrite && (buttonEnableFlags & ASSIGN_BTN_PRESENTED) == ASSIGN_BTN_PRESENTED) {
            Button assignBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_ASSIGN), clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireAssignForm(item);
            });
            editButtons.addComponent(assignBtn);
        }

        return editButtons;
    }
}
