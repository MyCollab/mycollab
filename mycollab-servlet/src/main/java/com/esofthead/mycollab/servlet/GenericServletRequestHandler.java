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
package com.esofthead.mycollab.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

import com.esofthead.mycollab.core.ResourceNotFoundException;
import com.esofthead.mycollab.core.UserInvalidInputException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class GenericServletRequestHandler implements
		HttpRequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			onHandleRequest(request, response);
		} catch (Exception e) {
			if (e instanceof ResourceNotFoundException) {
				throw new ResourceNotFoundException();
			} else if (e instanceof UserInvalidInputException) {
				PrintWriter out = response.getWriter();
				out.println(e.getMessage());
			} else {
				throw new ServletException(e);
			}
		}
	}

	abstract protected void onHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;
}
