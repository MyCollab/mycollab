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

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class ProjectPreviewFormControlsGenerator<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final AdvancedPreviewBeanForm<T> previewForm;
	private Button backBtn;
	private Button editBtn;
	private Button deleteBtn;
	private Button cloneBtn;
	private Button previousItem;
	private Button nextItemBtn;

	private Button assignBtn;
	private boolean haveAssignButton;

	private HorizontalLayout editButtons;
	private HorizontalLayout layout;

	public ProjectPreviewFormControlsGenerator(
			final AdvancedPreviewBeanForm<T> editForm) {
		this.previewForm = editForm;
	}

	public HorizontalLayout createButtonControls(final String permissionItem) {
		layout = new HorizontalLayout();
		layout.setStyleName("control-buttons");
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setWidth("100%");

		backBtn = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final T item = previewForm.getBean();
				previewForm.fireCancelForm(item);
			}
		});
		backBtn.setIcon(MyCollabResource.newResource("icons/16/back.png"));
		backBtn.setDescription("Back to list");
		backBtn.setStyleName("link");
		//UiUtils.addComponent(layout, backBtn, Alignment.MIDDLE_LEFT);

		editButtons = new HorizontalLayout();
		editButtons.setSpacing(true);
		editButtons.addStyleName("edit-btn");

		if (haveAssignButton) {
			assignBtn = new Button(GenericBeanForm.ASSIGN_ACTION,
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final T item = previewForm.getBean();
							previewForm.fireAssignForm(item);
						}
					});
			assignBtn.setIcon(MyCollabResource
					.newResource("icons/16/assign.png"));
			assignBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
			editButtons.addComponent(assignBtn);
			editButtons.setComponentAlignment(assignBtn,
					Alignment.MIDDLE_CENTER);
		}
		editBtn = new Button(GenericBeanForm.EDIT_ACTION,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						final T item = previewForm.getBean();
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
					public void buttonClick(final ClickEvent event) {
						final T item = previewForm.getBean();
						previewForm.fireDeleteForm(item);
					}
				});
		deleteBtn.setIcon(MyCollabResource.newResource("icons/16/delete2.png"));
		deleteBtn.setStyleName(UIConstants.THEME_RED_LINK);
		editButtons.addComponent(deleteBtn);
		editButtons.setComponentAlignment(deleteBtn, Alignment.MIDDLE_CENTER);

		cloneBtn = new Button(GenericBeanForm.CLONE_ACTION,
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						final T item = previewForm.getBean();
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

		previousItem = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final T item = previewForm.getBean();
				previewForm.fireGotoPrevious(item);
			}
		});

		previousItem.setIcon(MyCollabResource
				.newResource("icons/16/previous.png"));
		previousItem.setStyleName("link");
		previousItem.setDescription("Show previous item");
		layout.addComponent(previousItem);
		layout.setComponentAlignment(previousItem, Alignment.MIDDLE_RIGHT);

		nextItemBtn = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final T item = previewForm.getBean();
				previewForm.fireGotoNextItem(item);
			}
		});

		nextItemBtn.setIcon(MyCollabResource.newResource("icons/16/next.png"));
		nextItemBtn.setStyleName("link");
		nextItemBtn.setDescription("Show next item");
		layout.addComponent(nextItemBtn);
		layout.setComponentAlignment(nextItemBtn, Alignment.MIDDLE_RIGHT);

		if (permissionItem != null) {
			final boolean canRead = CurrentProjectVariables
					.canRead(permissionItem);
			final boolean canWrite = CurrentProjectVariables
					.canWrite(permissionItem);
			final boolean canAccess = CurrentProjectVariables
					.canAccess(permissionItem);

			if (haveAssignButton) {
				assignBtn.setEnabled(canWrite);
			}

			backBtn.setEnabled(canRead);
			editBtn.setEnabled(canWrite);
			cloneBtn.setEnabled(canWrite);
			deleteBtn.setEnabled(canAccess);
		}
		return layout;
	}

	public void insertToControlBlock(Button button) {
		editButtons.addComponent(button, 0);
	}

	public HorizontalLayout createButtonControls(final String permissionItem,
			final boolean haveAssignButton) {
		this.haveAssignButton = haveAssignButton;
		return createButtonControls(permissionItem);
	}
}
