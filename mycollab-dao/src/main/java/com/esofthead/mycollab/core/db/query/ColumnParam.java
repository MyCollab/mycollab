package com.esofthead.mycollab.core.db.query;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ColumnParam extends Param {

	protected String table;
	protected String column;

	public ColumnParam(String id, String displayName, String table,
			String column) {
		super(id, displayName);
		this.table = table;
		this.column = column;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
}
