package com.esofthead.mycollab.core.arguments;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CompositionSearchField extends SearchField {
	private List<SearchField> fields;

	public CompositionSearchField(String oper) {
		this.operation = oper;
	}

	public List<SearchField> getFields() {
		return fields;
	}

	public void setFields(List<SearchField> fields) {
		this.fields = fields;
	}

	public void addField(SearchField field) {
		if (fields == null) {
			fields = new ArrayList<SearchField>();
		}

		fields.add(field);
	}
}
