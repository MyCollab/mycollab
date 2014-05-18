package com.esofthead.mycollab.module.project.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("localization/project/version")
@LocaleData({ @Locale("en_US"), @Locale("ja_JP") })
public enum VersionI18nEnum {
	FORM_NAME, FORM_DESCRIPTION
}
