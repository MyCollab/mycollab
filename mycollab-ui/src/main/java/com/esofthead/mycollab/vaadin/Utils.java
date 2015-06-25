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

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
public class Utils {
    public static String getSubDomain(VaadinRequest request) {
        VaadinServletRequest servletRequest = (VaadinServletRequest) request;
        if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
            return servletRequest.getServerName().split("\\.")[0];
        } else {
            return servletRequest.getServerName();
        }
    }
}
