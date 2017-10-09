package com.mycollab.common.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyColab Ltd
 * @since 5.4.1
 */
public class QueryI18nEnum {
    @BaseName("common-querytring")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum StringI18nEnum {
        IS,
        IS_NOT,
        CONTAINS,
        NOT_CONTAINS,
        IS_EMPTY,
        IS_NOT_EMPTY
    }

    @BaseName("common-querynumber")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum NumberI18nEnum {
        EQUAL,
        NOT_EQUAL,
        LESS_THAN,
        LESS_THAN_EQUAL,
        GREATER_THAN,
        GREATER_THAN_EQUAL,
        IS_EMPTY,
        IS_NOT_EMPTY
    }

    @BaseName("common-querycollection")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CollectionI18nEnum {
        IN,
        NOT_IN
    }
}
