package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("common-webexception")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum ErrorI18nEnum {
    SUB_DOMAIN_IS_NOT_EXISTED,
    NOT_SUPPORT_SENDING_EMAIL_TO_ALL_USERS,
    ERROR_USER_IS_NOT_EXISTED,
    EXISTING_DOMAIN_REGISTER_ERROR,
    FIELD_MUST_NOT_NULL,
    NO_ACCESS_PERMISSION,
    NOT_VALID_EMAIL,
    SELECT_AT_LEAST_ONE_CRITERIA,
    QUERY_SEARCH_IS_INVALID,
    INVALID_FORMAT,
    RESOURCE_NOT_FOUND,
    BROWSER_OUT_UP_DATE,
    WEBSOCKET_NOT_SUPPORT,
    ERROR_ESTABLISH_DATABASE_CONNECTION
}
