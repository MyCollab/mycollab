package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ProjectMemberHistoryFieldFormat implements HistoryFieldFormat {

	@Override
	public Component formatField(String value) {
		return new Label(value);
	}

}
