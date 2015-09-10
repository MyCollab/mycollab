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
package com.esofthead.mycollab.vaadin;

import ch.qos.cal10n.IMessageConveyor;
import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.ErrorI18nEnum;
import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;
import com.esofthead.mycollab.configuration.LocaleHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.SessionExpireException;
import com.esofthead.mycollab.core.format.IDateFormat;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.events.SessionEvent;
import com.esofthead.mycollab.events.SessionEvent.UserProfileChangeEvent;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.module.billing.SubDomainNotExistException;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.*;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.service.GoogleAnalyticsService;
import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.USER_TIMEZONE;

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

    private IDateFormat dateFormat;

    private static GoogleAnalyticsService googleAnalyticsService = ApplicationContextUtil.getSpringBean
            (GoogleAnalyticsService.class);

    public AppContext() {
        MyCollabSession.putVariable("context", this);
    }

    /**
     * Get context of current logged in user
     *
     * @return context of current logged in user
     */
    public static AppContext getInstance() {
        try {
            AppContext context = (AppContext) MyCollabSession.getVariable("context");
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
    public void updateLastModuleVisit(String moduleName) {
        try {
            UserAccountMapper userAccountMapper = ApplicationContextUtil.getSpringBean(UserAccountMapper.class);
            UserAccount userAccount = new UserAccount();
            userAccount.setLastmodulevisit(moduleName);
            UserAccountExample ex = new UserAccountExample();
            ex.createCriteria().andAccountidEqualTo(AppContext
                    .getAccountId()).andUsernameEqualTo(AppContext.getUsername());
            userAccountMapper.updateByExampleSelective(userAccount, ex);
        } catch (Exception e) {
            LOG.error("There is error when try to update user preference for last module visit", e);
        }
    }

    public SimpleUser getUserOfContext() {
        return session;
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
        userLocale = LocaleHelper.toLocale(language);
        dateFormat = LocaleHelper.getDateFormatInstance(userLocale);
        VaadinSession.getCurrent().setLocale(userLocale);
        messageHelper = LocalizationHelper.getMessageConveyor(userLocale);

        TimeZone timezone;
        if (session.getTimezone() == null) {
            timezone = TimeZone.getDefault();
        } else {
            timezone = TimezoneMapper.getTimezone(session.getTimezone());
        }
        MyCollabSession.putVariable(USER_TIMEZONE, timezone);
    }

    public void clearSessionVariables() {
        session = null;
        billingAccount = null;
    }

    public static String getSiteName() {
        return getInstance().siteName;
    }

    public static Locale getUserLocale() {
        return getInstance().userLocale;
    }

    public static IDateFormat getUserDateFormat() {
        return getInstance().dateFormat;
    }

    public static String getMessage(Enum<?> key, Object... objects) {
        try {
            return (key != null) ? getInstance().messageHelper.getMessage(key,
                    objects) : "";
        } catch (Exception e) {
            return LocalizationHelper.getMessage(
                    LocalizationHelper.defaultLocale, key, objects);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String getMessage(Class<? extends Enum> enumCls, String option, Object... objects) {
        try {
            if (option == null)
                return "";

            Enum key = Enum.valueOf(enumCls, option);
            return getMessage(key, objects);
        } catch (Exception e) {
            LOG.debug("Can not find resource key " + option + " and enum class " + enumCls.getName(), e);
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
        BillingAccountService billingService = ApplicationContextUtil.getSpringBean(BillingAccountService.class);

        BillingAccount account = billingService.getAccountByDomain(domain);

        if (account == null) {
            throw new SubDomainNotExistException(AppContext.getMessage(
                    ErrorI18nEnum.SUB_DOMAIN_IS_NOT_EXISTED, domain));
        } else {
            if (org.apache.commons.lang3.StringUtils.isBlank(account.getSitename())) {
                siteName = SiteConfiguration.getDefaultSiteName();
            } else {
                siteName = account.getSitename();
            }

            LOG.debug("Get billing account {} of subDomain {}", BeanUtility.printBeanObj(account), domain);
            accountId = account.getId();
        }

        EventBusFactory.getInstance().register(new ApplicationEventListener<SessionEvent.UserProfileChangeEvent>() {
            private static final long serialVersionUID = 1L;

            @Subscribe
            @Override
            public void handle(UserProfileChangeEvent event) {
                if ("avatarid".equals(event.getFieldChange())) {
                    session.setAvatarid((String) event.getData());
                }
            }
        });
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
            return "";
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


    /**
     * Check whether current user is admin or system
     *
     * @return
     */
    public static boolean isAdmin() {
        Boolean isAdmin = getInstance().session.getIsAccountOwner();
        return Boolean.TRUE.equals(isAdmin);
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
        return (permissionMap == null) ? false : permissionMap.canBeYes(permissionItem);
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
        return (permissionMap == null) ? false : permissionMap.canBeFalse(permissionItem);
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
        return (permissionMap == null) ? false : permissionMap.canRead(permissionItem);
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
        return (permissionMap == null) ? false : permissionMap.canWrite(permissionItem);
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
        return (permissionMap == null) ? false : permissionMap.canAccess(permissionItem);
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
        return (perVal == null) ? getMessage(SecurityI18nEnum.NO_ACCESS) : AppContext.getMessage(AccessPermissionFlag.toKey(perVal));
    }

    public static TimeZone getTimezone() {
        try {
            return (TimeZone) MyCollabSession.getVariable(USER_TIMEZONE);
        } catch (Exception e) {
            return TimeZone.getDefault();
        }
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return DateTimeUtils.formatDate(date, AppContext.getUserDateFormat().getDateTimeFormat(),
                (TimeZone) MyCollabSession.getVariable(USER_TIMEZONE));
    }

    /**
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return DateTimeUtils.formatDate(date, AppContext.getUserDateFormat().getDateFormat(),
                (TimeZone) MyCollabSession.getVariable(USER_TIMEZONE));
    }

    /**
     * @param date
     * @param textIfDateIsNull
     * @return
     */
    public static String formatDate(Date date, String textIfDateIsNull) {
        if (date == null) {
            return textIfDateIsNull;
        } else {
            return formatDate(date);
        }
    }

    public static String formatDayMonth(Date date) {
        return DateTimeUtils.formatDate(date, AppContext.getUserDateFormat().getDayMonthFormat());
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

    public static String formatPrettyTime(Date date) {
        return DateTimeUtils.getPrettyDateValue(date, getUserLocale());
    }

    /**
     * @param fragment
     * @param windowTitle
     */
    public static void addFragment(String fragment, String windowTitle) {
        Page.getCurrent().setUriFragment(fragment, false);
        Page.getCurrent().setTitle(
                String.format("%s [%s]", StringUtils.trim(windowTitle, 150), AppContext.getSiteName()));
        googleAnalyticsService.trackPageView(fragment);
    }
}
