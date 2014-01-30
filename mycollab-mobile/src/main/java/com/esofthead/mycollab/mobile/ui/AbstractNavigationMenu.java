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
							+ iconCode + "\"></span>" + caption,
					defaultBtnClickListener);
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
