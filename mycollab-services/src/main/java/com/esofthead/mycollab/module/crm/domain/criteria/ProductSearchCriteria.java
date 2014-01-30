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
package com.esofthead.mycollab.module.crm.domain.criteria;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;

public class ProductSearchCriteria extends SearchCriteria {
	private StringSearchField productName;

	private StringSearchField accountName;

	private NumberSearchField contactId;

	private NumberSearchField accountId;

	private StringSearchField contactName;

	private NumberSearchField contractId;

	private NumberSearchField productId;

	public StringSearchField getProductName() {
		return productName;
	}

	public void setProductName(StringSearchField productName) {
		this.productName = productName;
	}

	public StringSearchField getAccountName() {
		return accountName;
	}

	public void setAccountName(StringSearchField accountName) {
		this.accountName = accountName;
	}

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

	public StringSearchField getContactName() {
		return contactName;
	}

	public void setContactName(StringSearchField contactName) {
		this.contactName = contactName;
	}

	public NumberSearchField getContractId() {
		return contractId;
	}

	public void setContractId(NumberSearchField contractId) {
		this.contractId = contractId;
	}

	public NumberSearchField getProductId() {
		return productId;
	}

	public void setProductId(NumberSearchField productId) {
		this.productId = productId;
	}
}
