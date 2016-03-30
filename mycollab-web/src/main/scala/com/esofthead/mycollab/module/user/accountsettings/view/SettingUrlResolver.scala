/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.premium.module.user.accountsettings.view

import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.user.accountsettings.view.events.SettingEvent
import com.esofthead.mycollab.module.user.accountsettings.view.events.SettingEvent.GotoTheme

/**
  * @author MyCollab Ltd
  * @since 5.1.0
  */
class SettingUrlResolver extends AccountSettingUrlResolver {
  this.addSubResolver("general", new GeneralUrlResolver)
  this.addSubResolver("theme", new ThemeUrlResolver)

  private class GeneralUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new SettingEvent.GotoGeneralSetting(this, null))
    }
  }

  private class ThemeUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new GotoTheme(SettingUrlResolver.this, null))
    }
  }

}
