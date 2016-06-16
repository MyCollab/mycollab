/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.i18n;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyorExt;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.FileUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

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
    public static final Locale defaultLocale = Locale.US;

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
        } catch (MissingResourceException e) {
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

    private static Map<String, String> cacheFile = new HashMap<>();

    private static String templatePath(String fileTemplatePath, Locale locale) {
        String key = (locale != null) ? (fileTemplatePath + locale.toLanguageTag()) : (fileTemplatePath + Locale.US.toLanguageTag().toString());
        String filePath = cacheFile.get(key);
        if (filePath != null) {
            return filePath;
        } else {
            int index = fileTemplatePath.indexOf("html");
            if (index == -1) {
                throw new MyCollabException("File type is not supported " + fileTemplatePath);
            }
            filePath = fileTemplatePath.substring(0, index - 1);
            filePath = String.format("%s_%s.html", filePath, locale.toLanguageTag());
            cacheFile.put(key, filePath);
            return filePath;
        }
    }

    /**
     * @param fileTemplatePath the path of template file
     * @param locale           locale associates with the template file
     * @return the reader of <code>fileTemplatePath</code> if it is found.
     * Otherwise, return null
     */
    public static Reader templateReader(String fileTemplatePath, Locale locale) {
        String templatePath = templatePath(fileTemplatePath, locale);
        File i18nFile = FileUtils.getDesireFile(new File(FileUtils.getUserFolder(), "i18n"), templatePath);

        InputStream resourceStream;
        if (i18nFile != null) {
            try {
                resourceStream = new FileInputStream(i18nFile);
            } catch (FileNotFoundException e) {
                resourceStream = LocalizationHelper.class.getClassLoader().getResourceAsStream(templatePath);
            }
        } else {
            resourceStream = LocalizationHelper.class.getClassLoader().getResourceAsStream(templatePath);
        }

        if (resourceStream == null) {
            return null;
        }

        try {
            return new InputStreamReader(resourceStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new MyCollabException("Not support UTF-8 encoding");
        }
    }

    public static Reader templateReader(String fileTemplatePath, Locale currentLocale, Locale defaultLocale) {
        Reader reader = templateReader(fileTemplatePath, currentLocale);
        if (reader == null) {
            if (defaultLocale == null) {
                throw new MyCollabException("Can not find file " + fileTemplatePath + " in locale " + currentLocale);
            }
            reader = templateReader(fileTemplatePath, defaultLocale);
            if (reader == null) {
                throw new MyCollabException("Can not find file " + fileTemplatePath + " in locale " +
                        currentLocale + " and default locale " + defaultLocale);
            }
        }
        return reader;
    }

    public static final Locale[] getAvailableLocales() {
        return Locale.getAvailableLocales();
    }

    public final static Locale getLocaleInstance(String languageTag) {
        try {
            return (languageTag == null) ? Locale.US : Locale.forLanguageTag(languageTag);
        } catch (Exception e) {
            LOG.error("Invalid language {}", languageTag);
            return Locale.US;
        }
    }

    public static final Enum localizeYesNo(Boolean value) {
        return Boolean.TRUE.equals(value) ? GenericI18Enum.BUTTON_YES : GenericI18Enum.BUTTON_NO;
    }
}
