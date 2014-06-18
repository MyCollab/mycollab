package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/version")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum VersionI18nEnum {
	LIST_VIEW_TITLE,
	VIEW_NO_ITEM_TITLE,
	VIEW_NO_ITEM_HINT,

	FORM_NEW_TITLE,
	FORM_EDIT_TITLE,
	FORM_NAME,
	FORM_DESCRIPTION,
	FORM_STATUS,
	FORM_DUE_DATE,
	FORM_VERSION_ERROR_MSG,

	TAB_RELATED_BUGS,
	
	MAIL_CREATE_ITEM_SUBJECT,
	MAIL_UPDATE_ITEM_SUBJECT,
	MAIL_COMMENT_ITEM_SUBJECT
}
