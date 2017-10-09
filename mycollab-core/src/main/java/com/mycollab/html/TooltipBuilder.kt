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

        @JvmStatic fun buildCellLink(href: String, imageLink: String, name: String): Td {
            val cutNameVal = StringUtils.trimHtmlTags(name)
            return Td().setStyle("width:200px;word-wrap: break-word; white-space: normal;vertical-align: top;").
                    appendChild(Img("", imageLink)).appendChild(DivLessFormatter.EMPTY_SPACE).
                    appendChild(A().setHref(href).appendText(cutNameVal))
        }
    }
}