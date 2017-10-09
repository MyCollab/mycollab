package com.mycollab.vaadin.web.ui.utils;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
public class FormControlsGenerator {
    public static <T> ComponentContainer generateEditFormControls(final AdvancedEditBeanForm<T> editForm) {
        return generateEditFormControls(editForm, true, true, true);
    }

    public static <T> ComponentContainer generateEditFormControls(final AdvancedEditBeanForm<T> editForm, boolean
            isSaveBtnVisible, boolean isSaveAndNewBtnVisible, boolean isCancelBtnVisible) {
        MHorizontalLayout layout = new MHorizontalLayout();

        if (isCancelBtnVisible) {
            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> editForm.fireCancelForm())
                    .withStyleName(WebThemes.BUTTON_OPTION);
            layout.addComponent(cancelBtn);
        }

        if (isSaveAndNewBtnVisible) {
            MButton saveAndNewBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE_NEW), clickEvent -> {
                if (editForm.validateForm()) {
                    editForm.fireSaveAndNewForm();
                }
            }).withIcon(FontAwesome.SHARE_ALT).withStyleName(WebThemes.BUTTON_ACTION);
            layout.addComponent(saveAndNewBtn);
        }

        if (isSaveBtnVisible) {
            final MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                if (editForm.validateForm()) {
                    editForm.fireSaveForm();
                }
            }).withIcon(FontAwesome.SAVE).withStyleName(WebThemes.BUTTON_ACTION);
            layout.addComponent(saveBtn);
        }

        return layout;
    }
}
