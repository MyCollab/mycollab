/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.community.servlet;

import org.eclipse.jetty.servlets.GzipFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
@WebFilter(urlPatterns = {"/*"}, description = "Gzip Filter", initParams = {@WebInitParam(name = "mimeTypes",
        value = "text/html,text/plain,text/xml,application/xhtml+xml,text/css,application/javascript,image/svg+xml")} )
public class AnnoGzipFilterServlet extends GzipFilter {
}
