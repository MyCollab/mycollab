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

import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.user.accountsettings.view.events.{AccountBillingEvent, ProfileEvent, SetupEvent}
import com.esofthead.mycollab.shell.events.ShellEvent
import com.esofthead.mycollab.vaadin.desktop.ui.ModuleHelper
import com.esofthead.mycollab.vaadin.mvp.UrlResolver

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class AccountSettingUrlResolver extends UrlResolver {
    def build: UrlResolver = {
        this.addSubResolver("preview", new ReadUrlResolver)
        this.addSubResolver("billing", new BillingUrlResolver)
        this.addSubResolver("user", new UserUrlResolver)
        this.addSubResolver("role", new RoleUrlResolver)
        this.addSubResolver("setting", new SettingUrlResolver)
        if (!SiteConfiguration.isDemandEdition) {
            this.addSubResolver("setup", new SetupUrlResolver)
        }
        this
    }

    override def handle(params: String*) {
        if (!ModuleHelper.isCurrentAccountModule) {
            EventBusFactory.getInstance.post(new ShellEvent.GotoUserAccountModule(this, params))
        }
        else {
            super.handle(params: _*)
        }
    }

    protected def defaultPageErrorHandler {
        EventBusFactory.getInstance.post(new ProfileEvent.GotoProfileView(this, null))
    }

    private class ReadUrlResolver extends AccountSettingUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance.post(new ProfileEvent.GotoProfileView(this, null))
        }
    }

    private class BillingUrlResolver extends AccountSettingUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance.post(new AccountBillingEvent.GotoSummary(this, null))
        }
    }

    private class SetupUrlResolver extends AccountSettingUrlResolver {
        protected override def handlePage(params: String*) {
            EventBusFactory.getInstance.post(new SetupEvent.GotoSetupPage(this, null))
        }
    }

}
