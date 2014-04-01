package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ProjectVerticalTabsheet extends VerticalTabsheet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void setDefaulButtonIcon(Button btn, Boolean selected) {
		String caption = btn.getCaption();
		String suffix;
		if (selected == true)
			suffix = "_selected";
		else
			suffix = "";

		switch (caption) {
		case "Dashboard":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/project/dashboard" + suffix + ".png"));
			break;

		case "Messages":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/message"
					+ suffix + ".png"));
			break;
		case "Phases":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/phase"
					+ suffix + ".png"));
			break;

		case "Tasks":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/task"
					+ suffix + ".png"));
			break;

		case "Bugs":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/bug"
					+ suffix + ".png"));
			break;

		case "Files":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/file"
					+ suffix + ".png"));
			break;
		case "Risks":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/risk"
					+ suffix + ".png"));
			break;
		case "Problems":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/problem"
					+ suffix + ".png"));
			break;
		case "Time":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/time"
					+ suffix + ".png"));
			break;
		case "StandUp":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/standup"
					+ suffix + ".png"));
			break;
		case "Users & Settings":
			btn.setIcon(MyCollabResource.newResource("icons/22/project/user"
					+ suffix + ".png"));
			break;
		default:

		}

	}

}
