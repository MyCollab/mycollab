/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin;

import ch.qos.cal10n.IMessageConveyor;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.SessionExpireException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.billing.SubDomainNotExistException;
import com.mycollab.module.user.dao.UserAccountMapper;
import com.mycollab.module.user.domain.*;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.security.PermissionFlag;
import com.mycollab.security.PermissionMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.MyCollabSession;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.mycollab.vaadin.ui.MyCollabSession.*;

/**
 * The core class that keep user session data while user login to MyCollab
 * successfully. We use thread local pattern to keep App context instance of
 * every user, so in current thread you can use static methods of AppContext to
 * get current user without fearing it impacts to other user sessions logging in
 * MyCollab system.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AppContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(AppContext.class);

    /**
     * Current user LOG in to MyCollab
     */
    private SimpleUser session;

    /**
     * Billing information of account of current user
     */
    private SimpleBillingAccount billingAccount;

    /**
     * Subdomain associates with account of current user. This value is valid
     * only for on-demand edition
     */
    private String subDomain;
    private String siteName;

    /**
     * id of account of current user. This value is valid only for on-demand
     * edition. Though other editions also use this id in all of queries but if
     * you have two different account ids in system may cause abnormal issues
     */
    private Integer accountId = null;
    private transient IMessageConveyor messageHelper;
    private Locale userLocale = Locale.US;
    private TimeZone userTimeZone;
    private Boolean isValidAccount = true;

    public AppContext() {
        MyCollabSession.putCurrentUIVariable("context", this);
    }

    /**
     * Get context of current logged in user
     *
     * @return context of current logged in user
     */
    public static AppContext getInstance() {
        try {
            AppContext context = (AppContext) MyCollabSession.getCurrentUIVariable("context");
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
            ex.createCriteria().andAccountidEqualTo(AppContext.getAccountId()).andUsernameEqualTo(AppContext.getUsername());
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
        MyCollabSession.putSessionVariable(USER_VAL, userSession);
    }

    public boolean isMatchAccount(Integer sAccountId) {
        return sAccountId.equals(accountId);
    }

    public void clearSessionVariables() {
        session = null;
        billingAccount = null;
        MyCollabSession.removeSessionVariable(USER_VAL);
        MyCollabSession.removeCurrentUIVariable(PRESENTER_VAL);
        MyCollabSession.removeCurrentUIVariable(VIEW_MANAGER_VAL);
    }

    public void setIsValidAccount(Boolean isValidAccount) {
        this.isValidAccount = isValidAccount;
    }

    public Boolean getIsValidAccount() {
        return isValidAccount;
    }

    public static String getSiteName() {
        try {
            return getInstance().siteName;
        } catch (Exception e) {
            return "MyCollab";
        }
    }

    public static Locale getUserLocale() {
        return getInstance().userLocale;
    }

    public static String getMessage(Enum<?> key, Object... objects) {
        try {
            return (key != null) ? getInstance().messageHelper.getMessage(key, objects) : "";
        } catch (Exception e) {
            return LocalizationHelper.getMessage(LocalizationHelper.defaultLocale, key, objects);
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
     * Start application by query account base on <code>domain</code>
     *
     * @param domain associate with current user logged in.
     */
    public void initDomain(String domain) {
        this.subDomain = domain;
        BillingAccountService billingService = AppContextUtil.getSpringBean(BillingAccountService.class);
        BillingAccount account = billingService.getAccountByDomain(domain);

        if (account == null) {
            throw new SubDomainNotExistException(AppContext.getMessage(ErrorI18nEnum.SUB_DOMAIN_IS_NOT_EXISTED, domain));
        } else {
            if (StringUtils.isBlank(account.getSitename())) {
                siteName = SiteConfiguration.getDefaultSiteName();
            } else {
                siteName = account.getSitename();
            }

            LOG.debug("Get billing account {} of subDomain {}", BeanUtility.printBeanObj(account), domain);
            accountId = account.getId();
        }
    }

    /**
     * Get account id of current user
     *
     * @return account id of current user. Return 0 if can not get
     */
    public static Integer getAccountId() {
        try {
            return getInstance().accountId;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get subDomain of current user
     *
     * @return subDomain of current user
     */
    public static String getSubDomain() {
        return getInstance().subDomain;
    }

    private String siteUrl = null;

    /**
     * @return
     */
    public static String getSiteUrl() {
        if (getInstance().siteUrl == null) {
            getInstance().siteUrl = SiteConfiguration.getSiteUrl(getInstance().subDomain);
        }

        return getInstance().siteUrl;
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


    /**
     * Get billing account of current logged in user
     *
     * @return billing account of current logged in user
     */
    public static SimpleBillingAccount getBillingAccount() {
        return getInstance().billingAccount;
    }

    public static final TimeZone getUserTimeZone() {
        return getInstance().userTimeZone;
    }

    public static final String getDateTimeFormat() {
        return getInstance().billingAccount.getDateTimeFormatInstance();
    }

    public static final String getDateFormat() {
        return getInstance().billingAccount.getDateFormatInstance();
    }

    public static final Boolean showEmailPublicly() {
        return getInstance().billingAccount.getDisplayemailpublicly();
    }

    public static final String getShortDateFormat() {
        return getInstance().billingAccount.getShortDateFormatInstance();
    }

    public static final String getLongDateFormat() {
        return getInstance().billingAccount.getLongDateFormatInstance();
    }

    public static final Currency getDefaultCurrency() {
        return getInstance().billingAccount.getCurrencyInstance();
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
        return AppContext.getMessage(PermissionFlag.toVal(perVal));
    }

    /**
     * @param date is the UTC date value
     * @return
     */
    public static String formatDateTime(Date date) {
//        return date == null ? "" : DateTimeUtils.formatDate(date, AppContext.getDateTimeFormat(), AppContext.getUserTimeZone());
        if (date == null) {
            return "";
        } else {
            DateTime jodaDate = new DateTime(date).toDateTime(DateTimeZone.forTimeZone(AppContext.getUserTimeZone()));
            if (jodaDate.getHourOfDay() > 0 || jodaDate.getMinuteOfHour() > 0) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(AppContext.getDateTimeFormat());
                return formatter.print(jodaDate);
            } else {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(AppContext.getDateFormat());
                return formatter.print(jodaDate);
            }
        }
    }

    /**
     * @param date is the UTC date value
     * @return
     */
    public static String formatDate(Date date) {
        return date == null ? "" : DateTimeUtils.formatDate(date, AppContext.getDateFormat(), AppContext.getUserTimeZone());
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
        return date == null ? "" : DateTimeUtils.formatDate(date, AppContext.getShortDateFormat(), AppContext.getUserTimeZone());
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

    /**
     * @param fragment
     * @param windowTitle
     */
    public static void addFragment(String fragment, String windowTitle) {
        Page.getCurrent().setUriFragment(fragment, false);
        Page.getCurrent().setTitle(String.format("%s [%s]", StringUtils.trim(windowTitle, 150), AppContext.getSiteName()));
    }
}
