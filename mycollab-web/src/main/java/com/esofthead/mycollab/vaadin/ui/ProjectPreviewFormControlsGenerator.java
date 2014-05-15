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

import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class ProjectPreviewFormControlsGenerator<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final AdvancedPreviewBeanForm<T> previewForm;
	private Button editBtn;
	private Button deleteBtn;
	private Button cloneBtn;
	private Button previousItem;
	private Button nextItemBtn;

	private Button assignBtn;
	private boolean haveAssignButton;

	private SplitButton optionBtn;
	private Button optionParentBtn;
	private VerticalLayout popupButtonsControl;

	private HorizontalLayout editButtons;
	private HorizontalLayout layout;

	private boolean haveNavigationBtns;

	public ProjectPreviewFormControlsGenerator(
			final AdvancedPreviewBeanForm<T> editForm) {
		this.previewForm = editForm;
	}

	public HorizontalLayout createButtonControls(final String permissionItem) {
		layout = new HorizontalLayout();
		layout.setStyleName("control-buttons");
		layout.setSpacing(true);
		layout.setSizeUndefined();

		editButtons = new HorizontalLayout();
		editButtons.setSpacing(true);
		editButtons.addStyleName("edit-btn");

		optionParentBtn = new Button("Option", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				/*
				 * if (optionBtn.getPopupVisible())
				 * optionBtn.setPopupVisible(false); else
				 */
				optionBtn.setPopupVisible(true);
			}
		});

		optionBtn = new SplitButton(optionParentBtn);
		optionBtn.setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
		optionBtn.addStyleName(UIConstants.THEME_GRAY_LINK);

		popupButtonsControl = new VerticalLayout();
		popupButtonsControl.setWidth("100px");
		popupButtonsControl.setMargin(new MarginInfo(false, true, false, true));
		popupButtonsControl.setSpacing(true);

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
		editBtn.setIcon(MyCollabResource.newResource("icons/16/edit_white.png"));
		editBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		editButtons.addComponent(editBtn);
		editButtons.setComponentAlignment(editBtn, Alignment.MIDDLE_CENTER);

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
		deleteBtn.setIcon(MyCollabResource.newResource("icons/16/delete2.png"));
		deleteBtn.setStyleName(UIConstants.THEME_RED_LINK);
		editButtons.addComponent(deleteBtn);
		editButtons.setComponentAlignment(deleteBtn, Alignment.MIDDLE_CENTER);

		if (haveAssignButton) {
			assignBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN_LABEL),
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
			assignBtn.setStyleName(UIConstants.THEME_LINK);
			popupButtonsControl.addComponent(assignBtn);

		}

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

		optionBtn.setContent(popupButtonsControl);
		editButtons.addComponent(optionBtn);
		editButtons.setComponentAlignment(optionBtn, Alignment.MIDDLE_CENTER);

		layout.addComponent(editButtons);
		layout.setComponentAlignment(editButtons, Alignment.MIDDLE_CENTER);
		layout.setExpandRatio(editButtons, 1.0f);

		if (haveNavigationBtns) {
			ButtonGroup navigationBtns = new ButtonGroup();
			navigationBtns.setStyleName("navigation-btns");
			previousItem = new Button("<", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireGotoPrevious(item);
				}
			});

			previousItem.setStyleName(UIConstants.THEME_GREEN_LINK);
			previousItem.setDescription("Show previous item");
			navigationBtns.addButton(previousItem);

			nextItemBtn = new Button(">", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					final T item = previewForm.getBean();
					previewForm.fireGotoNextItem(item);
				}
			});

			nextItemBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			nextItemBtn.setDescription("Show next item");

			navigationBtns.addButton(nextItemBtn);
			layout.addComponent(navigationBtns);
			layout.setComponentAlignment(navigationBtns, Alignment.MIDDLE_RIGHT);
		}

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

			editBtn.setEnabled(canWrite);
			cloneBtn.setEnabled(canWrite);
			deleteBtn.setEnabled(canAccess);
		}
		return layout;
	}

	public void insertToControlBlock(Component button) {
		editButtons.addComponent(button, 0);
	}

	public HorizontalLayout createButtonControls(final String permissionItem,
			final boolean haveAssignButton) {
		this.haveAssignButton = haveAssignButton;
		return createButtonControls(permissionItem);
	}

	public HorizontalLayout createButtonControls(final String permissionItem,
			final boolean haveAssignButton, boolean haveNavigationButtons) {
		this.haveNavigationBtns = haveNavigationButtons;
		return createButtonControls(permissionItem, haveAssignButton);
	}
}
