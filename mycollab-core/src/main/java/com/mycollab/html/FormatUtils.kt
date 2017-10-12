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

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Img
import com.hp.gagawa.java.elements.Span
import com.hp.gagawa.java.elements.Text

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object FormatUtils {
    fun newA(href: String, text: String): A = A(href, Text(text))

    fun newImg(alt: String, src: String): Img = Img(alt, src).setStyle("vertical-align: middle; margin-right: 3px;")

    fun newLink(img: Img, link: A): Span = Span().appendChild(img, DivLessFormatter.EMPTY_SPACE, link)

    fun newLink(txt: Text, link: A): Span = Span().appendChild(txt, DivLessFormatter.EMPTY_SPACE, link)
}