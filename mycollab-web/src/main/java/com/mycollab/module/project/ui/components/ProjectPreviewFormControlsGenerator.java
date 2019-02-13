/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.reporting.PrintButton;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.ButtonGroup;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.io.Serializable;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ProjectPreviewFormControlsGenerator<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int ADD_BTN_PRESENTED = 2;
    public static final int EDIT_BTN_PRESENTED = 4;
    public static final int DELETE_BTN_PRESENTED = 8;
    public static final int CLONE_BTN_PRESENTED = 16;
    public static final int ASSIGN_BTN_PRESENTED = 32;
    public static final int NAVIGATOR_BTN_PRESENTED = 64;
    public static final int PRINT_BTN_PRESENTED = 128;

    private AdvancedPreviewBeanForm<T> previewForm;

    private PopupButton optionBtn;
    private OptionPopupContent popupButtonsControl;
    private MHorizontalLayout editButtons;

    private MHorizontalLayout wrapLayout;
    private MHorizontalLayout layout;

    public ProjectPreviewFormControlsGenerator(AdvancedPreviewBeanForm<T> editForm) {
        this.previewForm = editForm;
        wrapLayout = new MHorizontalLayout().withMargin(false);
        layout = new MHorizontalLayout().withMargin(false);
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        popupButtonsControl = new OptionPopupContent();
        editButtons = new MHorizontalLayout();
    }

    public MHorizontalLayout createButtonControls(int buttonEnableFlags, String permissionItem) {
        optionBtn = new PopupButton();
        optionBtn.addStyleName(WebThemes.BUTTON_OPTION);
        optionBtn.setIcon(VaadinIcons.ELLIPSIS_H);

        if (permissionItem != null) {
            boolean canWrite = CurrentProjectVariables.canWrite(permissionItem);
            boolean canAccess = CurrentProjectVariables.canAccess(permissionItem);
            boolean canRead = CurrentProjectVariables.canRead(permissionItem);

            if (canWrite && (buttonEnableFlags & ASSIGN_BTN_PRESENTED) == ASSIGN_BTN_PRESENTED) {
                MButton assignBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ASSIGN), clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.fireAssignForm(item);
                }).withIcon(VaadinIcons.SHARE).withStyleName(WebThemes.BUTTON_ACTION);
                editButtons.addComponent(assignBtn);
            }

            if (canWrite && (buttonEnableFlags & ADD_BTN_PRESENTED) == ADD_BTN_PRESENTED) {
                MButton addBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                    optionBtn.setPopupVisible(false);
                    T item = previewForm.getBean();
                    previewForm.fireAddForm(item);
                }).withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION);
                editButtons.addComponent(addBtn);
            }

            if (canWrite && (buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
                MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
                    optionBtn.setPopupVisible(false);
                    T item = previewForm.getBean();
                    previewForm.fireEditForm(item);
                }).withIcon(VaadinIcons.EDIT).withStyleName(WebThemes.BUTTON_ACTION);
                editButtons.addComponent(editBtn);
            }

            if (canRead && (buttonEnableFlags & PRINT_BTN_PRESENTED) == PRINT_BTN_PRESENTED) {
                final PrintButton printBtn = new PrintButton(UserUIContext.getMessage(GenericI18Enum.ACTION_PRINT));
                printBtn.withListener(clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.firePrintForm(printBtn, item);
                }).withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_PRINT));
                popupButtonsControl.addOption(printBtn);
            }

            if (canWrite && (buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
                MButton cloneBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLONE), clickEvent -> {
                    optionBtn.setPopupVisible(false);
                    T item = previewForm.getBean();
                    previewForm.fireCloneForm(item);
                }).withIcon(VaadinIcons.ROAD);
                popupButtonsControl.addOption(cloneBtn);
            }

            wrapLayout.withComponent(editButtons);

            if (canRead && (buttonEnableFlags & NAVIGATOR_BTN_PRESENTED) == NAVIGATOR_BTN_PRESENTED) {
                ButtonGroup navigationBtns = new ButtonGroup();
                MButton previousItem = new MButton("", clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.fireGotoPrevious(item);
                }).withIcon(VaadinIcons.CHEVRON_LEFT).withStyleName(WebThemes.BUTTON_OPTION)
                        .withDescription(UserUIContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_PREVIOUS_ITEM));
                navigationBtns.addButton(previousItem);

                MButton nextItemBtn = new MButton("", clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.fireGotoNextItem(item);
                }).withIcon(VaadinIcons.CHEVRON_RIGHT).withStyleName(WebThemes.BUTTON_OPTION)
                        .withDescription(UserUIContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_NEXT_ITEM));
                navigationBtns.addButton(nextItemBtn);
                wrapLayout.withComponent(navigationBtns);
            }

            if (canAccess && (buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
                MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.fireDeleteForm(item);
                }).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_DANGER);
                popupButtonsControl.addDangerOption(deleteBtn);
            }

            if (popupButtonsControl.getComponentCount() > 0) {
                optionBtn.setContent(popupButtonsControl);
                wrapLayout.withComponent(optionBtn);
            }
        }

        return layout.with(wrapLayout).withAlign(wrapLayout, Alignment.MIDDLE_RIGHT);
    }

    /**
     * @param comp
     */
    public void insertToControlBlock(Component comp) {
        wrapLayout.addComponent(comp, 0);
    }

    public void addOptionButton(Button button) {
        button.addClickListener(clickEvent -> optionBtn.setPopupVisible(false));
        popupButtonsControl.addOption(button);
    }

    public MHorizontalLayout createButtonControls(String permissionItem) {
        return createButtonControls(ADD_BTN_PRESENTED | EDIT_BTN_PRESENTED
                | DELETE_BTN_PRESENTED | PRINT_BTN_PRESENTED | CLONE_BTN_PRESENTED
                | NAVIGATOR_BTN_PRESENTED, permissionItem);
    }
}
