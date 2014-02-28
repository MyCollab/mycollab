package com.esofthead.mycollab.core.db.query;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CompositionColumnParam<C extends ColumnParam> extends Param {

	private Class<C> type;
	private C[] params;
	private String[] compareOptions;

	public CompositionColumnParam(String id, String displayName, Class<C> type, C[] params,
			String[] compareOptions) {
		this.id = id;
		this.displayName = displayName;
		this.type = type;
		this.params = params;
		this.compareOptions = compareOptions;
	}

	public C[] getParams() {
		return params;
	}

	public void setParams(C[] params) {
		this.params = params;
	}

	public String[] getCompareOptions() {
		return compareOptions;
	}

	public void setCompareOptions(String[] compareOptions) {
		this.compareOptions = compareOptions;
	}

	public Class<C> getType() {
		return type;
	}

	public void setType(Class<C> type) {
		this.type = type;
	}
}
