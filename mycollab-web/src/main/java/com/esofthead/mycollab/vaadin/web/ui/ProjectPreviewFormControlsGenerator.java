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
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.reporting.PrintButton;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.peter.buttongroup.ButtonGroup;
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

    private SplitButton optionBtn;
    private OptionPopupContent popupButtonsControl;
    private MHorizontalLayout editButtons;
    private MHorizontalLayout layout;

    public ProjectPreviewFormControlsGenerator(AdvancedPreviewBeanForm<T> editForm) {
        this.previewForm = editForm;
        layout = new MHorizontalLayout();
        layout.setSizeUndefined();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        popupButtonsControl = new OptionPopupContent();
        editButtons = new MHorizontalLayout();
    }

    public HorizontalLayout createButtonControls(int buttonEnableFlags, String permissionItem) {
        Button optionParentBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_OPTION), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                optionBtn.setPopupVisible(true);
            }
        });

        optionBtn = new SplitButton(optionParentBtn);
        optionBtn.setWidthUndefined();
        optionBtn.addStyleName(UIConstants.BUTTON_OPTION);

        if (permissionItem != null) {
            boolean canWrite = CurrentProjectVariables.canWrite(permissionItem);
            boolean canAccess = CurrentProjectVariables.canAccess(permissionItem);
            boolean canRead = CurrentProjectVariables.canRead(permissionItem);

            if ((buttonEnableFlags & ASSIGN_BTN_PRESENTED) == ASSIGN_BTN_PRESENTED) {
                Button assignBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        T item = previewForm.getBean();
                        previewForm.fireAssignForm(item);
                    }
                });
                assignBtn.setIcon(FontAwesome.SHARE);
                assignBtn.setStyleName(UIConstants.BUTTON_ACTION);
                editButtons.addComponent(assignBtn);
                assignBtn.setEnabled(canWrite);
            }

            if ((buttonEnableFlags & ADD_BTN_PRESENTED) == ADD_BTN_PRESENTED) {
                Button addBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        optionBtn.setPopupVisible(false);
                        T item = previewForm.getBean();
                        previewForm.fireAddForm(item);
                    }
                });
                addBtn.setIcon(FontAwesome.PLUS);
                addBtn.setStyleName(UIConstants.BUTTON_ACTION);
                addBtn.setEnabled(canWrite);
                editButtons.addComponent(addBtn);
            }

            if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
                Button editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        optionBtn.setPopupVisible(false);
                        T item = previewForm.getBean();
                        previewForm.fireEditForm(item);
                    }
                });
                editBtn.setIcon(FontAwesome.EDIT);
                editBtn.setStyleName(UIConstants.BUTTON_ACTION);
                editBtn.setEnabled(canWrite);
                editButtons.addComponent(editBtn);
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
                deleteBtn.setIcon(FontAwesome.TRASH_O);
                deleteBtn.setStyleName(UIConstants.BUTTON_DANGER);
                deleteBtn.setEnabled(canAccess);
                editButtons.addComponent(deleteBtn);
            }

            if ((buttonEnableFlags & PRINT_BTN_PRESENTED) == PRINT_BTN_PRESENTED) {
                final PrintButton printBtn = new PrintButton();
                printBtn.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        T item = previewForm.getBean();
                        previewForm.firePrintForm(printBtn, item);
                    }
                });
                printBtn.setStyleName(UIConstants.BUTTON_OPTION);
                printBtn.setDescription("Print");
                printBtn.setEnabled(canRead);
                editButtons.addComponent(printBtn);
            }

            if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
                Button cloneBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLONE), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        optionBtn.setPopupVisible(false);
                        T item = previewForm.getBean();
                        previewForm.fireCloneForm(item);
                    }
                });
                cloneBtn.setIcon(FontAwesome.ROAD);
                cloneBtn.setEnabled(canWrite);
                popupButtonsControl.addOption(cloneBtn);
            }

            layout.with(editButtons);

            if (popupButtonsControl.getComponentCount() > 0) {
                optionBtn.setContent(popupButtonsControl);
                layout.addComponent(optionBtn);
            }


            if ((buttonEnableFlags & NAVIGATOR_BTN_PRESENTED) == NAVIGATOR_BTN_PRESENTED) {
                ButtonGroup navigationBtns = new ButtonGroup();
                Button previousItem = new Button(null, new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        T item = previewForm.getBean();
                        previewForm.fireGotoPrevious(item);
                    }
                });
                previousItem.setIcon(FontAwesome.CHEVRON_LEFT);
                previousItem.setCaptionAsHtml(true);
                previousItem.setStyleName(UIConstants.BUTTON_ACTION);
                previousItem.setDescription(AppContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_PREVIOUS_ITEM));
                previousItem.setEnabled(canRead);
                navigationBtns.addButton(previousItem);

                Button nextItemBtn = new Button(null, new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        T item = previewForm.getBean();
                        previewForm.fireGotoNextItem(item);
                    }
                });
                nextItemBtn.setIcon(FontAwesome.CHEVRON_RIGHT);
                nextItemBtn.setStyleName(UIConstants.BUTTON_ACTION);
                nextItemBtn.setDescription(AppContext.getMessage(GenericI18Enum.TOOLTIP_SHOW_NEXT_ITEM));
                nextItemBtn.setEnabled(canRead);
                navigationBtns.addButton(nextItemBtn);

                layout.addComponent(navigationBtns);
            }
        }

        return layout;
    }

    /**
     * @param comp
     */
    public void insertToControlBlock(Component comp) {
        layout.addComponent(comp, 0);
    }

    public void addOptionButton(Button button) {
        button.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 2710916670115028630L;

            @Override
            public void buttonClick(ClickEvent event) {
                optionBtn.setPopupVisible(false);
            }
        });
        popupButtonsControl.addOption(button);
    }

    public HorizontalLayout createButtonControls(String permissionItem) {
        return createButtonControls(ADD_BTN_PRESENTED | EDIT_BTN_PRESENTED
                | DELETE_BTN_PRESENTED | PRINT_BTN_PRESENTED | CLONE_BTN_PRESENTED
                | NAVIGATOR_BTN_PRESENTED, permissionItem);
    }
}
