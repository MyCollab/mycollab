/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common;

import java.util.StringTokenizer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class UrlTokenizer {

	private StringTokenizer urlTokenizer;
	private String internalVal;

	public UrlTokenizer(String url) {
		internalVal = (url.startsWith("/")) ? url.substring(1) : url;
		internalVal = UrlEncodeDecoder.decode(internalVal);
		urlTokenizer = new StringTokenizer(internalVal, "/");
	}

	public int getInt() throws InvalidTokenException {
		if (urlTokenizer.hasMoreTokens()) {
			try {
				return Integer.parseInt(urlTokenizer.nextToken());
			} catch (NumberFormatException e) {
				throw new InvalidTokenException(e);
			}
		} else {
			throw new InvalidTokenException("Invalid token " + internalVal);
		}
	}

	public String getString() throws InvalidTokenException {
		if (urlTokenizer.hasMoreTokens()) {
			return urlTokenizer.nextToken();
		} else {
			throw new InvalidTokenException("Invalid token " + internalVal);
		}
	}
}
