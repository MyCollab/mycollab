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
