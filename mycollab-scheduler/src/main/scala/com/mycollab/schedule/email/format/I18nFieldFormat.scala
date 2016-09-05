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

import com.hp.gagawa.java.elements.Span
import com.mycollab.core.utils.BeanUtility
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.schedule.email.MailContext
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory

/**
  * @author MyCollab Ltd.
  * @since 4.6.0
  */
class I18nFieldFormat(fieldName: String, displayName: Enum[_], enumKey: Class[_ <: Enum[_]], isColSpan: Boolean) extends FieldFormat(fieldName, displayName, isColSpan) {
  
  private val LOG = LoggerFactory.getLogger(classOf[I18nFieldFormat])
  
  def this(fieldName: String, displayName: Enum[_], enumKey: Class[_ <: Enum[_]]) = this(fieldName, displayName, enumKey, false)
  
  override def formatField(context: MailContext[_]): String = {
    val wrappedBean = context.wrappedBean
    var value: AnyRef = null
    try {
      value = PropertyUtils.getProperty(wrappedBean, fieldName)
      if (value == null) {
        new Span().write
      }
      else {
        new Span().appendText(LocalizationHelper.getMessage(context.locale, enumKey, value.toString)).write
      }
    } catch {
      case e: Any =>
        LOG.error("Can not generate of object " + BeanUtility.printBeanObj(wrappedBean) + " field: " + fieldName + " and " + value, e)
        new Span().write
    }
  }
  
  override def formatField(context: MailContext[_], value: String): String = {
    try {
      LocalizationHelper.getMessage(context.locale, enumKey, value.toString)
    } catch {
      case e: Exception =>
        LOG.error("Can not generate of object field: " + fieldName + " and " + value, e)
        new Span().write
    }
  }
}
