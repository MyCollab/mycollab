package com.esofthead.mycollab.module.user.ui.components;

import com.esofthead.mycollab.common.localization.LangI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class LanguageComboBox extends ComboBox {
	private static final long serialVersionUID = 1L;

	public LanguageComboBox() {
		super();
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

		this.addItem(LocalizationHelper.ENGLISH);
		this.setItemCaption(LocalizationHelper.ENGLISH,
				AppContext.getMessage(LangI18Enum.ENGLISH));

		this.addItem(LocalizationHelper.JAPANESE);
		this.setItemCaption(LocalizationHelper.JAPANESE,
				AppContext.getMessage(LangI18Enum.JAPANESE));

		this.addItem(LocalizationHelper.SPANISH);
		this.setItemCaption(LocalizationHelper.SPANISH,
				AppContext.getMessage(LangI18Enum.SPANISH));
	}
}
