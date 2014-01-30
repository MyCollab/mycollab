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

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class PopupButtonControl extends SplitButton implements
		HasMassItemActionHandlers {

	private static final long serialVersionUID = 1L;
	private VerticalLayout selectContent;
	private Set<MassItemActionHandler> handlers;

	public PopupButtonControl(final String id, final Button button) {
		super(button);

		addStyleName(UIConstants.THEME_GRAY_LINK);

		initPopupButton(id, button.getCaption());
	}

	private void initPopupButton(final String id, final String defaultName) {
		this.setData(id);

		this.addClickListener(new SplitButtonClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void splitButtonClick(SplitButtonClickEvent event) {
				changeOption(id);
				PopupButtonControl.this.setPopupVisible(false);
			}
		});

		selectContent = new VerticalLayout();
		selectContent.setWidth("100px");
		this.setContent(selectContent);
	}

	public void addOptionItem(final String id, final String name) {
		this.addOptionItem(id, name, true);
	}

	public void addOptionItem(final String id, final String name,
			final boolean isEnable) {
		final Button optionBtn = new Button(name, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				changeOption(id);
				PopupButtonControl.this.setPopupVisible(false);
			}
		});
		optionBtn.addStyleName("link");
		optionBtn.setEnabled(isEnable);
		selectContent.addComponent(optionBtn);
	}

	private void changeOption(String id) {
		if (handlers != null) {
			for (MassItemActionHandler handler : handlers) {
				handler.onSelect(id);
			}
		}
	}

	@Override
	public void addMassItemActionHandler(MassItemActionHandler handler) {
		if (handlers == null) {
			handlers = new HashSet<MassItemActionHandler>();
		}
		handlers.add(handler);

	}
}
