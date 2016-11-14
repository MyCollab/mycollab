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
package com.mycollab.module.user.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.io.Serializable;

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class PreviewFormControlsGenerator<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int ADD_BTN_PRESENTED = 2;
    public static final int EDIT_BTN_PRESENTED = 4;
    public static final int DELETE_BTN_PRESENTED = 8;
    public static final int CLONE_BTN_PRESENTED = 16;
    public static final int NAVIGATOR_BTN_PRESENTED = 32;

    private AdvancedPreviewBeanForm<T> previewForm;

    private PopupButton optionBtn;
    private OptionPopupContent popupButtonsControl;
    private MHorizontalLayout editButtons;
    private MHorizontalLayout layout;

    public PreviewFormControlsGenerator(AdvancedPreviewBeanForm<T> editForm) {
        this.previewForm = editForm;
        layout = new MHorizontalLayout();
        layout.setSizeUndefined();
        popupButtonsControl = new OptionPopupContent();
        editButtons = new MHorizontalLayout();
        editButtons.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
    }

    public HorizontalLayout createButtonControls(int buttonEnableFlags, String permissionItem) {
        optionBtn = new PopupButton();
        optionBtn.addStyleName(WebThemes.BUTTON_OPTION);
        optionBtn.setIcon(FontAwesome.ELLIPSIS_H);

        if (permissionItem != null) {
            boolean canWrite = UserUIContext.canWrite(permissionItem);
            boolean canAccess = UserUIContext.canAccess(permissionItem);
            boolean canRead = UserUIContext.canRead(permissionItem);

            if ((buttonEnableFlags & ADD_BTN_PRESENTED) == ADD_BTN_PRESENTED) {
                MButton addBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), clickEvent -> {
                    optionBtn.setPopupVisible(false);
                    T item = previewForm.getBean();
                    previewForm.fireAddForm(item);
                }).withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION).withVisible(canWrite);
                editButtons.addComponent(addBtn);
            }

            if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
                MButton editBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT), clickEvent -> {
                    optionBtn.setPopupVisible(false);
                    T item = previewForm.getBean();
                    previewForm.fireEditForm(item);
                }).withIcon(FontAwesome.EDIT).withStyleName(WebThemes.BUTTON_ACTION).withVisible(canWrite);
                editButtons.addComponent(editBtn);
            }

            if ((buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
                MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.fireDeleteForm(item);
                }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_DANGER).withVisible(canAccess);
                editButtons.addComponent(deleteBtn);
            }

            layout.with(editButtons);

            if ((buttonEnableFlags & NAVIGATOR_BTN_PRESENTED) == NAVIGATOR_BTN_PRESENTED) {
                ButtonGroup navigationBtns = new ButtonGroup();
                MButton previousItem = new MButton("", clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.fireGotoPrevious(item);
                }).withIcon(FontAwesome.CHEVRON_LEFT).withStyleName(WebThemes.BUTTON_ACTION)
                        .withDescription(UserUIContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_PREVIOUS_ITEM)).withVisible(canRead);
                navigationBtns.addButton(previousItem);

                MButton nextItemBtn = new MButton("", clickEvent -> {
                    T item = previewForm.getBean();
                    previewForm.fireGotoNextItem(item);
                }).withIcon(FontAwesome.CHEVRON_RIGHT).withStyleName(WebThemes.BUTTON_ACTION)
                        .withDescription(UserUIContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_NEXT_ITEM)).withVisible(canRead);
                navigationBtns.addButton(nextItemBtn);
                layout.with(navigationBtns);
            }

            if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
                MButton cloneBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLONE), clickEvent -> {
                    optionBtn.setPopupVisible(false);
                    T item = previewForm.getBean();
                    previewForm.fireCloneForm(item);
                }).withIcon(FontAwesome.ROAD).withVisible(canWrite);
                popupButtonsControl.addOption(cloneBtn);
            }

            if (popupButtonsControl.getComponentCount() > 0) {
                optionBtn.setContent(popupButtonsControl);
                layout.with(optionBtn);
            }
        }
        return layout;
    }

    public void insertToControlBlock(Component comp) {
        editButtons.addComponent(comp, 0);
    }

    public void removeButtonIndex(int index) {
        editButtons.removeComponent(editButtons.getComponent(index));
    }

    public HorizontalLayout createButtonControls(String permissionItem) {
        return createButtonControls(ADD_BTN_PRESENTED | EDIT_BTN_PRESENTED
                | DELETE_BTN_PRESENTED | CLONE_BTN_PRESENTED
                | NAVIGATOR_BTN_PRESENTED, permissionItem);
    }
}
