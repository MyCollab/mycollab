package com.esofthead.mycollab.common.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.1
 * 
 */
@BaseName("localization/file")
@LocaleData({ @Locale("en_US") })
public enum FileI18nEnum {
	EXCEL, PDF, CSV, IMPORT_FILE, EXPORT_FILE, NOT_ATTACH_FILE_WARNING, IMPORT_FILE_SUCCESS
}
