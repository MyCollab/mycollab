package com.mycollab.i18n;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyorExt;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Wrapper class to get localization string.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LocalizationHelper {
    private static final Logger LOG = LoggerFactory.getLogger(LocalizationHelper.class);
    private static final Map<Locale, IMessageConveyor> languageMap;
    private static IMessageConveyor defaultMessage = new MessageConveyorExt(Locale.US);

    static {
        languageMap = new HashMap<>();
        languageMap.put(Locale.US, defaultMessage);
    }

    public static IMessageConveyor getMessageConveyor(Locale language) {
        if (language == null) {
            return languageMap.get(Locale.US);
        } else {
            IMessageConveyor messageConveyor = languageMap.get(language);
            if (messageConveyor == null) {
                messageConveyor = new MessageConveyorExt(language);
                languageMap.put(language, messageConveyor);
            }

            return messageConveyor;
        }
    }

    public static String getMessage(Locale locale, Enum<?> key, Object... objects) {
        try {
            IMessageConveyor messageConveyor = getMessageConveyor(locale);
            return messageConveyor.getMessage(key, objects);
        } catch (Exception e) {
            return defaultMessage.getMessage(key, objects);
        }
    }

    public static String getMessage(Locale locale, Class cls, String option, Object... objects) {
        if (StringUtils.isBlank(option)) {
            return "";
        }
        Enum key = null;
        try {
            key = Enum.valueOf(cls, option);
            IMessageConveyor messageConveyor = getMessageConveyor(locale);
            return messageConveyor.getMessage(key, objects);
        } catch (Exception e) {
            if (key != null) {
                try {
                    return defaultMessage.getMessage(key, objects);
                } catch (Exception e1) {
                    LOG.debug("Can not find resource key " + cls + "---" + option, e);
                    return "Undefined";
                }
            } else {
                LOG.debug("Error", e);
                return "Undefined";
            }
        }
    }

    public static Locale[] getAvailableLocales() {
        return Locale.getAvailableLocales();
    }

    public static Locale getLocaleInstance(String languageTag) {
        try {
            if (languageTag == null) return Locale.US;

            Locale tmpLocale = Locale.forLanguageTag(languageTag);
            return StringUtils.isBlank(tmpLocale.getLanguage()) ? Locale.US : tmpLocale;
        } catch (Exception e) {
            LOG.error("Invalid language {}", languageTag);
            return Locale.US;
        }
    }

    public static Enum localizeYesNo(Boolean value) {
        return Boolean.TRUE.equals(value) ? GenericI18Enum.BUTTON_YES : GenericI18Enum.BUTTON_NO;
    }
}
