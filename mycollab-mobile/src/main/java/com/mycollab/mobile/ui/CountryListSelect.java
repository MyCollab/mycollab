package com.mycollab.mobile.ui;

import com.mycollab.common.CountryValueFactory;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CountryListSelect extends ValueListSelect {
	private static final long serialVersionUID = 1L;

	public CountryListSelect() {
		loadData(CountryValueFactory.countryList);
	}
}
