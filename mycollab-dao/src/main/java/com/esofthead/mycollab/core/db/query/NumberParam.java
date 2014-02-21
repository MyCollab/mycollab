package com.esofthead.mycollab.core.db.query;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class NumberParam extends ColumnParam {
	public static String EQUAL = "=";
	public static String NOT_EQUAL = "<>";
	public static String LESS_THAN = "<";
	public static String LESS_THAN_EQUAL = "<=";
	public static String GREATER_THAN = ">";
	public static String GREATER_THAN_EQUAL = "=>";
	public static String IS_EMPTY = "is empty";
	public static String IS_NOT_EMPTY = "is not empty";

	public static String[] OPTIONS = { EQUAL, NOT_EQUAL, LESS_THAN,
			LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, IS_EMPTY,
			IS_NOT_EMPTY };

	public NumberParam(String id, String displayName, String table,
			String column) {
		super(id, displayName, table, column);
	}

}
