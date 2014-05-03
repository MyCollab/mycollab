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

import static com.esofthead.mycollab.vaadin.MyCollabSession.USER_TIMEZONE;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.localization.WebExceptionI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.GroupIdProvider;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.events.SessionEvent;
import com.esofthead.mycollab.events.SessionEvent.UserProfileChangeEvent;
import com.esofthead.mycollab.module.billing.SubDomainNotExistException;
import com.esofthead.mycollab.module.billing.UsageExceedBillingPlanException;
import com.esofthead.mycollab.module.billing.service.BillingPlanCheckerService;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserPreference;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserPreferenceService;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.server.Page;

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

	public AppContext() {
		MyCollabSession.putVariable("context", this);

		GroupIdProvider.registerAccountIdProvider(new GroupIdProvider() {

			@Override
			public Integer getGroupId() {
				return AppContext.getAccountId();
			}
		});
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

		TimeZone timezone = getTimezoneInContext();
		MyCollabSession.putVariable(USER_TIMEZONE, timezone);
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
			throw new SubDomainNotExistException(LocalizationHelper.getMessage(
					WebExceptionI18nEnum.SUB_DOMAIN_IS_NOT_EXISTED, domain));
		} else {
			log.debug("Get billing account {} of subdomain {}",
					BeanUtility.printBeanObj(account), domain);
			accountId = account.getId();
		}

		EventBus.getInstance()
				.addListener(
						new ApplicationEventListener<SessionEvent.UserProfileChangeEvent>() {
							private static final long serialVersionUID = 1L;

							@Override
							public Class<? extends ApplicationEvent> getEventType() {
								return SessionEvent.UserProfileChangeEvent.class;
							}

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

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");
	private static SimpleDateFormat df = new SimpleDateFormat(
			"EEE MMM dd, hh:mm aa");

	private static TimeZone getTimezoneInContext() {
		SimpleUser session = getInstance().session;
		TimeZone timezone = null;
		if (session == null) {
			timezone = TimeZone.getDefault();
		} else {
			if (session.getTimezone() == null) {
				timezone = TimeZone.getDefault();
			} else {
				timezone = TimezoneMapper.getTimezone(session.getTimezone())
						.getTimezone();
			}
		}
		return timezone;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return DateTimeUtils.formatDateTime(date,
				(TimeZone) MyCollabSession.getVariable(USER_TIMEZONE));
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return DateTimeUtils.formatDate(date,
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
	 * @param dateVal
	 * @return
	 */
	public static Date parseDate(String dateVal) {
		try {
			return simpleDateFormat.parse(dateVal);
		} catch (ParseException e) {
			return new GregorianCalendar().getTime();
		}
	}

	public static String getDateFormat() {
		return "MM/dd/yyyy";
	}

	public static String getDateTimeFormat() {
		return "MM/dd/yyyy hh:mm a";
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateToHumanRead(Date date) {
		if (date == null) {
			return "";
		}
		return df.format(date);

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
