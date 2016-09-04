/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.schedule.email.format

import java.util.Locale

import com.hp.gagawa.java.elements.Span
import com.mycollab.core.utils.StringUtils
import com.mycollab.schedule.email.MailContext
import org.apache.commons.beanutils.PropertyUtils

/**
  * @author MyCollab Ltd
  * @since 5.4.2
  */
class CountryFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
  override def formatField(context: MailContext[_]): String = {
    val wrappedBean = context.wrappedBean
    var countryCode: String = null
    try {
      countryCode = PropertyUtils.getProperty(wrappedBean, fieldName).asInstanceOf[String]
      if (countryCode == null) {
        new Span().write
      } else {
        val locale = new Locale("", countryCode)
        new Span().appendText(locale.getDisplayCountry(locale)).write
      }
    } catch {
      case e: Any => new Span().write
    }
  }
  
  override def formatField(context: MailContext[_], value: String): String = {
    if (StringUtils.isBlank(value)) {
      return new Span().write
    }
    
    val locale = new Locale("", value)
    new Span().appendText(locale.getDisplayCountry(context.locale)).write
  }
}
