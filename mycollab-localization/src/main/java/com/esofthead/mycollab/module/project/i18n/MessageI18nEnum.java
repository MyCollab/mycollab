package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/message")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum MessageI18nEnum {
	LIST_VIEW_TITLE,
	
	NEW_MESSAGE_ACTION,
	FORM_TITLE_FIELD,
	FORM_IS_STICK_FIELD,
	FORM_ATTACHMENT_FIELD,
	FORM_TITLE_REQUIRED_ERROR,
	
	USER_COMMENT_ADD,
	
	NO_ITEM_VIEW_TITLE,
	NO_ITEM_VIEW_HINT,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
