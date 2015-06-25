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
package com.esofthead.mycollab.common;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.hp.gagawa.java.elements.*;

/**
 * @author MyCollab Ltd.
 * @since 4.1.1
 */
public class TooltipBuilder {

    private Div div;
    private Table table;

    public TooltipBuilder() {
        div = new Div();
        div.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
        table = new Table();
        table.setStyle("padding-left:10px; width:550px; color: #5a5a5a; font-size:11px;");
        div.appendChild(table);
    }

    public TooltipBuilder setTitle(String title) {
        H3 htmlTitle = new H3();
        htmlTitle.setStyle("whitespace: normal; width: 100%; word-wrap: break-word;");
        htmlTitle.appendText(title);
        div.appendChild(0, htmlTitle);
        return this;
    }

    public TooltipBuilder appendRow(Tr tr) {
        table.appendChild(tr);
        return this;
    }

    public Div create() {
        return div;
    }

    public static class TdUtil {
        public static Td buildCellName(String name) {
            return new Td().setStyle(
                    "width: 120px; vertical-align: top; text-align: right;")
                    .appendText(name + ": ");
        }

        public static Td buildCellValue(String value) {
            String cutNameVal = StringUtils.trimHtmlTags(value);
            return new Td()
                    .setStyle(
                            "width:200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
                    .appendText(cutNameVal);
        }

        public static Td buildCellValue(Number value) {
            return (value == null) ? buildCellValue("") : buildCellValue(value
                    .toString());

        }

        public static Td buildCellLink(String href, String name) {
            String cutNameVal = StringUtils.trimHtmlTags(name);
            return new Td()
                    .setStyle(
                            "width:200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
                    .appendChild(new A().setHref(href).appendText(cutNameVal));
        }

        public static Td buildCellLink(String href, String imageLink,
                                       String name) {
            String cutNameVal = StringUtils.trimHtmlTags(name);
            return new Td()
                    .setStyle(
                            "width:200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
                    .appendChild(new Img("", imageLink)).appendChild(DivLessFormatter.EMPTY_SPACE()).appendChild(new A().setHref(href).appendText(cutNameVal));
        }
    }
}
