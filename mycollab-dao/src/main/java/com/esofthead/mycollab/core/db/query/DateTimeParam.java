package com.esofthead.mycollab.core.db.query;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class DateTimeParam extends ColumnParam {

	public static String IS = "is";
	public static String IS_NOT = "isn't";
	public static String BEFORE = "is before";
	public static String AFTER = "is after";
	public static String BETWEEN = "between";
	public static String NOT_BETWEEN = "not between";
	public static String TODAY = "today";
	public static String TOMORROW = "tomorrow";
	public static String YESTERDAY = "yesterday";

	public static String[] OPTIONS = { IS, IS_NOT, BEFORE, AFTER, BETWEEN,
			NOT_BETWEEN, TODAY, TOMORROW, YESTERDAY };

	public DateTimeParam(String id, String displayName, String table,
			String column) {
		super(id, displayName, table, column);
	}
}
