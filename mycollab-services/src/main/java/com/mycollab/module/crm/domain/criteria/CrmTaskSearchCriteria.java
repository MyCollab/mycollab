package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.BitSearchField;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmTaskSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private NumberSearchField contactId;
    private NumberSearchField accountId;
    private NumberSearchField campaignId;
    private NumberSearchField targetId;
    private NumberSearchField leadId;
    private NumberSearchField opportunityId;
    private NumberSearchField quoteId;
    private NumberSearchField productId;
    private NumberSearchField caseId;
    private StringSearchField assignUser;
    private NumberSearchField id;

    private BitSearchField isClosed;

    public NumberSearchField getContactId() {
        return contactId;
    }

    public void setContactId(NumberSearchField contactId) {
        this.contactId = contactId;
    }

    public NumberSearchField getAccountId() {
        return accountId;
    }

    public void setAccountId(NumberSearchField accountId) {
        this.accountId = accountId;
    }

    public NumberSearchField getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(NumberSearchField campaignId) {
        this.campaignId = campaignId;
    }

    public NumberSearchField getTargetId() {
        return targetId;
    }

    public void setTargetId(NumberSearchField targetId) {
        this.targetId = targetId;
    }

    public NumberSearchField getLeadId() {
        return leadId;
    }

    public void setLeadId(NumberSearchField leadId) {
        this.leadId = leadId;
    }

    public NumberSearchField getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(NumberSearchField opportunityId) {
        this.opportunityId = opportunityId;
    }

    public NumberSearchField getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(NumberSearchField quoteId) {
        this.quoteId = quoteId;
    }

    public NumberSearchField getProductId() {
        return productId;
    }

    public void setProductId(NumberSearchField productId) {
        this.productId = productId;
    }

    public NumberSearchField getCaseId() {
        return caseId;
    }

    public void setCaseId(NumberSearchField caseId) {
        this.caseId = caseId;
    }

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }

    public BitSearchField getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(BitSearchField isClosed) {
        this.isClosed = isClosed;
    }

}
