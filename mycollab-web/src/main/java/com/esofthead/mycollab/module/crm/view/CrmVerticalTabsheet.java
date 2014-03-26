package com.esofthead.mycollab.module.crm.view;


import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.vaadin.ui.Button;
public class CrmVerticalTabsheet extends VerticalTabsheet{
	/**
	 * 
	 */
	public CrmVerticalTabsheet(boolean isLeft) {
	super(isLeft);
	}
	private static final long serialVersionUID = 1L;

	@Override 
	protected void setDefaulButtonIcon(Button btn, Boolean selected) {
		String caption = btn.getCaption();
		String suffix;
		if (selected != true)
			suffix = "_white";
		else
			suffix = "";

		switch (caption){
		case "About":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/detail" + suffix + ".png"));
			break;

		case "Campaigns":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/campaign" + suffix + ".png"));
			break;
		case "Contacts":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/contact" + suffix + ".png"));
			break;

		case "Leads":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/lead" + suffix + ".png"));
			break;

		case "Opportunities":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/opportunity" + suffix + ".png"));
			break;

		case "Cases":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/case" + suffix + ".png"));
			break;
		case "Activities":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/activitylist" + suffix + ".png"));
			break;
			
		case "Events":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/event" + suffix + ".png"));
			break;
		case "Mettings":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/meeting" + suffix + ".png"));
			break;
		case "Notifications":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/notification" + suffix + ".png"));
			break;
		case "Custom Layouts":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/layout" + suffix + ".png"));
			break;
		case "Accounts":
			btn.setIcon(MyCollabResource
					.newResource("icons/22/crm/account" + suffix + ".png"));
			break;
			
		default:
			break;
			
		}

	}

}
