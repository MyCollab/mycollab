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
package com.esofthead.mycollab.module.crm.ui.components;

import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmPreviewFormControlsGenerator<T> {
	public static int BACK_BTN_PRESENTED = 2;
	public static int EDIT_BTN_PRESENTED = 4;
	public static int DELETE_BTN_PRESENTED = 8;
	public static int CLONE_BTN_PRESENTED = 16;
	public static int PREVIOUS_BTN_PRESENTED = 32;
	public static int NEXT_BTN_PRESENTED = 64;
	public static int HISTORY_BTN_PRESENTED = 128;
	public static int ADD_BTN_PRESENTED = 256;

	private Button addBtn, editBtn, deleteBtn, cloneBtn, historyBtn,
			previousItem, nextItemBtn;
	private AdvancedPreviewBeanForm<T> previewForm;

	private VerticalLayout popupButtonsControl;
	private SplitButton optionBtn;
	private HorizontalLayout layout;

	public CrmPreviewFormControlsGenerator(
			final AdvancedPreviewBeanForm<T> editForm) {
		this.previewForm = editForm;

		layout = new HorizontalLayout();

		Button editButtons = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_OPTION_LABEL),
				new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {

						optionBtn.setPopupVisible(true);
					}
				});

		editButtons.setWidthUndefined();
		editButtons.addStyleName(UIConstants.THEME_GRAY_LINK);
		optionBtn = new SplitButton(editButtons);
		optionBtn.addStyleName(UIConstants.THEME_GRAY_LINK);
	}

	public void insertToControlBlock(Button button) {
		layout.addComponent(button, 0);
	}

	public HorizontalLayout createButtonControls(final String permissionItem) {
		return createButtonControls(EDIT_BTN_PRESENTED | DELETE_BTN_PRESENTED
				| CLONE_BTN_PRESENTED | HISTORY_BTN_PRESENTED
				| PREVIOUS_BTN_PRESENTED | NEXT_BTN_PRESENTED
				| ADD_BTN_PRESENTED, permissionItem);
	}

	public HorizontalLayout createButtonControls(int buttonEnableFlags,
			final String permissionItem) {

		layout.setStyleName("control-buttons");
		layout.setSpacing(true);
		layout.setSizeUndefined();

		boolean canRead = true;
		boolean canWrite = true;
		boolean canAccess = true;
		if (permissionItem != null) {
			canRead = AppContext.canRead(permissionItem);
			canWrite = AppContext.canWrite(permissionItem);
			canAccess = AppContext.canAccess(permissionItem);
		}

		popupButtonsControl = new VerticalLayout();

		popupButtonsControl.setMargin(new MarginInfo(false, true, false, true));
		popupButtonsControl.setSpacing(true);

		if ((buttonEnableFlags & ADD_BTN_PRESENTED) == ADD_BTN_PRESENTED) {
			addBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_ADD_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							optionBtn.setPopupVisible(false);
							final T item = previewForm.getBean();
							previewForm.fireAddForm(item);
						}
					});
			addBtn.setIcon(MyCollabResource
					.newResource("icons/16/addRecord.png"));
			addBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			addBtn.setEnabled(canWrite);
			layout.addComponent(addBtn);
		}

		if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
			editBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							optionBtn.setPopupVisible(false);
							final T item = previewForm.getBean();
							previewForm.fireEditForm(item);
						}
					});
			editBtn.setIcon(MyCollabResource
					.newResource("icons/16/edit_white.png"));
			editBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			editBtn.setEnabled(canWrite);
			layout.addComponent(editBtn);
		}

		if ((buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
			deleteBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final T item = previewForm.getBean();
							previewForm.fireDeleteForm(item);
						}
					});
			deleteBtn.setIcon(MyCollabResource
					.newResource("icons/16/delete2.png"));
			deleteBtn.setStyleName(UIConstants.THEME_RED_LINK);
			layout.addComponent(deleteBtn);
			deleteBtn.setEnabled(canAccess);
		}

		if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
			cloneBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLONE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							optionBtn.setPopupVisible(false);
							final T item = previewForm.getBean();
							previewForm.fireCloneForm(item);
						}
					});
			cloneBtn.setIcon(MyCollabResource.newResource("icons/16/clone.png"));
			cloneBtn.setStyleName("link");
			popupButtonsControl.addComponent(cloneBtn);
		}

		if ((buttonEnableFlags & HISTORY_BTN_PRESENTED) == HISTORY_BTN_PRESENTED) {
			historyBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_HISTORY_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							optionBtn.setPopupVisible(false);
							previewForm.showHistory();
						}
					});
			historyBtn.setIcon(MyCollabResource
					.newResource("icons/16/history.png"));
			historyBtn.setStyleName("link");
			popupButtonsControl.addComponent(historyBtn);
		}

		optionBtn.setContent(popupButtonsControl);

		if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED
				| (buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {

			layout.addComponent(optionBtn);
		}

		ButtonGroup navigationBtns = new ButtonGroup();
		navigationBtns.setStyleName("navigation-btns");

		if ((buttonEnableFlags & PREVIOUS_BTN_PRESENTED) == PREVIOUS_BTN_PRESENTED) {
			previousItem = new Button("<", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireGotoPrevious(item);
				}
			});
			previousItem.setStyleName(UIConstants.THEME_GREEN_LINK);
			previousItem.setDescription(AppContext
					.getMessage(GenericI18Enum.TOOLTIP_SHOW_PREVIOUS_ITEM));
			navigationBtns.addButton(previousItem);
			previousItem.setEnabled(canRead);
		}

		if ((buttonEnableFlags & NEXT_BTN_PRESENTED) == NEXT_BTN_PRESENTED) {
			nextItemBtn = new Button(">", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireGotoNextItem(item);
				}
			});
			nextItemBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			nextItemBtn.setDescription(AppContext
					.getMessage(GenericI18Enum.TOOLTIP_SHOW_NEXT_ITEM));
			navigationBtns.addButton(nextItemBtn);
			nextItemBtn.setEnabled(canRead);
		}

		layout.addComponent(navigationBtns);

		return layout;
	}
}
