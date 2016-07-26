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
package com.mycollab.vaadin.web.ui.utils;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
public class FormControlsGenerator {
    public static final <T> ComponentContainer generateEditFormControls(final AdvancedEditBeanForm<T> editForm) {
        return generateEditFormControls(editForm, true, true, true);
    }

    public static final <T> ComponentContainer generateEditFormControls(final AdvancedEditBeanForm<T> editForm, boolean
            isSaveBtnVisible, boolean isSaveAndNewBtnVisible, boolean isCancelBtnVisible) {
        MHorizontalLayout layout = new MHorizontalLayout();

        if (isCancelBtnVisible) {
            MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> editForm.fireCancelForm())
                    .withIcon(FontAwesome.MINUS).withStyleName(WebUIConstants.BUTTON_OPTION);
            layout.addComponent(cancelBtn);
        }

        if (isSaveAndNewBtnVisible) {
            MButton saveAndNewBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_NEW), clickEvent -> {
                if (editForm.validateForm()) {
                    editForm.fireSaveAndNewForm();
                }
            }).withIcon(FontAwesome.SHARE_ALT).withStyleName(WebUIConstants.BUTTON_ACTION);
            layout.addComponent(saveAndNewBtn);
        }

        if (isSaveBtnVisible) {
            final MButton saveBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
                if (editForm.validateForm()) {
                    editForm.fireSaveForm();
                }
            }).withIcon(FontAwesome.SAVE).withStyleName(WebUIConstants.BUTTON_ACTION);
            layout.addComponent(saveBtn);
        }

        return layout;
    }
}
