package com.esofthead.mycollab.core.db.query;

import com.esofthead.mycollab.core.arguments.CompositionSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CompositionStringParam extends Param {
	private StringParam[] params;

	public CompositionStringParam(String id, String displayName,
			StringParam[] params) {
		this.id = id;
		this.displayName = displayName;
		this.params = params;
	}

	public StringParam[] getParams() {
		return params;
	}

	public void setParams(StringParam[] params) {
		this.params = params;
	}

	public SearchField buildSearchField(String prefixOper, String compareOper,
			String value) {
		CompositionSearchField searchField = new CompositionSearchField(
				prefixOper);
		for (StringParam param : params) {
			SearchField field = param.buildSearchField("", compareOper, value);
			searchField.addField(field);
		}
		return searchField;
	}
}
