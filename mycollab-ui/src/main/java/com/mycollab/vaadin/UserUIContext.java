/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin;

import ch.qos.cal10n.IMessageConveyor;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.SessionExpireException;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.dao.UserAccountMapper;
import com.mycollab.module.user.domain.*;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.MyCollabSession;
import com.vaadin.server.VaadinSession;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.mycollab.vaadin.ui.MyCollabSession.*;

/**
 * The core class that keep user session data while user login to MyCollab
 * successfully. We use thread local pattern to keep App context instance of
 * every user, so in current thread you can use static methods of UserUIContext to
 * get current user without fearing it impacts to other user sessions logging in
 * MyCollab system.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserUIContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(UserUIContext.class);

    /**
     * Current user LOG in to MyCollab
     */
    private SimpleUser session;

    /**
     * Billing information of account of current user
     */
    private SimpleBillingAccount billingAccount;

    private transient IMessageConveyor messageHelper;
    private Locale userLocale = Locale.US;
    private TimeZone userTimeZone;
    private Boolean isValidAccount = true;

    public UserUIContext() {
        MyCollabSession.putCurrentUIVariable("context", this);
    }

    /**
     * Get context of current logged in user
     *
     * @return context of current logged in user
     */
    public static UserUIContext getInstance() {
        try {
            UserUIContext context = (UserUIContext) MyCollabSession.getCurrentUIVariable("context");
            if (context == null) {
                throw new SessionExpireException("Session is expired");
            }
            return context;
        } catch (Exception e) {
            throw new SessionExpireException("Session is expired");
        }
    }

    /**
     * Update last module visit then the next sign in, MyCollab will lead user
     * to last visit module
     *
     * @param moduleName
     */
    public static void updateLastModuleVisit(String moduleName) {
        try {
            UserAccountMapper userAccountMapper = AppContextUtil.getSpringBean(UserAccountMapper.class);
            UserAccount userAccount = new UserAccount();
            userAccount.setLastmodulevisit(moduleName);
            UserAccountExample ex = new UserAccountExample();
            ex.createCriteria().andAccountidEqualTo(AppUI.getAccountId()).andUsernameEqualTo(UserUIContext.getUsername());
            userAccountMapper.updateByExampleSelective(userAccount, ex);
        } catch (Exception e) {
            LOG.error("There is error when try to update user preference for last module visit", e);
        }
    }

    /**
     * Keep user session in server sessions
     *
     * @param userSession current user
     * @param billingAc   account information of current user
     */
    public void setSessionVariables(SimpleUser userSession, SimpleBillingAccount billingAc) {
        session = userSession;
        billingAccount = billingAc;

        String language = session.getLanguage();
        userLocale = language != null ? LocalizationHelper.getLocaleInstance(language) : billingAccount.getLocaleInstance();
        VaadinSession.getCurrent().setLocale(userLocale);
        messageHelper = LocalizationHelper.getMessageConveyor(userLocale);

        userTimeZone = TimezoneVal.valueOf(session.getTimezone());
        MyCollabSession.putSessionVariable(MyCollabSession.USER_VAL, userSession);
    }

    public boolean isMatchAccount(Integer sAccountId) {
        return sAccountId.equals(AppUI.getAccountId());
    }

    public void clearSessionVariables() {
        session = null;
        billingAccount = null;
        MyCollabSession.removeSessionVariable(MyCollabSession.USER_VAL);
        MyCollabSession.removeCurrentUIVariable(MyCollabSession.PRESENTER_VAL);
        MyCollabSession.removeCurrentUIVariable(MyCollabSession.VIEW_MANAGER_VAL);
    }

    public void setIsValidAccount(Boolean isValidAccount) {
        this.isValidAccount = isValidAccount;
    }

    public Boolean getIsValidAccount() {
        return isValidAccount;
    }

    public static Locale getUserLocale() {
        return getInstance().userLocale;
    }

    public static String getMessage(Enum<?> key, Object... objects) {
        try {
            return (key != null) ? getInstance().messageHelper.getMessage(key, objects) : "";
        } catch (Exception e) {
            return LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale(), key, objects);
        }
    }

    public static String getMessage(Class<? extends Enum> enumCls, String option, Object... objects) {
        try {
            if (option == null)
                return "";
            Enum key = Enum.valueOf(enumCls, option);
            return getMessage(key, objects);
        } catch (Exception e) {
            return option;
        }
    }

    /**
     * Get current user in session
     *
     * @return current user in session
     */
    public static SimpleUser getUser() {
        return getInstance().session;
    }


    /**
     * Get username of current user
     *
     * @return username of current user
     */
    public static String getUsername() {
        try {
            return getInstance().session.getUsername();
        } catch (Exception e) {
            throw new SessionExpireException("Can not get username of the current session");
        }
    }

    /**
     * Get avatar id of current user
     *
     * @return avatar id of current user
     */
    public static String getUserAvatarId() {
        return getInstance().session.getAvatarid();
    }

    public static String getUserDisplayName() {
        return getInstance().session.getDisplayName();
    }

    public static final TimeZone getUserTimeZone() {
        return getInstance().userTimeZone;
    }

    /**
     * Check whether current user is admin or system
     *
     * @return
     */
    public static boolean isAdmin() {
        try {
            Boolean isAdmin = getInstance().session.getIsAccountOwner();
            return Boolean.TRUE.equals(isAdmin);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param permissionItem
     * @return
     */
    public static boolean canBeYes(String permissionItem) {
        if (isAdmin()) {
            return true;
        }

        PermissionMap permissionMap = getInstance().session.getPermissionMaps();
        return permissionMap != null && permissionMap.canBeYes(permissionItem);
    }

    /**
     * @param permissionItem
     * @return
     */
    public static boolean canBeFalse(String permissionItem) {
        if (isAdmin()) {
            return true;
        }

        PermissionMap permissionMap = getInstance().session.getPermissionMaps();
        return permissionMap != null && permissionMap.canBeFalse(permissionItem);
    }

    /**
     * @param permissionItem
     * @return
     */
    public static boolean canRead(String permissionItem) {
        if (isAdmin()) {
            return true;
        }

        PermissionMap permissionMap = getInstance().session.getPermissionMaps();
        return permissionMap != null && permissionMap.canRead(permissionItem);
    }

    /**
     * @param permissionItem
     * @return
     */
    public static boolean canWrite(String permissionItem) {
        if (isAdmin()) {
            return true;
        }
        PermissionMap permissionMap = getInstance().session.getPermissionMaps();
        return permissionMap != null && permissionMap.canWrite(permissionItem);
    }

    /**
     * @param permissionItem
     * @return
     */
    public static boolean canAccess(String permissionItem) {
        if (isAdmin()) {
            return true;
        }
        PermissionMap permissionMap = getInstance().session.getPermissionMaps();
        return permissionMap != null && permissionMap.canAccess(permissionItem);
    }

    /**
     * Get permission map of current user
     *
     * @return permission map of current user
     */
    public static PermissionMap getPermissionMap() {
        return getInstance().session.getPermissionMaps();
    }

    public static String getPermissionCaptionValue(PermissionMap permissionMap, String permissionItem) {
        Integer perVal = permissionMap.get(permissionItem);
        return UserUIContext.getMessage(PermissionFlag.toVal(perVal));
    }

    /**
     * @param date is the UTC date value
     * @return
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        } else {
            DateTime jodaDate = new DateTime(date).toDateTime(DateTimeZone.forTimeZone(UserUIContext.getUserTimeZone()));
            if (jodaDate.getHourOfDay() > 0 || jodaDate.getMinuteOfHour() > 0) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(AppUI.getDateTimeFormat()).withLocale(UserUIContext.getUserLocale());
                return formatter.print(jodaDate);
            } else {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(AppUI.getDateFormat()).withLocale(UserUIContext.getUserLocale());
                return formatter.print(jodaDate);
            }
        }
    }

    /**
     * @param date is the UTC date value
     * @return
     */
    public static String formatDate(Date date) {
        return date == null ? "" : DateTimeUtils.formatDate(date, AppUI.getDateFormat(), UserUIContext.getUserLocale(),
                UserUIContext.getUserTimeZone());
    }

    /**
     * @param date
     * @param textIfDateIsNull
     * @return
     */
    public static String formatDate(Date date, String textIfDateIsNull) {
        return date == null ? textIfDateIsNull : formatDate(date);
    }

    public static String formatPrettyTime(Date date) {
        return DateTimeUtils.getPrettyDateValue(date, getUserLocale());
    }

    public static String formatShortDate(Date date) {
        return date == null ? "" : DateTimeUtils.formatDate(date, AppUI.getShortDateFormat(), UserUIContext.getUserLocale(),
                UserUIContext.getUserTimeZone());
    }

    public static String formatDuration(Date date) {
        return DateTimeUtils.getPrettyDurationValue(date, getUserLocale());
    }

    /**
     * @param hour
     * @return
     */
    public static String formatTime(double hour) {
        long hourCount = (long) Math.floor(hour);
        long minuteCount = (long) ((hourCount - hour) * 60);

        String timeFormat = getMessage(DayI18nEnum.TIME_FORMAT);
        String[] patterns = timeFormat.split(":");
        String output = "";

        String hourSuffix = getMessage(DayI18nEnum.HOUR_SUFFIX);
        String hourPluralSuffix = getMessage(DayI18nEnum.HOUR_PLURAL_SUFFIX);

        String minuteSuffix = getMessage(DayI18nEnum.MINUTE_SUFFIX);
        String minutePluralSuffix = getMessage(DayI18nEnum.MINUTE_PLURAL_SUFFIX);
        for (String pattern : patterns) {
            if (pattern.equals("H") && hourCount > 0) {
                output += hourCount;
                output += (hourCount > 1 ? hourPluralSuffix : hourSuffix);
            } else if (pattern.equals("m") && minuteCount > 0) {
                output += minuteCount;
                output += (minuteCount > 1 ? minutePluralSuffix : minuteSuffix);
            }
        }
        return output;
    }

}
