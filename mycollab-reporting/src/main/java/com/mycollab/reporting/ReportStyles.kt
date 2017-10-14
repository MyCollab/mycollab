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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.reporting

import com.mycollab.reporting.generator.ComponentBuilderGenerator
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.component.LineBuilder
import net.sf.dynamicreports.report.builder.style.StyleBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment
import net.sf.dynamicreports.report.constant.Markup
import net.sf.dynamicreports.report.constant.VerticalTextAlignment

import java.awt.*

import net.sf.dynamicreports.report.builder.DynamicReports.cmp
import net.sf.dynamicreports.report.builder.DynamicReports.stl

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
class ReportStyles private constructor() {

    private val borderColor = Color(233, 233, 233)
    private val metaColor = Color(153, 153, 153)

    val rootStyle: StyleBuilder = stl.style().setPadding(4)
    private val boldStyle: StyleBuilder
    val italicStyle: StyleBuilder
    val underlineStyle: StyleBuilder
    val boldCenteredStyle: StyleBuilder
    val h2Style: StyleBuilder
    val h3Style: StyleBuilder
    val h4Style: StyleBuilder
    val columnTitleStyle: StyleBuilder
    val formCaptionStyle: StyleBuilder
    val borderStyle: StyleBuilder
    val metaInfoStyle: StyleBuilder
    val htmlStyle: StyleBuilder

    init {
        boldStyle = stl.style(rootStyle).bold()
        italicStyle = stl.style(rootStyle).italic()
        underlineStyle = stl.style(rootStyle).underline()
        boldCenteredStyle = stl.style(boldStyle).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.MIDDLE)
        borderStyle = stl.style(rootStyle).setBorder(stl.pen1Point().setLineColor(borderColor))
        metaInfoStyle = stl.style(rootStyle).setForegroundColor(metaColor)
        formCaptionStyle = stl.style(rootStyle).setForegroundColor(metaColor)
                .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT).setVerticalTextAlignment(VerticalTextAlignment.TOP)
        h2Style = stl.style(rootStyle).setFontSize(18)
        h3Style = stl.style(rootStyle).setFontSize(16)
        h4Style = stl.style(rootStyle).setFontSize(14)

        htmlStyle = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setMarkup(Markup.HTML)

        columnTitleStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT).setBorder(stl.pen1Point())
                .setBackgroundColor(Color.LIGHT_GRAY)
    }

    fun buildCompBuilder(value: ComponentBuilderGenerator): ComponentBuilder<*, *> {
        return value.getCompBuilder(this)
    }

    fun line(): LineBuilder {
        return cmp.line().setPen(stl.pen().setLineColor(borderColor))
    }

    companion object {
        private val _instance = ReportStyles()

        fun instance(): ReportStyles {
            return _instance
        }
    }
}
