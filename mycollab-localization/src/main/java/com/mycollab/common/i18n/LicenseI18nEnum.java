package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-license")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum LicenseI18nEnum {
    ACTION_BUY_LICENSE,
    ACTION_ENTER_LICENSE,
    ACTION_CHANGE_LICENSE,
    OPT_LICENSE_EXPIRE_DATE,
    OPT_LICENSE_EXPIRE_SOON_DATE,
    OPT_LICENSE_VALID_TO_DATE,
    OPT_ACTIVATION_CODE,
    OPT_BROWSE_LICENSE_HELP,
    OPT_LICENSE_ACTIVATED,
    OPT_TRIAL_THE_PRO_EDITION,
    OPT_BUY_LICENSE,
    FORM_ORGANIZATION,
    FORM_ISSUE_DATE,
    FORM_EXPIRE_DATE,
    FORM_MAX_USERS,
    EXPIRE_NOTIFICATION,
    TRIAL_NOTIFICATION,
    ERROR_LICENSE_INVALID,
    ERROR_LICENSE_FILE_INVALID,
    FEATURE_NOT_AVAILABLE,
}
