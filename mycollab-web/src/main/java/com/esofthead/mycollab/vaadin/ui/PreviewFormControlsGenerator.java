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
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 * @param <B>
 */
public class PreviewFormControlsGenerator<B> {

	private AdvancedPreviewBeanForm<B> previewForm;
	// private Button backBtn;
	private Button editBtn;
	private Button deleteBtn;
	private Button cloneBtn;
	private SplitButton optionBtn;
	private Button optionParentBtn;
	private VerticalLayout popupButtonsControl;
	private HorizontalLayout editButtons;
	private HorizontalLayout layout;

	public PreviewFormControlsGenerator(AdvancedPreviewBeanForm<B> editForm) {
		this.previewForm = editForm;
	}

	public HorizontalLayout createButtonControls(String permissionItem) {
		layout = new HorizontalLayout();
		layout.setStyleName("control-buttons");
		layout.setSpacing(true);
		layout.setSizeUndefined();

		optionParentBtn = new Button("Option", new Button.ClickListener() {
			private static final long serialVersionUID = 695008443208333680L;

			@Override
			public void buttonClick(ClickEvent event) {
				optionBtn.setPopupVisible(true);
			}
		});

		optionBtn = new SplitButton(optionParentBtn);
		optionBtn.setWidthUndefined();
		optionBtn.addStyleName(UIConstants.THEME_GRAY_LINK);

		popupButtonsControl = new VerticalLayout();
		popupButtonsControl.setWidth("100px");
		popupButtonsControl.setMargin(new MarginInfo(false, true, false, true));
		popupButtonsControl.setSpacing(true);

		editButtons = new HorizontalLayout();
		editButtons.setSpacing(true);

		editBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						optionBtn.setPopupVisible(false);
						B item = previewForm.getBean();
						previewForm.fireEditForm(item);
					}
				});
		editBtn.setIcon(MyCollabResource.newResource(WebResourceIds._16_edit));
		editBtn.setStyleName("link");
		popupButtonsControl.addComponent(editBtn);

		deleteBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_DELETE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						B item = previewForm.getBean();
						previewForm.fireDeleteForm(item);
					}
				});
		deleteBtn.setIcon(MyCollabResource.newResource(WebResourceIds._16_delete2));
		deleteBtn.setStyleName(UIConstants.THEME_RED_LINK);
		editButtons.addComponent(deleteBtn);
		editButtons.setComponentAlignment(deleteBtn, Alignment.MIDDLE_CENTER);

		cloneBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CLONE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						optionBtn.setPopupVisible(false);
						B item = previewForm.getBean();
						previewForm.fireCloneForm(item);
					}
				});
		cloneBtn.setIcon(MyCollabResource.newResource(WebResourceIds._16_clone));
		cloneBtn.setStyleName("link");
		popupButtonsControl.addComponent(cloneBtn);

		optionBtn.setContent(popupButtonsControl);
		editButtons.addComponent(optionBtn);
		editButtons.setComponentAlignment(optionBtn, Alignment.MIDDLE_CENTER);

		layout.addComponent(editButtons);
		layout.setComponentAlignment(editButtons, Alignment.MIDDLE_CENTER);
		layout.setExpandRatio(editButtons, 1.0f);

		if (permissionItem != null) {
			boolean canWrite = AppContext.canWrite(permissionItem);
			boolean canAccess = AppContext.canAccess(permissionItem);

			editBtn.setEnabled(canWrite);
			cloneBtn.setEnabled(canWrite);
			deleteBtn.setEnabled(canAccess);
		}
		return layout;
	}

	public void removeCloneButton() {
		popupButtonsControl.removeComponent(cloneBtn);
	}

	public HorizontalLayout getLayout() {
		return layout;
	}

	public void setDeleteButtonVisible(boolean visible) {
		deleteBtn.setVisible(visible);
	}
}
