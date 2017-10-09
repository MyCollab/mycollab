package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-setting")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ProjectSettingI18nEnum {
    VIEW_TITLE,
    WIDGET_CUSTOMIZE_FEATURES,
    DIALOG_UPDATE_SUCCESS,
    OPT_DEFAULT_SETTING,
    OPT_NONE_SETTING,
    OPT_MINIMUM_SETTING,
    OPT_MAXIMUM_SETTING,

    OPT_APPLY_TO_ALL_PROJECTS
}
