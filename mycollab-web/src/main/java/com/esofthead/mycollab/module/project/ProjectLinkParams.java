/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 *
 */
public class ProjectLinkParams {
	private static Pattern PATTERN = Pattern.compile("^\\w{1,3}-\\d*$");

	public static boolean isValidParam(String param) {
		Matcher matcher = PATTERN.matcher(param);
		return matcher.find();
	}

	public static String getProjectShortName(String param) {
		int index = param.indexOf("-");
		if (index > 0) {
			return param.substring(0, index);
		} else {
			throw new MyCollabException("Invalid param " + param);
		}
	}

	public static int getItemKey(String param) {
		int index = param.indexOf("-");
		if (index > 0) {
			return Integer.parseInt(param.substring(index + 1));
		} else {
			throw new MyCollabException("Invalid param " + param);
		}
	}

	public static void main(String[] args) {
		Pattern compile = Pattern.compile("^\\w{1,3}-\\d*$");
		Matcher matcher = compile.matcher("AB-11");
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}
}
