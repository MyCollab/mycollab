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
package com.mycollab.module.crm.ui.format

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Text
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.html.DivLessFormatter
import com.mycollab.module.crm.CrmLinkBuilder
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.service.AccountService
import com.mycollab.module.crm.ui.CrmAssetsManager
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.TooltipHelper
import com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat
import org.apache.commons.lang3.StringUtils

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
class AccountHistoryFieldFormat : HistoryFieldFormat {

    override fun toString(value: String): String {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))
    }

    override fun toString(value: String, displayAsHtml: Boolean, msgIfBlank: String): String {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank
        }

        val accountId = Integer.parseInt(value)
        val accountService = AppContextUtil.getSpringBean(AccountService::class.java)
        val account = accountService.findById(accountId, AppUI.accountId)

        if (account != null) {
            return if (displayAsHtml) {
                val link = A(CrmLinkBuilder.generateAccountPreviewLinkFull(accountId)).
                        setId("tag$TOOLTIP_ID").
                        appendChild(Text(account.accountname))
                link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(CrmTypeConstants.ACCOUNT, accountId.toString()))
                link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
                val div = DivLessFormatter().appendChild(Text(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT).html),
                        DivLessFormatter.EMPTY_SPACE, link)
                div.write()
            } else {
                account.accountname
            }
        }
        return value
    }
}
