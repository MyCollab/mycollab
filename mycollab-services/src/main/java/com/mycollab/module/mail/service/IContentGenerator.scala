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
package com.mycollab.module.mail.service

import java.util.Locale

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
trait IContentGenerator {
  /**
    *
    * @param key
    * @param value
    */
  def putVariable(key: String, value: Any)

  /**
    *
    * @param templateFilePath
    * @return
    */
  def parseFile(templateFilePath: String): String

  /**
    *
    * @param templateFilePath
    * @param currentLocale
    * @return
    */
  def parseFile(templateFilePath: String, currentLocale: Locale): String
}
