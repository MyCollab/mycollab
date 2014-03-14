package com.esofthead.mycollab.core.db.query;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class SearchFieldInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String prefixOper;

	private Param param;

	private String compareOper;

	private Object value;

	private String paramClsName;

	public SearchFieldInfo() {
	}

	public SearchFieldInfo(String prefixOper, Param param, String compareOper,
			Object value) {
		this.prefixOper = prefixOper;
		this.param = param;
		this.compareOper = compareOper;
		this.value = value;
		this.paramClsName = param.getClass().getName();
	}

	public String getPrefixOper() {
		return prefixOper;
	}

	public void setPrefixOper(String prefixOper) {
		this.prefixOper = prefixOper;
	}

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getCompareOper() {
		return compareOper;
	}

	public void setCompareOper(String compareOper) {
		this.compareOper = compareOper;
	}

	public String getParamClsName() {
		return paramClsName;
	}

	public void setParamClsName(String paramClsName) {
		this.paramClsName = paramClsName;
	}
}
