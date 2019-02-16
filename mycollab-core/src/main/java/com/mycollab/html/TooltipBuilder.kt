/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.html

import com.hp.gagawa.java.elements.*
import com.mycollab.core.utils.StringUtils

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TooltipBuilder() {
    private var table = Table().setStyle("padding-left:10px; width:550px; color: black; font-size:12px;")
    private var div = Div().setStyle("line-height: normal;").appendChild(table)

    fun appendTitle(title: String): TooltipBuilder {
        val htmlTitle = H3().setStyle("whitespace: normal; width: 100%; word-wrap: break-word;").appendText(title)
        div.appendChild(0, htmlTitle)
        return this;
    }

    fun appendRow(tr: Tr): TooltipBuilder {
        table.appendChild(tr)
        return this
    }

    fun create(): Div = div

    companion object {
        @JvmStatic fun buildCellName(name: String): Td = Td().setStyle("width: 120px; vertical-align: top; text-align: right; color:#999").
                appendText(name + ": ")

        @JvmStatic fun buildCellValue(value: String?): Td {
            val cutNameVal = StringUtils.trimHtmlTags(value)
            return Td().setStyle("width:200px;word-wrap: break-word; white-space: normal;vertical-align: top;").appendText(cutNameVal)
        }

        @JvmStatic fun buildCellValue(value: Number?): Td = if (value == null) buildCellValue("") else buildCellValue(value.toString())

        @JvmStatic fun buildCellLink(href: String?, name: String?): Td {
            val cutNameVal = StringUtils.trimHtmlTags(name)
            return Td().setStyle("width:200px;word-wrap: break-word; white-space: normal;vertical-align: top;").
                    appendChild(A().setHref(href).appendText(cutNameVal))
        }

        @JvmStatic fun buildCellLink(href: String, imageLink: String, name: String?): Td {
            val cutNameVal = StringUtils.trimHtmlTags(name)
            return Td().setStyle("width:200px;word-wrap: break-word; white-space: normal;vertical-align: top;").
                    appendChild(Img("", imageLink)).appendChild(DivLessFormatter.EMPTY_SPACE).
                    appendChild(A().setHref(href).appendText(cutNameVal))
        }
    }
}