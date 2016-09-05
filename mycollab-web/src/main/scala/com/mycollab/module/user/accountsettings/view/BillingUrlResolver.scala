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
package com.mycollab.module.user.accountsettings.view

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.user.accountsettings.view.events.AccountBillingEvent
import com.mycollab.premium.module.user.accountsettings.view.AccountSettingUrlResolver
import com.mycollab.vaadin.mvp.UrlResolver

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
class BillingUrlResolver extends AccountSettingUrlResolver {
  this.defaultUrlResolver = new SummaryUrlResolver
  this.addSubResolver("history", new HistoryUrlResolver)
  this.addSubResolver("cancel", new CancelUrlResolver)

  private class SummaryUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new AccountBillingEvent.GotoSummary(this, null))
    }
  }

  private class HistoryUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new AccountBillingEvent.GotoHistory(this, null))
    }
  }

  private class CancelUrlResolver extends AccountSettingUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new AccountBillingEvent.CancelAccount(this, null))
    }
  }

  override protected def defaultPageErrorHandler(): Unit = {
    handlePage()
  }
}