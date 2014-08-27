/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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

	private Button backBtn, deleteBtn, previousItem, nextItemBtn, historyBtn,
			editBtn, cloneBtn;
	private AdvancedPreviewBeanForm<T> previewForm;

	private VerticalLayout editButtons;

	public CrmPreviewFormControlsGenerator(
			final AdvancedPreviewBeanForm<T> editForm) {
		this.previewForm = editForm;

		editButtons = new VerticalLayout();
		editButtons.setSpacing(true);
		editButtons.setWidth("100%");
		editButtons.setMargin(true);
		editButtons.addStyleName("edit-btn-layout");
	}

	public void insertToControlBlock(Button button) {
		editButtons.addComponent(button, 0);
	}

	public VerticalLayout createButtonControls(final String permissionItem) {
		return createButtonControls(EDIT_BTN_PRESENTED | DELETE_BTN_PRESENTED
				| CLONE_BTN_PRESENTED, permissionItem);
	}

	public VerticalLayout createButtonControls(int buttonEnableFlags,
			final String permissionItem) {

		boolean canRead = true;
		boolean canWrite = true;
		boolean canAccess = true;
		if (permissionItem != null) {
			canRead = AppContext.canRead(permissionItem);
			canWrite = AppContext.canWrite(permissionItem);
			canAccess = AppContext.canAccess(permissionItem);
		}

		if ((buttonEnableFlags & BACK_BTN_PRESENTED) == BACK_BTN_PRESENTED) {
			backBtn = new Button(null, new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {

					final T item = previewForm.getBean();
					previewForm.fireCancelForm(item);
				}
			});
			backBtn.setDescription("Back to list");
			backBtn.setStyleName("link");
			editButtons.addComponent(backBtn);
			editButtons.setComponentAlignment(backBtn, Alignment.MIDDLE_LEFT);
			backBtn.setEnabled(canRead);
		}

		if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
			editBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL));
			editBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireEditForm(item);
				}
			});
			editBtn.setWidth("100%");
			editButtons.addComponent(editBtn);
			editButtons.setComponentAlignment(editBtn, Alignment.MIDDLE_CENTER);
			editBtn.setEnabled(canWrite);
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
			deleteBtn.setWidth("100%");
			editButtons.addComponent(deleteBtn);
			editButtons.setComponentAlignment(deleteBtn,
					Alignment.MIDDLE_CENTER);
			deleteBtn.setEnabled(canAccess);
		}

		if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
			cloneBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLONE_LABEL));
			cloneBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireCloneForm(item);
				}
			});
			cloneBtn.setWidth("100%");
			editButtons.addComponent(cloneBtn);
			editButtons
					.setComponentAlignment(cloneBtn, Alignment.MIDDLE_CENTER);
			cloneBtn.setEnabled(canWrite);
		}

		if ((buttonEnableFlags & PREVIOUS_BTN_PRESENTED) == PREVIOUS_BTN_PRESENTED) {
			previousItem = new Button(null, new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireGotoPrevious(item);
				}
			});

			previousItem.setStyleName("link");
			previousItem.setDescription("Read previous item");
			editButtons.addComponent(previousItem);
			editButtons.setComponentAlignment(previousItem,
					Alignment.MIDDLE_RIGHT);
			previousItem.setEnabled(canRead);
		}

		if ((buttonEnableFlags & NEXT_BTN_PRESENTED) == NEXT_BTN_PRESENTED) {
			nextItemBtn = new Button(null, new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireGotoNextItem(item);
				}
			});

			nextItemBtn.setStyleName("link");
			nextItemBtn.setDescription("Read next item");
			editButtons.addComponent(nextItemBtn);
			editButtons.setComponentAlignment(nextItemBtn,
					Alignment.MIDDLE_RIGHT);
			nextItemBtn.setEnabled(canRead);
		}

		if ((buttonEnableFlags & HISTORY_BTN_PRESENTED) == HISTORY_BTN_PRESENTED) {
			historyBtn = new Button(null, new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					previewForm.showHistory();
				}
			});

			historyBtn.setStyleName("link");
			historyBtn.setDescription("Show history log");
			editButtons.addComponent(historyBtn);
			editButtons.setComponentAlignment(historyBtn,
					Alignment.MIDDLE_RIGHT);
			historyBtn.setEnabled(canRead);
		}

		return editButtons;
	}
}
