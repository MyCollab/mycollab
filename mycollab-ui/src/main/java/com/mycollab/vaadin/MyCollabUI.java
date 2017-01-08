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

import com.google.common.base.MoreObjects;
import com.mycollab.common.SessionIdGenerator;
import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.GroupIdProvider;
import com.mycollab.module.billing.SubDomainNotExistException;
import com.mycollab.module.user.domain.SimpleBillingAccount;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.ThemeManager;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.3.2
 */
public abstract class MyCollabUI extends UI {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(MyCollabUI.class);

    static {
        GroupIdProvider.registerAccountIdProvider(new GroupIdProvider() {
            @Override
            public Integer getGroupId() {
                return MyCollabUI.getAccountId();
            }

            @Override
            public String getGroupRequestedUser() {
                return UserUIContext.getUsername();
            }
        });

        SessionIdGenerator.registerSessionIdGenerator(new SessionIdGenerator() {
            @Override
            public String getSessionIdApp() {
                return UI.getCurrent().toString();
            }
        });
    }

    /**
     * Context of current logged in user
     */
    protected UserUIContext currentContext;

    private String initialSubDomain = "1";
    private String currentFragmentUrl = "";
    private SimpleBillingAccount billingAccount;
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * @return
     */
    public static String getSiteUrl() {
        return SiteConfiguration.getSiteUrl(getBillingAccount().getSubdomain());
    }

    public static SimpleBillingAccount getBillingAccount() {
        return getInstance().billingAccount;
    }

    public static MyCollabUI getInstance() {
        return (MyCollabUI) UI.getCurrent();
    }

    public static String getSubDomain() {
        return getInstance().billingAccount.getSubdomain();
    }

    /**
     * Get account id of current user
     *
     * @return account id of current user. Return 0 if can not get
     */
    public static Integer getAccountId() {
        try {
            return getInstance().billingAccount.getId();
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getSiteName() {
        try {
            return MoreObjects.firstNonNull(getInstance().billingAccount.getSitename(), SiteConfiguration.getDefaultSiteName());
        } catch (Exception e) {
            return SiteConfiguration.getDefaultSiteName();
        }
    }

    public static Currency getDefaultCurrency() {
        return getInstance().billingAccount.getCurrencyInstance();
    }

    public static String getLongDateFormat() {
        return getInstance().billingAccount.getLongDateFormatInstance();
    }

    public static Boolean showEmailPublicly() {
        return getInstance().billingAccount.getDisplayemailpublicly();
    }

    public static String getShortDateFormat() {
        return getInstance().billingAccount.getShortDateFormatInstance();
    }

    public static String getDateFormat() {
        return getInstance().billingAccount.getDateFormatInstance();
    }

    public static String getDateTimeFormat() {
        return getInstance().billingAccount.getDateTimeFormatInstance();
    }

    public static Locale getDefaultLocale() {
        return getInstance().billingAccount.getLocaleInstance();
    }

    /**
     * @param fragment
     * @param windowTitle
     */
    public static void addFragment(String fragment, String windowTitle) {
        Page.getCurrent().setUriFragment(fragment, false);
        Page.getCurrent().setTitle(String.format("%s [%s]", StringUtils.trim(windowTitle, 150), getSiteName()));
    }

    public String getCurrentFragmentUrl() {
        return currentFragmentUrl;
    }

    public void setCurrentFragmentUrl(String value) {
        this.currentFragmentUrl = value;
    }

    final protected void postSetupApp(VaadinRequest request) {
        initialSubDomain = Utils.getSubDomain(request);
        BillingAccountService billingService = AppContextUtil.getSpringBean(BillingAccountService.class);
        billingAccount = billingService.getAccountByDomain(initialSubDomain);

        if (billingAccount == null) {
            throw new SubDomainNotExistException(UserUIContext.getMessage(ErrorI18nEnum.SUB_DOMAIN_IS_NOT_EXISTED, initialSubDomain));
        } else {
            Integer accountId = billingAccount.getId();
            ThemeManager.loadDesktopTheme(accountId);
        }
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void close() {
        LOG.debug("Application is closed. Clean all resources");
        currentContext.clearSessionVariables();
        currentContext = null;
        super.close();
    }

    public static boolean isTablet() {
        try {
            WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
            return webBrowser.isIPad();
        } catch (Exception e) {
            return false;
        }
    }
}
