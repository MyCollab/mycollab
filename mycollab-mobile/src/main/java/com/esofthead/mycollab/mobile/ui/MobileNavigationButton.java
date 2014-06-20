package com.esofthead.mycollab.mobile.ui;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.2
 * 
 */
public class MobileNavigationButton extends NavigationButton {
	private static final long serialVersionUID = 5591853157860002587L;

	public MobileNavigationButton() {
		super();
	}

	public MobileNavigationButton(String caption) {
		super(caption);
	}

	public MobileNavigationButton(Component targetView) {
		super(targetView);
	}

	public MobileNavigationButton(String caption, Component targetView) {
		super(caption, targetView);
	}

	@Override
	public void beforeClientResponse(boolean initial) {
		// Override to stop auto set caption for button
	}
}
