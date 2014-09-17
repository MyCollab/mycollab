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

import static com.esofthead.mycollab.common.MyCollabSession.USER_DATE_FORMAT;
import static com.esofthead.mycollab.common.MyCollabSession.USER_DATE_TIME_DATE_FORMAT;
import static com.esofthead.mycollab.common.MyCollabSession.USER_DAY_MONTH_FORMAT;
import static com.esofthead.mycollab.common.MyCollabSession.USER_SHORT_DATE_FORMAT;
import static com.esofthead.mycollab.common.MyCollabSession.USER_TIMEZONE;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.cal10n.IMessageConveyor;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;
import com.esofthead.mycollab.common.i18n.WebExceptionI18nEnum;
import com.esofthead.mycollab.configuration.LocaleHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
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
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserPreference;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserPreferenceService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * The core class that keep user session data while user login to MyCollab
 * successfully. We use thread local pattern to keep App context instance of
 * every user, so in current thread you can use static methods of AppContext to
 * get current user without fearing it impacts to other user sessions logging in
 * MyCollab system.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class AppContext implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger log = LoggerFactory.getLogger(AppContext.class);

	/**
	 * Current user log in to MyCollab
	 */
	private SimpleUser session;

	/**
	 * Preference information of current user log in to MyCollab
	 */
	private UserPreference userPreference;

	/**
	 * Billing information of account of current user
	 */
	private SimpleBillingAccount billingAccount;

	/**
	 * Subdomain associates with account of current user. This value is valid
	 * only for on-demand edition
	 */
	private String subdomain;

	/**
	 * id of account of current user. This value is valid only for on-demand
	 * edition. Though other editions also use this id in all of queries but if
	 * you have two different account ids in system may cause abnormal issues
	 */
	private Integer accountId = null;

	private IMessageConveyor messageHelper;

	private Locale userLocale = Locale.US;

	public AppContext(UI uiOwner) {
		MyCollabSession.putVariable("context", this);
	}

	/**
	 * Get context of current logged in user
	 * 
	 * @return context of current logged in user
	 */
	public static AppContext getInstance() {
		try {
			return (AppContext) MyCollabSession.getVariable("context");
		} catch (Exception e) {
			return null;
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
			UserPreference pref = getInstance().userPreference;
			UserPreferenceService prefService = ApplicationContextUtil
					.getSpringBean(UserPreferenceService.class);
			pref.setLastmodulevisit(moduleName);
			prefService.updateWithSession(pref, AppContext.getUsername());
		} catch (Exception e) {
			log.error(
					"There is error when try to update user preference for last module visit",
					e);
		}
	}

	/**
	 * Keep user session in server sessions
	 * 
	 * @param userSession
	 *            current user
	 * @param userPref
	 *            current user preference
	 * @param billingAc
	 *            account information of current user
	 */
	public void setSession(SimpleUser userSession, UserPreference userPref,
			SimpleBillingAccount billingAc) {
		session = userSession;
		userPreference = userPref;
		billingAccount = billingAc;
		setUserVariables();
	}

	public void clearSession() {
		session = null;
		userPreference = null;
		billingAccount = null;
	}

	private void setUserVariables() {
		String language = session.getLanguage();
		userLocale = LocaleHelper.toLocale(language);
		VaadinSession.getCurrent().setLocale(userLocale);
		messageHelper = LocalizationHelper.getMessageConveyor(userLocale);
		MyCollabSession.putVariable(USER_DATE_FORMAT,
				LocaleHelper.getDateFormatAssociateToLocale(userLocale));
		MyCollabSession.putVariable(USER_DATE_TIME_DATE_FORMAT,
				LocaleHelper.getDateTimeFormatAssociateToLocale(userLocale));
		MyCollabSession.putVariable(USER_SHORT_DATE_FORMAT,
				LocaleHelper.getShortDateFormatAssociateToLocale(userLocale));
		MyCollabSession.putVariable(USER_DAY_MONTH_FORMAT,
				LocaleHelper.getDayMonthFormatAssociateToLocale(userLocale));

		TimeZone timezone = null;
		if (session.getTimezone() == null) {
			timezone = TimeZone.getDefault();
		} else {
			timezone = TimezoneMapper.getTimezone(session.getTimezone());
		}
		MyCollabSession.putVariable(USER_TIMEZONE, timezone);
	}

	public static Locale getUserLocale() {
		return getInstance().userLocale;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getMessage(Class<? extends Enum> enumCls,
			String option, Object... objects) {
		try {
			Enum key = Enum.valueOf(enumCls, option);
			return getMessage(key, objects);
		} catch (Exception e) {
			log.error("Can not find resource key " + option
					+ " and enum class " + enumCls, e);
			return "Undefined";
		}
	}

	/**
	 * Get current user in session
	 * 
	 * @return current user in session
	 */
	public static SimpleUser getSession() {
		return getInstance().session;
	}

	/**
	 * Start application by query account base on <code>domain</code>
	 * 
	 * @param domain
	 *            associate with current user logged in.
	 */
	public void initDomain(String domain) {
		this.subdomain = domain;
		BillingAccountService billingService = ApplicationContextUtil
				.getSpringBean(BillingAccountService.class);
		
		BillingAccount account = billingService.getAccountByDomain(domain);

		if (account == null) {
			throw new SubDomainNotExistException(AppContext.getMessage(
					WebExceptionI18nEnum.SUB_DOMAIN_IS_NOT_EXISTED, domain));
		} else {
			log.debug("Get billing account {} of subdomain {}",
					BeanUtility.printBeanObj(account), domain);
			accountId = account.getId();
		}

		EventBusFactory
				.getInstance()
				.register(
						new ApplicationEventListener<SessionEvent.UserProfileChangeEvent>() {
							private static final long serialVersionUID = 1L;

							@Subscribe
							@Override
							public void handle(UserProfileChangeEvent event) {
								if ("avatarid".equals(event.getFieldChange())) {
									session.setAvatarid((String) event
											.getData());
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
	 * Get subdomain of current user
	 * 
	 * @return subdomain of current user
	 */
	public static String getSubDomain() {
		return getInstance().subdomain;
	}

	private String siteUrl = null;

	/**
	 * 
	 * @return
	 */
	public static String getSiteUrl() {
		if (getInstance().siteUrl == null) {
			getInstance().siteUrl = SiteConfiguration
					.getSiteUrl(getInstance().subdomain);
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

	/**
	 * Get preference info of current user
	 * 
	 * @return preference info of current user
	 */
	public static UserPreference getUserPreference() {
		return getInstance().userPreference;
	}

	/**
	 * Get billing account of current logged in user
	 * 
	 * @return billing account of current logged in user
	 */
	public static SimpleBillingAccount getBillingAccount() {
		return getInstance().billingAccount;
	}

	public static boolean isBugComponentEnable() {
		SimpleBillingAccount billingAccount = getBillingAccount();
		return (billingAccount == null) ? false : billingAccount
				.getBillingPlan().getHasbugenable();
	}

	public static boolean isStandupComponentEnable() {
		SimpleBillingAccount billingAccount = getBillingAccount();
		return (billingAccount == null) ? false : billingAccount
				.getBillingPlan().getHasstandupmeetingenable();
	}

	/**
	 * Check whether current user is admin or system
	 * 
	 * @return
	 */
	public static boolean isAdmin() {
		Boolean isAdmin = getInstance().session.getIsAccountOwner();
		if (isAdmin == null) {
			return Boolean.FALSE;
		} else {
			return isAdmin;
		}
	}

	/**
	 * 
	 * @param permissionItem
	 * @return
	 */
	public static boolean canBeYes(String permissionItem) {
		if (isAdmin()) {
			return true;
		}

		PermissionMap permissionMap = getInstance().session.getPermissionMaps();
		if (permissionMap == null) {
			return false;
		} else {
			return permissionMap.canBeYes(permissionItem);
		}
	}

	/**
	 * 
	 * @param permissionItem
	 * @return
	 */
	public static boolean canBeFalse(String permissionItem) {
		if (isAdmin()) {
			return true;
		}

		PermissionMap permissionMap = getInstance().session.getPermissionMaps();
		if (permissionMap == null) {
			return false;
		} else {
			return permissionMap.canBeFalse(permissionItem);
		}
	}

	/**
	 * 
	 * @param permissionItem
	 * @return
	 */
	public static boolean canRead(String permissionItem) {
		if (isAdmin()) {
			return true;
		}

		PermissionMap permissionMap = getInstance().session.getPermissionMaps();
		if (permissionMap == null) {
			return false;
		} else {
			return permissionMap.canRead(permissionItem);
		}
	}

	/**
	 * 
	 * @param permissionItem
	 * @return
	 */
	public static boolean canWrite(String permissionItem) {
		if (isAdmin()) {
			return true;
		}
		PermissionMap permissionMap = getInstance().session.getPermissionMaps();
		if (permissionMap == null) {
			return false;
		} else {
			return permissionMap.canWrite(permissionItem);
		}
	}

	/**
	 * 
	 * @param permissionItem
	 * @return
	 */
	public static boolean canAccess(String permissionItem) {
		if (isAdmin()) {
			return true;
		}
		PermissionMap permissionMap = getInstance().session.getPermissionMaps();
		if (permissionMap == null) {
			return false;
		} else {
			return permissionMap.canAccess(permissionItem);
		}
	}

	/**
	 * Get permission map of current user
	 * 
	 * @return permission map of current user
	 */
	public static PermissionMap getPermissionMap() {
		return getInstance().session.getPermissionMaps();
	}

	public static String getPermissionCaptionValue(
			final PermissionMap permissionMap, final String permissionItem) {
		final Integer perVal = permissionMap.get(permissionItem);
		if (perVal == null) {
			return getMessage(SecurityI18nEnum.NO_ACCESS);
		} else {
			return AppContext.getMessage(AccessPermissionFlag.toKey(perVal));
		}
	}

	public static TimeZone getTimezone() {
		try {
			return (TimeZone) MyCollabSession.getVariable(USER_TIMEZONE);
		} catch (Exception e) {
			return TimeZone.getDefault();
		}
	}

	public static String getUserShortDateFormat() {
		return (String) MyCollabSession.getVariable(USER_SHORT_DATE_FORMAT);
	}

	public static String getUserDateFormat() {
		return (String) MyCollabSession.getVariable(USER_DATE_FORMAT);
	}

	public static String getUserDateTimeFormat() {
		return (String) MyCollabSession.getVariable(USER_DATE_TIME_DATE_FORMAT);
	}

	public static String getUserDayMonthFormat() {
		return (String) MyCollabSession.getVariable(USER_DAY_MONTH_FORMAT);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return DateTimeUtils.formatDate(date, getUserDateTimeFormat(),
				(TimeZone) MyCollabSession.getVariable(USER_TIMEZONE));
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return DateTimeUtils.formatDate(date, getUserDateFormat(),
				(TimeZone) MyCollabSession.getVariable(USER_TIMEZONE));
	}

	/**
	 * 
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

	/**
	 * 
	 * @param fragement
	 * @param windowTitle
	 */
	public static void addFragment(String fragement, String windowTitle) {
		Page.getCurrent().setUriFragment(fragement, false);
		Page.getCurrent().setTitle(
				StringUtils.trim(windowTitle, 150) + " [MyCollab]");
	}
}
