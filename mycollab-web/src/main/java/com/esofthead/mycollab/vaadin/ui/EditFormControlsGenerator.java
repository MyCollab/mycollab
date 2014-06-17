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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

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

	public HorizontalLayout createButtonControls(
			final boolean isSaveBtnVisible,
			final boolean isSaveAndNewBtnVisible,
			final boolean isCancelBtnVisible) {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setSizeUndefined();
		layout.setStyleName("addNewControl");

		if (isSaveBtnVisible) {
			final Button saveBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							if (EditFormControlsGenerator.this.editForm
									.validateForm()) {
								EditFormControlsGenerator.this.editForm
										.fireSaveForm();
							}
						}
					});
			saveBtn.setIcon(MyCollabResource.newResource("icons/16/save.png"));
			saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			layout.addComponent(saveBtn);
			layout.setComponentAlignment(saveBtn, Alignment.MIDDLE_CENTER);
		}

		if (isSaveAndNewBtnVisible) {
			final Button saveAndNewBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_NEW_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							if (EditFormControlsGenerator.this.editForm
									.validateForm()) {
								EditFormControlsGenerator.this.editForm
										.fireSaveAndNewForm();
							}
						}
					});
			saveAndNewBtn.setIcon(MyCollabResource
					.newResource("icons/16/save_new.png"));
			saveAndNewBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			layout.addComponent(saveAndNewBtn);
			layout.setComponentAlignment(saveAndNewBtn, Alignment.MIDDLE_CENTER);
		}

		if (isCancelBtnVisible) {
			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EditFormControlsGenerator.this.editForm
									.fireCancelForm();
						}
					});
			cancelBtn.setIcon(MyCollabResource
					.newResource("icons/16/cancel.png"));
			cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			layout.addComponent(cancelBtn);
			layout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_CENTER);
		}

		return layout;
	}
}
