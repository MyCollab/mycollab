package com.esofthead.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/follower")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum FollowerI18nEnum {
	LABEL_MY_FOLLOWING_TICKETS,
	BUTTON_BACK_TO_WORKBOARD,
	FOLLOWER_NAME,
	FOLLOWER_CREATE_DATE,
	FORM_PROJECT_NAME,
	FORM_SUMMARY
}
