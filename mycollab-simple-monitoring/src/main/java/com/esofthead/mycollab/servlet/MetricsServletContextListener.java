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
package com.esofthead.mycollab.servlet;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class MetricsServletContextListener extends MetricsServlet.ContextListener {
    @Autowired
    private MetricRegistry metricRegistry;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContextUtils
                .getRequiredWebApplicationContext(event.getServletContext())
                .getAutowireCapableBeanFactory()
                .autowireBean(this);
        super.contextInitialized(event);
    }

    @Override
    protected MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }
}
