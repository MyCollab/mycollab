package com.mycollab.module.crm.domain;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunitySalesStage;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleOpportunity extends Opportunity {
    private static final long serialVersionUID = 1L;

    private String createdUserAvatarId;

    private String createdUserFullName;

    private String accountName;

    private String campaignName;

    private String assignUserAvatarId;

    private String assignUserFullName;

    public String getCreatedUserAvatarId() {
        return createdUserAvatarId;
    }

    public void setCreatedUserAvatarId(String createdUserAvatarId) {
        this.createdUserAvatarId = createdUserAvatarId;
    }

    public String getCreatedUserFullName() {
        if (StringUtils.isBlank(createdUserFullName)) {
            return StringUtils.extractNameFromEmail(getCreateduser());
        }
        return createdUserFullName;
    }

    public void setCreatedUserFullName(String createdUserFullName) {
        this.createdUserFullName = createdUserFullName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getAssignUserFullName() {
        if (StringUtils.isBlank(assignUserFullName)) {
            return StringUtils.extractNameFromEmail(getAssignuser());
        }
        return assignUserFullName;
    }

    public void setAssignUserFullName(String assignUserFullName) {
        this.assignUserFullName = assignUserFullName;
    }

    public String getAssignUserAvatarId() {
        return assignUserAvatarId;
    }

    public void setAssignUserAvatarId(String assignUserAvatarId) {
        this.assignUserAvatarId = assignUserAvatarId;
    }

    public boolean isOverdue() {
        String saleState = getSalesstage();
        Date closeDate = getExpectedcloseddate();
        return (!OpportunitySalesStage.Closed_Won.name().equals(saleState) &&
                !OpportunitySalesStage.Closed_Lost.name().equals(saleState))
                && closeDate != null && (closeDate.before(new GregorianCalendar().getTime()));
    }
}
