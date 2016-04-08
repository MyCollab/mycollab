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

import com.esofthead.mycollab.common.SessionIdGenerator;
import com.esofthead.mycollab.core.arguments.GroupIdProvider;
import com.esofthead.mycollab.license.LicenseInfo;
import com.esofthead.mycollab.license.LicenseResolver;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.service.GoogleAnalyticsService;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
                return AppContext.getAccountId();
            }

            @Override
            public String getGroupRequestedUser() {
                return AppContext.getUsername();
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
    protected AppContext currentContext;

    protected String initialSubDomain = "1";
    private String currentFragmentUrl = "";
    private Map<String, Object> attributes = new HashMap<>();

    public String getCurrentFragmentUrl() {
        return currentFragmentUrl;
    }

    public void setCurrentFragmentUrl(String value) {
        this.currentFragmentUrl = value;
    }

    final protected void postSetupApp(VaadinRequest request) {
        initialSubDomain = Utils.getSubDomain(request);
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    protected final void init(final VaadinRequest request) {
        GoogleAnalyticsService googleAnalyticsService = ApplicationContextUtil.getSpringBean(GoogleAnalyticsService.class);
        googleAnalyticsService.registerUI(this);

        LicenseResolver licenseResolver = ApplicationContextUtil.getSpringBean(LicenseResolver.class);
        if (licenseResolver != null) {
            LicenseInfo licenseInfo = licenseResolver.getLicenseInfo();
            if (licenseInfo == null) {
                licenseResolver.acquireALicense();
            } else if (licenseInfo != null && licenseInfo.isExpired() && licenseInfo.isTrial()) {
                Window activateWindow = ViewManager.getCacheComponent(AbstractLicenseActivationWindow.class);
                UI.getCurrent().addWindow(activateWindow);
            }
        }
        doInit(request);
    }

    abstract protected void doInit(final VaadinRequest request);

    @Override
    public void close() {
        LOG.debug("Application is closed. Clean all resources");
        currentContext.clearSessionVariables();
        currentContext = null;
        super.close();
    }
}
