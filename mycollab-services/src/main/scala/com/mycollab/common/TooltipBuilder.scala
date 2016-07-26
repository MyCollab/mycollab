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
package com.mycollab.common

import com.hp.gagawa.java.elements._
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.DivLessFormatter

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
class TooltipBuilder {
  var div: Div = new Div().setStyle("line-height: normal;")
  var table: Table = new Table().setStyle("padding-left:10px; width:550px; color: black; font-size:12px;")
  div.appendChild(table)
  
  def appendTitle(title: String): TooltipBuilder = {
    val htmlTitle = new H3().setStyle("whitespace: normal; width: 100%; word-wrap: break-word;").appendText(title)
    div.appendChild(0, htmlTitle)
    this
  }
  
  def appendRow(tr: Tr): TooltipBuilder = {
    table.appendChild(tr)
    this
  }
  
  def create: Div = div
}

object TooltipBuilder {
  def buildCellName(name: String): Td = new Td().setStyle("width: 120px; vertical-align: top; text-align: right; color:#999").
    appendText(name + ": ")
  
  def buildCellValue(value: String): Td = {
    val cutNameVal: String = StringUtils.trimHtmlTags(value)
    new Td().setStyle("width:200px;word-wrap: break-word; white-space: normal;vertical-align: top;").appendText(cutNameVal)
  }
  
  def buildCellValue(value: Number): Td = if (value == null) buildCellValue("")
  else buildCellValue(value.toString)
  
  def buildCellLink(href: String, name: String): Td = {
    val cutNameVal: String = StringUtils.trimHtmlTags(name)
    new Td().setStyle("width:200px;word-wrap: break-word; white-space: normal;vertical-align: top;").
      appendChild(new A().setHref(href).appendText(cutNameVal))
  }
  
  def buildCellLink(href: String, imageLink: String, name: String): Td = {
    val cutNameVal: String = StringUtils.trimHtmlTags(name)
    new Td().setStyle("width:200px;word-wrap: break-word; white-space: normal;vertical-align: top;").
      appendChild(new Img("", imageLink)).appendChild(DivLessFormatter.EMPTY_SPACE).
      appendChild(new A().setHref(href).appendText(cutNameVal))
  }
}
