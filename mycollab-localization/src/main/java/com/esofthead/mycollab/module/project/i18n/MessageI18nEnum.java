package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/message")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum MessageI18nEnum {
	VIEW_LIST_TITLE,
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,
	
	BUTTON_NEW_MESSAGE,
	
	FORM_TITLE,
	FORM_IS_STICK,
	FORM_ATTACHMENT_FIELD,
	FORM_TITLE_REQUIRED_ERROR,
	
	USER_COMMENT_ADD,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
