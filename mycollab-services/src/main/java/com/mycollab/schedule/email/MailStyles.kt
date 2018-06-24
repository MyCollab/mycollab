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
package com.mycollab.schedule.email

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MailStyles {
    companion object {
        private val _instance = MailStyles()

        fun instance(): MailStyles = _instance
    }

    private val styles = mapOf("footer_background" to "#3A3A3A", "font" to "14px Arial, 'Times New Roman', sans-serif",
            "small_font" to "12px Arial, 'Times New Roman', sans-serif",
            "background" to "#f5faff", "link_color" to "#006DAC",
            "border_color" to "#e5e5e5", "meta_color" to "#999",
            "action_color" to "#24a2e3")

    fun get(name: String) = styles[name] ?: ""

    fun cell(width: String) = "width: $width; padding: 10px; vertical-align: top;"
}