package com.esofthead.mycollab.core.db.query;

import java.util.Collection;
import java.util.List;

import com.esofthead.mycollab.core.arguments.CollectionValueSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class StringListParam extends ColumnParam {
	public static final String IN = "in";
	public static final String NOT_IN = "not in";

	private static String IN_EXPR = "%s.%s in ";
	private static String NOT_IN_EXPR = "%s.%s not in ";

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

	public CollectionValueSearchField buildStringParamInList(String oper,
			Collection<?> value) {
		return new CollectionValueSearchField(oper, String.format(IN_EXPR,
				this.getTable(), this.getColumn()), value);
	}

	public CollectionValueSearchField andStringParamInList(Collection<?> value) {
		return buildStringParamInList(SearchField.AND, value);
	}

	public CollectionValueSearchField orStringParamInList(Collection<?> value) {
		return buildStringParamInList(SearchField.OR, value);
	}

	public CollectionValueSearchField buildStringParamNotInList(String oper,
			Collection<?> value) {
		return new CollectionValueSearchField(oper, String.format(NOT_IN_EXPR,
				this.getTable(), this.getColumn()), value);
	}

	public CollectionValueSearchField andStringParamNotInList(List<?> value) {
		return buildStringParamNotInList(SearchField.AND, value);
	}

	public CollectionValueSearchField orStringParamNotInList(List<?> value) {
		return buildStringParamNotInList(SearchField.OR, value);
	}
}
