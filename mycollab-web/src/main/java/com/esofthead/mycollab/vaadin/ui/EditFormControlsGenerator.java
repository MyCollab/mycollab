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
package com.esofthead.mycollab.vaadin.ui;

import java.io.Serializable;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class EditFormControlsGenerator<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final AdvancedEditBeanForm<T> editForm;

    public EditFormControlsGenerator(final AdvancedEditBeanForm<T> editForm) {
        this.editForm = editForm;
    }

    public HorizontalLayout createButtonControls() {
        return this.createButtonControls(true, true, true);
    }

    public HorizontalLayout createButtonControls(boolean isSaveBtnVisible, boolean isSaveAndNewBtnVisible,
                                                 boolean isCancelBtnVisible) {
        MHorizontalLayout layout = new MHorizontalLayout().withStyleName("addNewControl");
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        layout.setSizeUndefined();

        if (isSaveBtnVisible) {
            final Button saveBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            if (editForm.validateForm()) {
                                editForm.fireSaveForm();
                            }
                        }
                    });
            saveBtn.setIcon(FontAwesome.SAVE);
            saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            layout.addComponent(saveBtn);
        }

        if (isSaveAndNewBtnVisible) {
            Button saveAndNewBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_NEW),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            if (editForm.validateForm()) {
                                editForm.fireSaveAndNewForm();
                            }
                        }
                    });
            saveAndNewBtn.setIcon(FontAwesome.SHARE_ALT);
            saveAndNewBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            layout.addComponent(saveAndNewBtn);
        }

        if (isCancelBtnVisible) {
             Button cancelBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            editForm.fireCancelForm();
                        }
                    });
            cancelBtn.setIcon(FontAwesome.MINUS);
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            layout.addComponent(cancelBtn);
        }

        return layout;
    }
}
