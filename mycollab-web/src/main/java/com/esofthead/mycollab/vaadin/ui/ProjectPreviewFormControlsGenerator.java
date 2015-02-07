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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.peter.buttongroup.ButtonGroup;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class ProjectPreviewFormControlsGenerator<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int ADD_BTN_PRESENTED = 2;
	public static final int EDIT_BTN_PRESENTED = 4;
	public static final int DELETE_BTN_PRESENTED = 8;
	public static final int CLONE_BTN_PRESENTED = 16;
	public static final int ASSIGN_BTN_PRESENTED = 32;
	public static final int NAVIGATOR_BTN_PRESENTED = 64;

	private final AdvancedPreviewBeanForm<T> previewForm;
	private Button addBtn;
	private Button editBtn;
	private Button deleteBtn;
	private Button cloneBtn;

	private Button assignBtn;

	private SplitButton optionBtn;
	private MVerticalLayout popupButtonsControl;
	private MHorizontalLayout editButtons;
	private MHorizontalLayout layout;

	public ProjectPreviewFormControlsGenerator(
			final AdvancedPreviewBeanForm<T> editForm) {
		this.previewForm = editForm;
		layout = new MHorizontalLayout().withStyleName("control-buttons");
		layout.setSizeUndefined();
		popupButtonsControl = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
		editButtons = new MHorizontalLayout();
		editButtons.addStyleName("edit-btn");
	}

	public HorizontalLayout createButtonControls(int buttonEnableFlags,
			final String permissionItem) {

		Button optionParentBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_OPTION),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						optionBtn.setPopupVisible(true);
					}
				});

		optionBtn = new SplitButton(optionParentBtn);
		optionBtn.setWidthUndefined();
		optionBtn.addStyleName(UIConstants.THEME_GRAY_LINK);

		if ((buttonEnableFlags & ADD_BTN_PRESENTED) == ADD_BTN_PRESENTED) {
			addBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_ADD),
					new Button.ClickListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							optionBtn.setPopupVisible(false);
							final T item = previewForm.getBean();
							previewForm.fireAddForm(item);
						}
					});
			addBtn.setIcon(FontAwesome.PLUS_SQUARE);
			addBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			editButtons.addComponent(addBtn);
			editButtons.setComponentAlignment(addBtn, Alignment.MIDDLE_CENTER);
		}

		if ((buttonEnableFlags & EDIT_BTN_PRESENTED) == EDIT_BTN_PRESENTED) {
			editBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
					new Button.ClickListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							optionBtn.setPopupVisible(false);
							final T item = previewForm.getBean();
							previewForm.fireEditForm(item);
						}
					});
			editBtn.setIcon(FontAwesome.EDIT);
			editBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			editButtons.addComponent(editBtn);
			editButtons.setComponentAlignment(editBtn, Alignment.MIDDLE_CENTER);
		}

		if ((buttonEnableFlags & DELETE_BTN_PRESENTED) == DELETE_BTN_PRESENTED) {
			deleteBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
					new Button.ClickListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final T item = previewForm.getBean();
							previewForm.fireDeleteForm(item);
						}
					});
			deleteBtn.setIcon(FontAwesome.RECYCLE);
			deleteBtn.setStyleName(UIConstants.THEME_RED_LINK);
			editButtons.addComponent(deleteBtn);
			editButtons.setComponentAlignment(deleteBtn,
					Alignment.MIDDLE_CENTER);
		}

		if ((buttonEnableFlags & ASSIGN_BTN_PRESENTED) == ASSIGN_BTN_PRESENTED) {
			assignBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_ASSIGN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final T item = previewForm.getBean();
							previewForm.fireAssignForm(item);
						}
					});
			assignBtn.setIcon(FontAwesome.SHARE_SQUARE_O);
			assignBtn.setStyleName(UIConstants.THEME_LINK);
			popupButtonsControl.addComponent(assignBtn);

		}

		if ((buttonEnableFlags & CLONE_BTN_PRESENTED) == CLONE_BTN_PRESENTED) {
			cloneBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLONE),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							optionBtn.setPopupVisible(false);
							final T item = previewForm.getBean();
							previewForm.fireCloneForm(item);
						}
					});
			cloneBtn.setIcon(FontAwesome.ROAD);
			cloneBtn.setStyleName("link");
			popupButtonsControl.addComponent(cloneBtn);
		}

		if (popupButtonsControl.getComponentCount() > 0) {
			optionBtn.setContent(popupButtonsControl);
			editButtons.addComponent(optionBtn);
			editButtons.setComponentAlignment(optionBtn,
					Alignment.MIDDLE_CENTER);
		}

		layout.addComponent(editButtons);
		layout.setComponentAlignment(editButtons, Alignment.MIDDLE_CENTER);
		layout.setExpandRatio(editButtons, 1.0f);

		if ((buttonEnableFlags & NAVIGATOR_BTN_PRESENTED) == NAVIGATOR_BTN_PRESENTED) {
			ButtonGroup navigationBtns = new ButtonGroup();
			navigationBtns.setStyleName("navigation-btns");
			Button previousItem = new Button("<", new Button.ClickListener() {
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

			Button nextItemBtn = new Button(">", new Button.ClickListener() {
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
			layout.addComponent(navigationBtns);
			layout.setComponentAlignment(navigationBtns, Alignment.MIDDLE_RIGHT);
		}

		if (permissionItem != null) {
			final boolean canWrite = CurrentProjectVariables
					.canWrite(permissionItem);
			final boolean canAccess = CurrentProjectVariables
					.canAccess(permissionItem);

			if (assignBtn != null) {
				assignBtn.setEnabled(canWrite);
			}

			if (addBtn != null) {
				addBtn.setEnabled(canWrite);
			}

			if (editBtn != null) {
				editBtn.setEnabled(canWrite);
			}

			if (cloneBtn != null) {
				cloneBtn.setEnabled(canWrite);
			}

			if (deleteBtn != null) {
				deleteBtn.setEnabled(canAccess);
			}
		}
		return layout;
	}

	/**
	 * 
	 * @param comp
	 */
	public void insertToControlBlock(Component comp) {
		editButtons.addComponent(comp, 0);
	}

	public void addOptionButton(Button button) {
		button.setStyleName(UIConstants.THEME_LINK);
		button.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 2710916670115028630L;

			@Override
			public void buttonClick(ClickEvent event) {
				optionBtn.setPopupVisible(false);
			}
		});
		popupButtonsControl.addComponent(button);
	}

	public HorizontalLayout createButtonControls(final String permissionItem) {
		return createButtonControls(ADD_BTN_PRESENTED | EDIT_BTN_PRESENTED
				| DELETE_BTN_PRESENTED | CLONE_BTN_PRESENTED
				| NAVIGATOR_BTN_PRESENTED, permissionItem);
	}
}
