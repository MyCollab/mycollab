package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.vaadin.shared.ui.label.ContentMode;
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
		String html = ProjectLinkBuilder.generateProjectMemberHtmlLink(value,
				CurrentProjectVariables.getProjectId());
		return (value != null) ? new Label(html, ContentMode.HTML) : new Label(
				"");
	}

}
