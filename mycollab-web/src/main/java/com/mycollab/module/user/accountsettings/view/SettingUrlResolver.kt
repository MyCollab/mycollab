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
package com.mycollab.module.user.accountsettings.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.user.accountsettings.view.event.SettingEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class SettingUrlResolver : AccountSettingUrlResolver() {
    init {
        this.addSubResolver("general", GeneralUrlResolver())
        this.addSubResolver("theme", ThemeUrlResolver())
    }

    private class GeneralUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(SettingEvent.GotoGeneralSetting(this, null))
    }

    private class ThemeUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(SettingEvent.GotoTheme(this, null))
    }
}