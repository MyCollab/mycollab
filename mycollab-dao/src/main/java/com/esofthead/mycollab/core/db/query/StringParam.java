package com.esofthead.mycollab.core.db.query;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class StringParam extends ColumnParam {
	public static final String IS = "is";
	public static final String IS_NOT = "isn't";
	public static final String CONTAINS = "contains";
	public static final String NOT_CONTAINS = "doesn't contains";
	public static final String IS_EMPTY = "is empty";
	public static final String IS_NOT_EMPTY = "is not empty";

	public static String[] OPTIONS = { IS, IS_NOT, CONTAINS, NOT_CONTAINS,
			IS_EMPTY, IS_NOT_EMPTY };

	public StringParam(String id, String displayName, String table,
			String column) {
		super(id, displayName, table, column);
	}
}
