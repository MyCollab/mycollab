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

	public AbstractNavigationMenu() {
		super();

		setWidth("100%");
		setStyleName("navigation-menu");

		defaultBtnClickListener = createDefaultButtonClickListener();
	}

	protected class MenuButton extends Button {
		private static final long serialVersionUID = -2516191547029466932L;

		private final String btnId;

		public MenuButton(String caption, String iconCode) {
			super(
					"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
							+ iconCode
							+ "\"></span><div class=\"screen-reader-text\">"
							+ caption + "</div>", defaultBtnClickListener);
			setStyleName("nav-btn");
			setHtmlContentAllowed(true);
			setWidth("100%");
			btnId = caption;
		}

		public String getBtnId() {
			return btnId;
		}
	}

	protected abstract Button.ClickListener createDefaultButtonClickListener();
}
