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
package com.esofthead.mycollab.schedule.email.format

import com.esofthead.mycollab.configuration.StorageManager
import com.hp.gagawa.java.elements.{Span, Img, Text, A}

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
object FormatUtils {
  def newA(href: String, text: String): A = new A(href, new Text(text)).setStyle("text-decoration: none; color: rgb(0, 109, 172);")

  def newImg(alt: String, src: String): Img = new Img(alt, src).setStyle("vertical-align: middle; margin-right: 3px;")

  def newLink(img: Img, link: A): Span = new Span().appendChild(img, link)

  def newAvatar(avatarId:String): Img = new Img("", StorageManager.getAvatarLink(avatarId, 16)).setWidth("16")
    .setHeight("16").setStyle("display: inline-block; vertical-align: top;")
}
