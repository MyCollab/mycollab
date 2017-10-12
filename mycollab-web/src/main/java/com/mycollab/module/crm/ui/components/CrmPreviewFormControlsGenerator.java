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
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.reporting.PrintButton;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmPreviewFormControlsGenerator<T> {
    public static final int BACK_BTN_PRESENTED = 2;
    public static final int EDIT_BTN_PRESENTED = 4;
    public static final int DELETE_BTN_PRESENTED = 8;
    public static final int CLONE_BTN_PRESENTED = 16;
    public static final int NAVIGATOR_BTN_PRESENTED = 32;
    public static final int ADD_BTN_PRESENTED = 64;
    public static final int PRINT_BTN_PRESENTED = 128;

    private AdvancedPreviewBeanForm<T> previewForm;
    private PopupButton optionBtn;
    private MHorizontalLayout layout;

    public CrmPreviewFormControlsGenerator(AdvancedPreviewBeanForm<T> editForm) {
        this.previewForm = editForm;
        layout = new MHorizontalLayout();

        optionBtn = new PopupButton();
        optionBtn.addStyleName(WebThemes.BUTTON_OPTION);
        optionBtn.setIcon(FontAwesome.ELLIPSIS_H);
    }

    public void insertToControlBlock(Button button) {
        layout.addComponent(button, 0);
    }

    public HorizontalLayout createButtonControls(final String permissionItem) {
        return createButtonControls(EDIT_BTN_PRESENTED | DELETE_BTN_PRESENTED
                | CLONE_BTN_PRESENTED | PRINT_BTN_PRESENTED | NAVIGATOR_BTN_PRESENTED
                | ADD_BTN_PRESENTED, permissionItem);
    }

    public HorizontalLayout createButtonControls(int buttonEnableFlags, String permissionItem) {
        boolean canRead = false;
        boolean canWrite = false;
        boolean canAccess = false;
        if (permissionItem != null) {
            canRead = UserUIContext.canRead(permissionItem);
            canWrite = UserUIContext.canWrite(permissionItem);
            canAccess = UserUIContext.canAccess(permissionItem);
        }

        MHorizontalLayout editBtns = new MHorizontalLayout();
        layout.addComponent(editBtns);

        OptionPopupContent popupButtonsControl = new OptionPopupContent();

        if (canWrite && (buttonEnableFlags & ADD_BTN_PRESENTED) == ADD_BTN_PRESENTED) {
            MButton addBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                optionBtn.setPopupVisible(false);
                T item = previewForm.getBean();
                previewForm.fireAddForm(item);
            }).withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION);
            editBtns.addComponent(addBtn);
        }

        if (canWrite && (buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
            MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
                optionBtn.setPopupVisible(false);
                T item = previewForm.getBean();
                previewForm.fireEditForm(item);
            }).withIcon(FontAwesome.EDIT).withStyleName(WebThemes.BUTTON_ACTION);
            editBtns.addComponent(editBtn);
        }

        if (canAccess && (buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
            MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireDeleteForm(item);
            }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_DANGER);
            editBtns.addComponent(deleteBtn);
        }

        if (canRead && (buttonEnableFlags & PRINT_BTN_PRESENTED) == PRINT_BTN_PRESENTED) {
            final PrintButton printBtn = new PrintButton();
            printBtn.withListener(clickEvent -> {
                T item = previewForm.getBean();
                previewForm.firePrintForm(printBtn, item);
            }).withStyleName(WebThemes.BUTTON_OPTION);
            editBtns.addComponent(printBtn);
        }

        if (canWrite && (buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
            MButton cloneBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLONE), clickEvent -> {
                optionBtn.setPopupVisible(false);
                T item = previewForm.getBean();
                previewForm.fireCloneForm(item);
            }).withIcon(FontAwesome.ROAD);
            popupButtonsControl.addOption(cloneBtn);
        }

        optionBtn.setContent(popupButtonsControl);

        ButtonGroup navigationBtns = new ButtonGroup();
        navigationBtns.setStyleName("navigation-btns");

        if (canRead && (buttonEnableFlags & NAVIGATOR_BTN_PRESENTED) == NAVIGATOR_BTN_PRESENTED) {
            MButton previousItem = new MButton("", clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireGotoPrevious(item);
            }).withIcon(FontAwesome.CHEVRON_LEFT).withStyleName(WebThemes.BUTTON_OPTION)
                    .withDescription(UserUIContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_PREVIOUS_ITEM));
            navigationBtns.addButton(previousItem);

            MButton nextItemBtn = new MButton("", clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireGotoNextItem(item);
            }).withIcon(FontAwesome.CHEVRON_RIGHT).withStyleName(WebThemes.BUTTON_OPTION)
                    .withDescription(UserUIContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_NEXT_ITEM));
            navigationBtns.addButton(nextItemBtn);
        }

        layout.addComponent(navigationBtns);
        if (popupButtonsControl.getComponentCount() > 0) {
            optionBtn.setContent(popupButtonsControl);
            layout.addComponent(optionBtn);
        }
        return layout;
    }
}
