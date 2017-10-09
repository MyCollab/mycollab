package com.mycollab.form.domain;

import java.util.List;

public class SimpleFormSection extends FormSection {
	private static final long serialVersionUID = 1L;
	
	private List<FormSectionField> fields;

	public List<FormSectionField> getFields() {
		return fields;
	}

	public void setFields(List<FormSectionField> fields) {
		this.fields = fields;
	}
}
