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
package com.esofthead.mycollab.schedule.email

import scala.collection.mutable._

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
class MailStyles {
    private val styles: Map[String, String] = Map();
    styles.put("footer_background", "#3A3A3A")
    styles.put("font", "13px Arial, 'Times New Roman', sans-serif")
    styles.put("background", "#FFFFFF")
    styles.put("link_color", "#006DAC")

    def get(name: String): String = {
        val option: Option[String] = styles.get(name)
        option.get
    }
}

object MailStyles {
    private val _instance: MailStyles = new MailStyles()

    def instance(): MailStyles = _instance
}
