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
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.reporting.PrintButton;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.UIConstants;
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
        optionBtn.addStyleName(UIConstants.BOX);
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
            canRead = AppContext.canRead(permissionItem);
            canWrite = AppContext.canWrite(permissionItem);
            canAccess = AppContext.canAccess(permissionItem);
        }

        MHorizontalLayout editBtns = new MHorizontalLayout();
        layout.addComponent(editBtns);

        OptionPopupContent popupButtonsControl = new OptionPopupContent();

        if ((buttonEnableFlags & ADD_BTN_PRESENTED) == ADD_BTN_PRESENTED) {
            MButton addBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                optionBtn.setPopupVisible(false);
                T item = previewForm.getBean();
                previewForm.fireAddForm(item);
            }).withIcon(FontAwesome.PLUS).withStyleName(UIConstants.BUTTON_ACTION).withVisible(canWrite);
            editBtns.addComponent(addBtn);
        }

        if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
            MButton editBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
                optionBtn.setPopupVisible(false);
                T item = previewForm.getBean();
                previewForm.fireEditForm(item);
            }).withIcon(FontAwesome.EDIT).withStyleName(UIConstants.BUTTON_ACTION).withVisible(canWrite);
            editBtns.addComponent(editBtn);
        }

        if ((buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
            MButton deleteBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireDeleteForm(item);
            }).withIcon(FontAwesome.TRASH_O).withStyleName(UIConstants.BUTTON_DANGER).withVisible(canAccess);
            editBtns.addComponent(deleteBtn);
        }

        if ((buttonEnableFlags & PRINT_BTN_PRESENTED) == PRINT_BTN_PRESENTED) {
            final PrintButton printBtn = new PrintButton();
            printBtn.withListener(clickEvent -> {
                T item = previewForm.getBean();
                previewForm.firePrintForm(printBtn, item);
            }).withStyleName(UIConstants.BUTTON_OPTION).withVisible(canRead);
            editBtns.addComponent(printBtn);
        }

        if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
            MButton cloneBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLONE), clickEvent -> {
                optionBtn.setPopupVisible(false);
                T item = previewForm.getBean();
                previewForm.fireCloneForm(item);
            }).withIcon(FontAwesome.ROAD).withVisible(canWrite);
            popupButtonsControl.addOption(cloneBtn);
        }

        optionBtn.setContent(popupButtonsControl);

        ButtonGroup navigationBtns = new ButtonGroup();
        navigationBtns.setStyleName("navigation-btns");

        if ((buttonEnableFlags & NAVIGATOR_BTN_PRESENTED) == NAVIGATOR_BTN_PRESENTED) {
            MButton previousItem = new MButton("", clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireGotoPrevious(item);
            }).withIcon(FontAwesome.CHEVRON_LEFT).withStyleName(UIConstants.BUTTON_OPTION)
                    .withDescription(AppContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_PREVIOUS_ITEM))
                    .withVisible(canRead);
            navigationBtns.addButton(previousItem);

            MButton nextItemBtn = new MButton("", clickEvent -> {
                T item = previewForm.getBean();
                previewForm.fireGotoNextItem(item);
            }).withIcon(FontAwesome.CHEVRON_RIGHT).withStyleName(UIConstants.BUTTON_OPTION)
                    .withDescription(AppContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_NEXT_ITEM))
                    .withVisible(canRead);
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
