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
package com.esofthead.mycollab.module.mail;

import java.util.Locale;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public interface IContentGenerator {

	/**
	 * 
	 * @param key
	 * @param value
	 */
	void putVariable(String key, Object value);

	/**
	 * 
	 * @param subject
	 * @return
	 */
	String generateSubjectContent(String subject);

	/**
	 *
	 * @param templateFilePath
	 * @return
	 */
	String generateBodyContent(String templateFilePath);

	/**
	 * 
	 * @param templateFilePath
	 * @param currentLocale
	 * @return
	 */
	String generateBodyContent(String templateFilePath, Locale currentLocale);

	/**
	 * 
	 * @param templateFilePath
	 * @param currentLocale
	 * @param defaultLocale
	 * @return
	 */
	String generateBodyContent(String templateFilePath, Locale currentLocale,
			Locale defaultLocale);
}
