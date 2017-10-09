package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContractSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField contractName;

    private NumberSearchField accountId;

    private NumberSearchField opportunityId;

    private StringSearchField accountName;

    private StringSearchField opportunityName;

    private StringSearchField assignUser;

    private StringSearchField assignUserName;

    private NumberSearchField quoteId;

    private NumberSearchField productId;

    public StringSearchField getContractName() {
        return contractName;
    }

    public void setContractName(StringSearchField contractName) {
        this.contractName = contractName;
    }

    public NumberSearchField getAccountId() {
        return accountId;
    }

    public void setAccountId(NumberSearchField accountId) {
        this.accountId = accountId;
    }

    public NumberSearchField getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(NumberSearchField opportunityId) {
        this.opportunityId = opportunityId;
    }

    public StringSearchField getAccountName() {
        return accountName;
    }

    public void setAccountName(StringSearchField accountName) {
        this.accountName = accountName;
    }

    public StringSearchField getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(StringSearchField opportunityName) {
        this.opportunityName = opportunityName;
    }

    public StringSearchField getAssignUserName() {
        return assignUserName;
    }

    public void setAssignUserName(StringSearchField assignUserName) {
        this.assignUserName = assignUserName;
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

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }
}
