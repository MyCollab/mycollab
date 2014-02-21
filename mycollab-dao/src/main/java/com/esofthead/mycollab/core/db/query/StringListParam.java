package com.esofthead.mycollab.core.db.query;

import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class StringListParam extends ColumnParam {
	public static final String IN = "in";
	public static final String NOT_IN = "not in";

	public static String[] OPTIONS = { IN, NOT_IN };

	private List<String> lstValues;

	public StringListParam(String id, String displayName, String table,
			String column, List<String> values) {
		super(id, displayName, table, column);
		this.lstValues = values;
	}

	public List<String> getLstValues() {
		return lstValues;
	}

	public void setLstValues(List<String> lstValues) {
		this.lstValues = lstValues;
	}
}
