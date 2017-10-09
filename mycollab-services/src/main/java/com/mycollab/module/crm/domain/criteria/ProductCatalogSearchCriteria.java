package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

public class ProductCatalogSearchCriteria extends SearchCriteria {
    private StringSearchField productName;

    private StringSearchField mftNumber;

	public StringSearchField getProductName() {
		return productName;
	}

	public void setProductName(StringSearchField productName) {
		this.productName = productName;
	}

	public StringSearchField getMftNumber() {
		return mftNumber;
	}

	public void setMftNumber(StringSearchField mftNumber) {
		this.mftNumber = mftNumber;
	}
}
