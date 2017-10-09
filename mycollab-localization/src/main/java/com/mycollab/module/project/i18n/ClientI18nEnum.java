package com.mycollab.module.project.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("project-client")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ClientI18nEnum {
    NEW,
    DETAIL,
    LIST,
    SINGLE,
    EDIT,

    OPT_NUM_PROJECTS,
    OPT_REMOVE_CLIENT
}
