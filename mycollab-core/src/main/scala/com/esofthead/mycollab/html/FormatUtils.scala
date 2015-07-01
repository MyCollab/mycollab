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
package com.esofthead.mycollab.html

import com.hp.gagawa.java.elements.{A, Img, Span, Text}

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
object FormatUtils {
  def newA(href: String, text: String): A = new A(href, new Text(text)).setStyle("color: #006DAC;")

  def newImg(alt: String, src: String): Img = new Img(alt, src).setStyle("vertical-align: middle; margin-right: 3px;")

  def newLink(img: Img, link: A): Span = new Span().appendChild(img, DivLessFormatter.EMPTY_SPACE, link)

  def newLink(txt:Text, link: A):Span = new Span().appendChild(txt, DivLessFormatter.EMPTY_SPACE, link)
}
