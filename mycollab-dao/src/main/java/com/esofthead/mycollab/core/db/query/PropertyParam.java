package com.esofthead.mycollab.core.db.query;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.OneValueSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class PropertyParam extends ColumnParam {
	public static final String IS = "is";
	public static final String IS_NOT = "isn't";

	public static String[] OPTIONS = { IS, IS_NOT };

	private static final String IS_EXPR = "%s.%s = ";
	private static final String IS_NOT_EXPR = "%s.%s <> ";

	public PropertyParam(String id, String displayName, String table,
			String column) {
		super(id, displayName, table, column);
	}

	public SearchField buildSearchField(String prefixOper, String compareOper,
			Object value) {
		switch (compareOper) {
		case IS:
			return buildPropertyIs(prefixOper, value);
		case IS_NOT:
			return buildPropertyIsNot(prefixOper, value);
		default:
			throw new MyCollabException("Not support");
		}
	}

	public OneValueSearchField buildPropertyIs(String oper, Object value) {
		return new OneValueSearchField(oper, String.format(IS_EXPR, table,
				column), value);
	}

	public OneValueSearchField buildPropertyIsNot(String oper, Object value) {
		return new OneValueSearchField(oper, String.format(IS_NOT_EXPR, table,
				column), value);
	}
}
