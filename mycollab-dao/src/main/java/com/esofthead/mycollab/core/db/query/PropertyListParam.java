package com.esofthead.mycollab.core.db.query;

import java.util.Collection;

import com.esofthead.mycollab.core.arguments.CollectionValueSearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class PropertyListParam extends ColumnParam {
	public static final String BELONG_TO = "belong to";
	public static final String NOT_BELONG_TO = "not belong to";

	public static final String[] OPTIONS = { BELONG_TO, NOT_BELONG_TO };

	private static String IN_EXPR = "%s.%s in ";
	private static String NOT_IN_EXPR = "%s.%s not in ";

	public PropertyListParam(String id, String displayName, String table,
			String column) {
		super(id, displayName, table, column);
	}

	public CollectionValueSearchField buildPropertyParamInList(String oper,
			Collection<?> value) {
		return new CollectionValueSearchField(oper, String.format(IN_EXPR,
				this.getTable(), this.getColumn()), value);
	}

	public CollectionValueSearchField buildPropertyParamNotInList(String oper,
			Collection<?> value) {
		return new CollectionValueSearchField(oper, String.format(NOT_IN_EXPR,
				this.getTable(), this.getColumn()), value);
	}
}
