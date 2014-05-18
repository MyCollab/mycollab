package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/follower")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum FollowerI18nEnum {
	FOLLOWER_NAME, FOLLOWER_CREATE_DATE, FORM_PROJECT_NAME, FORM_SUMMARY
}
