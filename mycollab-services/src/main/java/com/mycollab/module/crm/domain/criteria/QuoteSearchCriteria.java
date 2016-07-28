/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class QuoteSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField subject;

    private NumberSearchField opportunityId;

    private NumberSearchField accountId;

    private StringSearchField assignUserName;

    private StringSearchField assignUser;

    private StringSearchField billingAccountName;

    private StringSearchField billingContactName;

    private StringSearchField shippingAccountName;

    private StringSearchField shippingContactName;

    private NumberSearchField contractId;

    private NumberSearchField contactId;

    public StringSearchField getSubject() {
        return subject;
    }

    public void setSubject(StringSearchField subject) {
        this.subject = subject;
    }

    public NumberSearchField getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(NumberSearchField opportunityId) {
        this.opportunityId = opportunityId;
    }

    public NumberSearchField getAccountId() {
        return accountId;
    }

    public void setAccountId(NumberSearchField accountId) {
        this.accountId = accountId;
    }

    public StringSearchField getAssignUserName() {
        return assignUserName;
    }

    public void setAssignUserName(StringSearchField assignUserName) {
        this.assignUserName = assignUserName;
    }

    public StringSearchField getBillingAccountName() {
        return billingAccountName;
    }

    public void setBillingAccountName(StringSearchField billingAccountName) {
        this.billingAccountName = billingAccountName;
    }

    public StringSearchField getBillingContactName() {
        return billingContactName;
    }

    public void setBillingContactName(StringSearchField billingContactName) {
        this.billingContactName = billingContactName;
    }

    public StringSearchField getShippingAccountName() {
        return shippingAccountName;
    }

    public void setShippingAccountName(StringSearchField shippingAccountName) {
        this.shippingAccountName = shippingAccountName;
    }

    public StringSearchField getShippingContactName() {
        return shippingContactName;
    }

    public void setShippingContactName(StringSearchField shippingContactName) {
        this.shippingContactName = shippingContactName;
    }

    public NumberSearchField getContractId() {
        return contractId;
    }

    public void setContractId(NumberSearchField contractId) {
        this.contractId = contractId;
    }

    public NumberSearchField getContactId() {
        return contactId;
    }

    public void setContactId(NumberSearchField contactId) {
        this.contactId = contactId;
    }

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }
}
