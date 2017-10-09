package com.mycollab.module.crm.ui.format

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Text
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.html.DivLessFormatter
import com.mycollab.module.crm.CrmLinkBuilder
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.service.CampaignService
import com.mycollab.module.crm.ui.CrmAssetsManager
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.TooltipHelper
import com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
class CampaignHistoryFieldFormat : HistoryFieldFormat {

    override fun toString(value: String): String {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))
    }

    override fun toString(value: String, displayAsHtml: Boolean?, msgIfBlank: String): String {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank
        }

        try {
            val campaignId = Integer.parseInt(value)
            val campaignService = AppContextUtil.getSpringBean(CampaignService::class.java)
            val campaign = campaignService.findById(campaignId, AppUI.accountId)

            if (campaign != null) {
                return if (displayAsHtml!!) {
                    val link = A(CrmLinkBuilder.generateCampaignPreviewLinkFull(campaignId)).
                            setId("tag" + TOOLTIP_ID).
                            appendChild(Text(campaign.campaignname))
                    link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(CrmTypeConstants.CAMPAIGN,
                            campaignId.toString() + ""))
                    link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction())
                    val div = DivLessFormatter().appendChild(Text(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN).html),
                            DivLessFormatter.EMPTY_SPACE, link)
                    div.write()
                } else campaign.campaignname
            }
        } catch (e: Exception) {
            LOG.error("Error", e)
        }

        return value
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CampaignHistoryFieldFormat::class.java)
    }
}
