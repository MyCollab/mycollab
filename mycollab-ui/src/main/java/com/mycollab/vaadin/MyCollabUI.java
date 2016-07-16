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

import com.mycollab.common.SessionIdGenerator;
import com.mycollab.db.arguments.GroupIdProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void close() {
        LOG.debug("Application is closed. Clean all resources");
        currentContext.clearSessionVariables();
        currentContext = null;
        super.close();
    }
}
