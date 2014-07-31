package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class ButtonI18nComp extends Button {
	private static final long serialVersionUID = 1L;

	private String key;

	public ButtonI18nComp(String key, Enum<?> caption,
			Button.ClickListener listener) {
		this.key = key;
		this.setCaption(AppContext.getMessage(caption));
		this.addClickListener(listener);
	}

	public String getKey() {
		return key;
	}
}
