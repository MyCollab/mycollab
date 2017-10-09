package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-security")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum SecurityI18nEnum {
    NO_ACCESS, READONLY, READ_WRITE, ACCESS, YES, NO, UNDEFINE, ACCESS_PERMISSION_HELP, BOOLEAN_PERMISSION_HELP;

    public Enum desc() {
        if (this == NO_ACCESS || this == READONLY || this == READ_WRITE || this == ACCESS) {
            return ACCESS_PERMISSION_HELP;
        } else if (this == YES || this == NO) {
            return BOOLEAN_PERMISSION_HELP;
        }
        return UNDEFINE;
    }
}
