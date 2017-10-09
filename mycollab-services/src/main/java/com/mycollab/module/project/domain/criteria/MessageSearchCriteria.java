package com.mycollab.module.project.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class MessageSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	private NumberSearchField id;

	private SetSearchField<Integer> projectids;

	private StringSearchField category;

	private StringSearchField title;

	private StringSearchField message;

	public NumberSearchField getId() {
		return id;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public SetSearchField<Integer> getProjectids() {
		return projectids;
	}

	public void setProjectids(SetSearchField<Integer> projectids) {
		this.projectids = projectids;
	}

	public StringSearchField getCategory() {
		return category;
	}

	public void setCategory(StringSearchField category) {
		this.category = category;
	}

	public void setTitle(StringSearchField title) {
		this.title = title;
	}

	public StringSearchField getTitle() {
		return title;
	}

	public void setMessage(StringSearchField message) {
		this.message = message;
	}

	public StringSearchField getMessage() {
		return message;
	}
}
