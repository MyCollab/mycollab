package com.esofthead.mycollab.vaadin.ui;

import java.util.List;

import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.ListSelect;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public class I18nValueListSelect extends ListSelect {
	private static final long serialVersionUID = 1L;

	public void loadData(List<? extends Enum> values) {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

		for (Enum entry : values) {
			this.addItem(entry.name());
			this.setItemCaption(entry.name(), AppContext.getMessage(entry));
		}

		this.setRows(4);
		this.setMultiSelect(true);
	}
}
