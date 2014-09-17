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
package com.esofthead.mycollab.mobile.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.esofthead.vaadin.mobilecomponent.NavigationMenuButton;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public abstract class AbstractNavigationMenu extends CssLayout {
	private static final long serialVersionUID = -8517225259459579426L;

	protected final Button.ClickListener defaultBtnClickListener;

	protected final Map<String, MenuButton> buttonMap = new HashMap<String, MenuButton>();

	public AbstractNavigationMenu() {
		super();

		setWidth("100%");
		setStyleName("navigation-menu");

		defaultBtnClickListener = createDefaultButtonClickListener();
	}

	public static class MenuButton extends Button {
		private static final long serialVersionUID = -2516191547029466932L;

		private final String btnId;

		public MenuButton(String caption, String iconCode) {
			super(
					"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
							+ iconCode + "\"></span>" + caption);
			setStyleName("nav-btn");
			setHtmlContentAllowed(true);
			setWidth("100%");
			btnId = caption;
			NavigationMenuButton.extend(this);
		}

		public String getBtnId() {
			return btnId;
		}
	}

	public void addMenu(MenuButton newMenu) {
		newMenu.addClickListener(defaultBtnClickListener);
		addComponent(newMenu);
		buttonMap.put(newMenu.getBtnId(), newMenu);
	}

	public void selectButton(String caption) {
		for (final Iterator<MenuButton> it = this.buttonIterator(); it
				.hasNext();) {
			final MenuButton btn = it.next();
			btn.removeStyleName("isSelected");
			if (btn.getBtnId().equals(caption)) {
				btn.addStyleName("isSelected");
			}
		}
	}

	protected Iterator<MenuButton> buttonIterator() {
		return buttonMap.values().iterator();
	}

	protected abstract Button.ClickListener createDefaultButtonClickListener();
}
