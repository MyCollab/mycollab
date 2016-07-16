/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.servlet;

import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.i18n.LocalizationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import static com.mycollab.core.utils.ExceptionUtils.getExceptionType;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public abstract class GenericHttpServlet extends HttpServlet {
    private static Logger LOG = LoggerFactory.getLogger(GenericHttpServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setLocale(getResponseLocale(request));
            response.setCharacterEncoding("UTF-8");
            onHandleRequest(request, response);
        } catch (Exception e) {
            Exception filterException = getExceptionType(e, UserInvalidInputException.class);
            if (filterException != null) {
                PrintWriter out = response.getWriter();
                out.println(filterException.getMessage());
                return;
            }
            filterException = getExceptionType(e, ResourceNotFoundException.class);
            if (filterException != null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                LOG.error("Resource not found", filterException);
                return;
            }
            throw new ServletException(e);
        }
    }

    protected Locale getResponseLocale(HttpServletRequest request) {
        String locale = request.getParameter("locale");
        return (locale == null) ? Locale.US : LocalizationHelper.getLocaleInstance(locale);
    }

    abstract protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
