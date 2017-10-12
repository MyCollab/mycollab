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
package com.mycollab.core.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.safety.Whitelist
import java.util.*
import java.util.regex.Pattern
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress


/**
 * Utility class to process string
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
object StringUtils {

    @JvmStatic
    fun isBlank(value: CharSequence?): Boolean = org.apache.commons.lang3.StringUtils.isBlank(value)

    @JvmStatic
    fun isNotBlank(value: CharSequence?): Boolean = org.apache.commons.lang3.StringUtils.isNotBlank(value)

    @JvmStatic
    fun convertHtmlToPlainText(html: String): String {
        val doc = Jsoup.parse(html)
        doc.outputSettings(Document.OutputSettings().prettyPrint(false))//makes html() preserve linebreaks and spacing
        doc.select("br").append("\\n")
        doc.select("p").prepend("\\n\\n")
        // Trick for better formatting
        val s = doc.html().replace("\\\\n", "\n");
        s.replace("&nbsp;", " ")
        return Jsoup.clean(s, "", Whitelist.none(), Document.OutputSettings().prettyPrint(false));

    }

    /**
     * @param input
     * @param length
     * @param withEllipsis
     * @return
     */
    @JvmOverloads
    @JvmStatic
    fun trim(input: String?, length: Int, withEllipsis: Boolean = true): String {
        if (input == null) {
            return ""
        }

        if (input.length <= length)
            return input

        return if (withEllipsis) input.substring(0, length) + "..." else input.substring(0, length)
    }

    /**
     * Check whether `text` is an Ascii string
     *
     * @param text
     * @return
     */
    @JvmStatic
    fun isAsciiString(text: String): Boolean = text.matches("\\A\\p{ASCII}*\\z".toRegex())

    /**
     * @param value
     * @return
     */
    @JvmStatic
    fun formatRichText(value: String?): String {
        if (value == null) {
            return ""
        }
        var inVal = value
        inVal = Jsoup.clean(inVal, relaxed().addTags("img")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width", "style")
                .addProtocols("img", "src", "http", "https"))
        val doc = Jsoup.parse(inVal)
        val body = doc.body()
        replaceHtml(body)
        return body.html()
    }

    private fun relaxed(): Whitelist =
            Whitelist.basic().addTags(*arrayOf("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul")).addAttributes("a", *arrayOf("href", "title")).addAttributes("blockquote", *arrayOf("cite")).addAttributes("col", *arrayOf("span", "width")).addAttributes("colgroup", *arrayOf("span", "width")).addAttributes("img", *arrayOf("align", "alt", "height", "src", "title", "width")).addAttributes("ol", *arrayOf("start", "type")).addAttributes("q", *arrayOf("cite")).addAttributes("table", *arrayOf("summary", "width")).addAttributes("td", *arrayOf("abbr", "axis", "colspan", "rowspan", "width")).addAttributes("th", *arrayOf("abbr", "axis", "colspan", "rowspan", "scope", "width")).addAttributes("ul", *arrayOf("type")).addProtocols("a", "href", *arrayOf("ftp", "http", "https", "mailto")).addProtocols("blockquote", "cite", *arrayOf("http", "https")).addProtocols("cite", "cite", *arrayOf("http", "https")).addProtocols("img", "src", *arrayOf("http", "https")).addProtocols("q", "cite", "http", "https")

    private fun replaceHtml(element: Node) {
        val elements = element.childNodes()
        val compile = Pattern.compile("(?:https?|ftps?)://[\\w/%.-][/\\??\\w=?\\w?/%.-]?[/\\?&\\w=?\\w?/%.-]*")
        for (i in elements.indices.reversed()) {
            val node = elements[i]
            if (node is TextNode) {
                var value = node.text()
                val matcher = compile.matcher(value)
                if (matcher.find()) {
                    value = value.replace("(?:https?|ftps?)://[\\w/%.-][/\\??\\w=?\\w?/%.-]?[/\\?&\\w=?\\w?/%.-]*".toRegex(), "<a href=\"$0\" target=\"_blank\">$0</a>")
                    val newDoc = Jsoup.parse(value)
                    val childs = newDoc.body().childNodes()
                    childs.indices.forEach {
                        val childNode = childs[it].clone()
                        node.before(childNode)
                    }
                    node.remove()
                }
            }
        }
    }

    @JvmOverloads
    @JvmStatic
    fun trimHtmlTags(value: String?, limitedCharacters: Int = 200): String =
            if (value != null) {
                var str = Jsoup.parse(value).text()
                when {
                    str.length > limitedCharacters -> str = str.substring(0, limitedCharacters) + "..."
                }
                str
            } else ""

    @JvmStatic
    fun generateSoftUniqueId(): String = "" + GregorianCalendar().timeInMillis + Random().nextInt(10)

    @JvmStatic
    fun getStrOptionalNullValue(value: String?): String = value ?: ""

    @JvmStatic
    fun extractNameFromEmail(value: String?): String {
        val index = value?.indexOf("@") ?: 0
        return when {
            index > 0 -> value!!.substring(0, index)
            else -> value ?: ""
        }
    }


    @JvmStatic
    fun isValidEmail(value: String?): Boolean {
        return try {
            //
            // Create InternetAddress object and validated the supplied
            // address which is this case is an email address.
            val internetAddress = InternetAddress(value)
            internetAddress.validate()
            true
        } catch (e: AddressException) {
            false
        }

    }

    @JvmStatic
    fun isValidPhoneNumber(value: String?): Boolean  = true
}
