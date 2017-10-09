package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-theme")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ThemeI18nEnum {
    OPT_BUTTONS,
    OPT_BUTTON,
    OPT_SELECTED_MENU_TEXT,
    OPT_SELECTED_MENU,
    OPT_NORMAL_MENU_TEXT,
    OPT_NORMAL_MENU,
    OPT_VERTICAL_MENU,
    OPT_THEME_CUSTOMIZATION
}
