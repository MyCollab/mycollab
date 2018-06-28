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
package com.mycollab.module.crm.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.event.CrmEvent
import com.mycollab.module.crm.view.account.AccountUrlResolver
import com.mycollab.module.crm.view.activity.ActivityUrlResolver
import com.mycollab.module.crm.view.campaign.CampaignUrlResolver
import com.mycollab.module.crm.view.cases.CaseUrlResolver
import com.mycollab.module.crm.view.contact.ContactUrlResolver
import com.mycollab.module.crm.view.file.FileUrlResolver
import com.mycollab.module.crm.view.lead.LeadUrlResolver
import com.mycollab.module.crm.view.opportunity.OpportunityUrlResolver
import com.mycollab.module.crm.view.setting.CrmSettingUrlResolver
import com.mycollab.shell.event.ShellEvent
import com.mycollab.vaadin.mvp.UrlResolver
import com.mycollab.vaadin.web.ui.ModuleHelper

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CrmUrlResolver : UrlResolver() {
    fun build(): UrlResolver {
        this.addSubResolver("dashboard", CrmDashboardUrlResolver())
        this.addSubResolver("account", AccountUrlResolver())
        this.addSubResolver("contact", ContactUrlResolver())
        this.addSubResolver("campaign", CampaignUrlResolver())
        this.addSubResolver("lead", LeadUrlResolver())
        this.addSubResolver("opportunity", OpportunityUrlResolver())
        this.addSubResolver("cases", CaseUrlResolver())
        this.addSubResolver("activity", ActivityUrlResolver())
        this.addSubResolver("file", FileUrlResolver())
        this.addSubResolver("setting", CrmSettingUrlResolver())
        return this
    }

    override fun handle(vararg params: String) {
        if (!ModuleHelper.isCurrentCrmModule()) {
            EventBusFactory.getInstance().post(ShellEvent.GotoCrmModule(this, params))
        } else {
            super.handle(*params)
        }
    }

    override fun defaultPageErrorHandler() {
        EventBusFactory.getInstance().post(ShellEvent.GotoCrmModule(this, null))
    }

    class CrmDashboardUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(CrmEvent.GotoHome(this, null))
        }
    }
}