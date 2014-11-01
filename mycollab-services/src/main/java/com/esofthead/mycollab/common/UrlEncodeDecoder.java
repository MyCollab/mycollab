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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UrlEncodeDecoder {

	private static final Logger LOG = LoggerFactory
			.getLogger(UrlEncodeDecoder.class);

	private UrlEncodeDecoder() {
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		try {
			if (StringUtils.isBlank(str)) {
				return "";
			}
			return URLEncoder.encode(
					new String(
							Base64.encodeBase64URLSafe(str.getBytes("UTF-8")),
							"UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new MyCollabException(ex);
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	static String decode(String str) {
		try {
			String decodeStr = URLDecoder.decode(str, "UTF8");
			decodeStr = new String(Base64.decodeBase64(decodeStr
					.getBytes("UTF-8")), "UTF-8");
			return decodeStr;
		} catch (Exception e) {
			LOG.error("Error while decode string: " + str);
			return "";
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String encode(Number str) {
		return encode(str.toString());
	}
}
