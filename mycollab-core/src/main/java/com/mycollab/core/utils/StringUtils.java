/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is part of mycollab-core.
 * <p>
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to process string
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public final class StringUtils {

    public static String trim(String input, int length) {
        return trim(input, length, true);
    }

    public static boolean isBlank(CharSequence value) {
        return org.apache.commons.lang3.StringUtils.isBlank(value);
    }

    public static boolean isNotBlank(CharSequence value) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(value);
    }

    /**
     * @param input
     * @param length
     * @param withEllipsis
     * @return
     */
    public static String trim(String input, int length, boolean withEllipsis) {
        if (input == null) {
            return "";
        }

        if (input.length() <= length)
            return input;

        return (withEllipsis) ? (input.substring(0, length) + "...") : (input.substring(0, length));
    }

    /**
     * Check whether <code>text</code> is an Ascii string
     *
     * @param text
     * @return
     */
    public static boolean isAsciiString(String text) {
        return text.matches("\\A\\p{ASCII}*\\z");
    }

    /**
     * @param value
     * @return
     */
    public static String formatRichText(String value) {
        if (isBlank(value)) {
            return "";
        }

        value = Jsoup.clean(value, relaxed().addTags("img")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width", "style")
                .addProtocols("img", "src", "http", "https"));
        Document doc = Jsoup.parse(value);
        Element body = doc.body();
        replaceHtml(body);
        return body.html();
    }

    private static Whitelist relaxed() {
        return (Whitelist.basic()).addTags(new String[]{"a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul"}).addAttributes("a", new String[]{"href", "title"}).addAttributes("blockquote", new String[]{"cite"}).addAttributes("col", new String[]{"span", "width"}).addAttributes("colgroup", new String[]{"span", "width"}).addAttributes("img", new String[]{"align", "alt", "height", "src", "title", "width"}).addAttributes("ol", new String[]{"start", "type"}).addAttributes("q", new String[]{"cite"}).addAttributes("table", new String[]{"summary", "width"}).addAttributes("td", new String[]{"abbr", "axis", "colspan", "rowspan", "width"}).addAttributes("th", new String[]{"abbr", "axis", "colspan", "rowspan", "scope", "width"}).addAttributes("ul", new String[]{"type"}).addProtocols("a", "href", new String[]{"ftp", "http", "https", "mailto"}).addProtocols("blockquote", "cite", new String[]{"http", "https"}).addProtocols("cite", "cite", new String[]{"http", "https"}).addProtocols("img", "src", new String[]{"http", "https"}).addProtocols("q", "cite", "http", "https");
    }

    private static void replaceHtml(Node element) {
        List<Node> elements = element.childNodes();
        Pattern compile = Pattern.compile("(?:https?|ftps?)://[\\w/%.-][/\\??\\w=?\\w?/%.-]?[/\\?&\\w=?\\w?/%.-]*");
        for (int i = elements.size() - 1; i >= 0; i--) {
            Node node = elements.get(i);
            if (node instanceof TextNode) {
                String value = ((TextNode) node).text();
                Matcher matcher = compile.matcher(value);
                if (matcher.find()) {
                    value = value.replaceAll(
                            "(?:https?|ftps?)://[\\w/%.-][/\\??\\w=?\\w?/%.-]?[/\\?&\\w=?\\w?/%.-]*",
                            "<a href=\"$0\" target=\"_blank\">$0</a>");
                    Document newDoc = Jsoup.parse(value);
                    List<Node> childs = newDoc.body().childNodes();
                    for (int j = 0; j < childs.size(); j++) {
                        Node childNode = childs.get(j).clone();
                        node.before(childNode);
                    }
                    node.remove();
                }
            }
        }
    }

    public static String trimHtmlTags(String value) {
        return trimHtmlTags(value, 200);
    }

    public static String trimHtmlTags(String value, int limitedCharacters) {
        if (isBlank(value)) {
            return "";
        } else {
            String str = Jsoup.parse(value).text();
            if (str.length() > limitedCharacters) {
                str = str.substring(0, limitedCharacters) + "...";
            }
            return str;
        }
    }

    public static String generateSoftUniqueId() {
        return "" + (new GregorianCalendar().getTimeInMillis()) + new Random().nextInt(10);
    }

    public static String getStrOptionalNullValue(String value) {
        return (value == null) ? "" : value;
    }

    public static String extractNameFromEmail(String value) {
        int index = (value != null) ? value.indexOf("@") : 0;
        if (index > 0) {
            return value.substring(0, index);
        } else {
            return (value != null) ? value : "";
        }
    }


    public static boolean isValidEmail(String value) {
        try {
            //
            // Create InternetAddress object and validated the supplied
            // address which is this case is an email address.
            InternetAddress internetAddress = new InternetAddress(value);
            internetAddress.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    public static boolean isValidPhoneNumber(String value) {
        return true;
    }

}
