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
package com.mycollab.vaadin.ui

import com.vaadin.server.FontAwesome
import com.vaadin.ui.CssLayout
import com.vaadin.ui.themes.ValoTheme

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
class HeaderWithFontAwesome private constructor(private val iconFont: FontAwesome, title: String, primaryStyle: String) : CssLayout() {
    private val wrappedLbl: ELabel = ELabel.html("").withStyleName(primaryStyle, ValoTheme.LABEL_NO_MARGIN)
    private var title: String? = null

    init {
        updateTitle(title)
        this.addComponent(wrappedLbl)
    }

    fun updateTitle(value: String) {
        this.title = value
        wrappedLbl.value = "${iconFont.html} $value"
    }

    fun appendToTitle(value: String) {
        wrappedLbl.value = "${iconFont.html} $title $value"
    }

    companion object {

        @JvmStatic
        fun h2(iconFont: FontAwesome, title: String): HeaderWithFontAwesome =
                HeaderWithFontAwesome(iconFont, title, ValoTheme.LABEL_H2)

        @JvmStatic
        fun h3(iconFont: FontAwesome, title: String): HeaderWithFontAwesome =
                HeaderWithFontAwesome(iconFont, title, ValoTheme.LABEL_H3)
    }
}
