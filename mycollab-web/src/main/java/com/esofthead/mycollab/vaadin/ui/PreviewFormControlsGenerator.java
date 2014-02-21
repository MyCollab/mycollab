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

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 * @param <B>
 */
public class PreviewFormControlsGenerator<B> {

	private AdvancedPreviewBeanForm<B> previewForm;
	private Button backBtn;
	private Button editBtn;
	private Button deleteBtn;
	private Button cloneBtn;
	private HorizontalLayout editButtons;
	private HorizontalLayout layout;

	public PreviewFormControlsGenerator(AdvancedPreviewBeanForm<B> editForm) {
		this.previewForm = editForm;
	}

	public HorizontalLayout createButtonControls(String permissionItem) {
		layout = new HorizontalLayout();
		layout.setStyleName("control-buttons");
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setWidth("100%");

		backBtn = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				B item = previewForm.getBean();
				previewForm.fireCancelForm(item);
			}
		});
		backBtn.setIcon(MyCollabResource.newResource("icons/16/back.png"));
		backBtn.setDescription("Back to list");
		backBtn.setStyleName("link");
		layout.addComponent(backBtn);
		layout.setComponentAlignment(backBtn, Alignment.MIDDLE_LEFT);

		editButtons = new HorizontalLayout();
		editButtons.setSpacing(true);

		editBtn = new Button(GenericBeanForm.EDIT_ACTION,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						B item = previewForm.getBean();
						previewForm.fireEditForm(item);
					}
				});
		editBtn.setIcon(MyCollabResource.newResource("icons/16/edit_white.png"));
		editBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		editButtons.addComponent(editBtn);
		editButtons.setComponentAlignment(editBtn, Alignment.MIDDLE_CENTER);

		deleteBtn = new Button(GenericBeanForm.DELETE_ACTION,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						B item = previewForm.getBean();
						previewForm.fireDeleteForm(item);
					}
				});
		deleteBtn.setIcon(MyCollabResource.newResource("icons/16/delete2.png"));
		deleteBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		editButtons.addComponent(deleteBtn);
		editButtons.setComponentAlignment(deleteBtn, Alignment.MIDDLE_CENTER);

		cloneBtn = new Button(GenericBeanForm.CLONE_ACTION,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						B item = previewForm.getBean();
						previewForm.fireCloneForm(item);
					}
				});
		cloneBtn.setIcon(MyCollabResource.newResource("icons/16/clone.png"));
		cloneBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		editButtons.addComponent(cloneBtn);
		editButtons.setComponentAlignment(cloneBtn, Alignment.MIDDLE_CENTER);

		layout.addComponent(editButtons);
		layout.setComponentAlignment(editButtons, Alignment.MIDDLE_CENTER);
		layout.setExpandRatio(editButtons, 1.0f);

		if (permissionItem != null) {
			boolean canRead = AppContext.canRead(permissionItem);
			boolean canWrite = AppContext.canWrite(permissionItem);
			boolean canAccess = AppContext.canAccess(permissionItem);

			backBtn.setEnabled(canRead);
			editBtn.setEnabled(canWrite);
			cloneBtn.setEnabled(canWrite);
			deleteBtn.setEnabled(canAccess);
		}
		return layout;
	}

	public void removeCloneButton() {
		editButtons.removeComponent(cloneBtn);
	}

	public HorizontalLayout getLayout() {
		return layout;
	}

	public void setDeleteButtonVisible(boolean visible) {
		deleteBtn.setVisible(visible);
	}
}
