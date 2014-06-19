package com.esofthead.mycollab.vaadin.ui;

import java.util.List;

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class I18nValueComboBox extends ComboBox {
	private static final long serialVersionUID = 1L;

	public I18nValueComboBox() {
		super();
		this.setPageLength(20);
	}

	public final void loadData(List<? extends Enum> values) {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

		for (Enum entry : values) {
			this.addItem(entry.name());
			this.setItemCaption(entry.name(), AppContext.getMessage(entry));
		}

		if (!this.isNullSelectionAllowed()) {
			this.select(this.getItemIds().iterator().next());
		}
	}
}
