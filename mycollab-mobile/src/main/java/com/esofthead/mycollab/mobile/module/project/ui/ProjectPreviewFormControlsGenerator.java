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
package com.esofthead.mycollab.mobile.module.project.ui;

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
 * @since 4.5.0
 * 
 */
public class ProjectPreviewFormControlsGenerator<T> {

	public static int EDIT_BTN_PRESENTED = 2;
	public static int DELETE_BTN_PRESENTED = 4;
	public static int CLONE_BTN_PRESENTED = 8;
	public static int ASSIGN_BTN_PRESENTED = 16;

	private Button deleteBtn, editBtn, cloneBtn, assignBtn;
	private AdvancedPreviewBeanForm<T> previewForm;

	private VerticalLayout editButtons;

	public ProjectPreviewFormControlsGenerator(
			final AdvancedPreviewBeanForm<T> editForm) {
		this.previewForm = editForm;

		editButtons = new VerticalLayout();
		editButtons.setSpacing(true);
		editButtons.setWidth("100%");
		editButtons.setMargin(true);
		editButtons.addStyleName("edit-btn-layout");
		editButtons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
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

		boolean canWrite = true;
		boolean canAccess = true;
		if (permissionItem != null) {
			canWrite = AppContext.canWrite(permissionItem);
			canAccess = AppContext.canAccess(permissionItem);
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
			cloneBtn.setEnabled(canWrite);
		}

		if ((buttonEnableFlags & ASSIGN_BTN_PRESENTED) == ASSIGN_BTN_PRESENTED) {
			assignBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN_LABEL));
			assignBtn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 6882405297466892069L;

				@Override
				public void buttonClick(Button.ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireAssignForm(item);
				}
			});
			assignBtn.setWidth("100%");
			editButtons.addComponent(assignBtn);
			editButtons.setEnabled(canWrite);
		}

		return editButtons;
	}
}
