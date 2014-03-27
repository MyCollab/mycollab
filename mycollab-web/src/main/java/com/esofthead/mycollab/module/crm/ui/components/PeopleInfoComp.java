package com.esofthead.mycollab.module.crm.ui.components;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class PeopleInfoComp extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(PeopleInfoComp.class);

	public void displayEntryPeople(ValuedBean bean) {
		this.removeAllComponents();
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false, false, false, true));

		Label peopleInfoHeader = new Label("People");
		peopleInfoHeader.setStyleName("info-hdr");
		this.addComponent(peopleInfoHeader);

		GridLayout layout = new GridLayout(2, 2);
		layout.setSpacing(true);
		layout.setWidth("100%");
		layout.setMargin(new MarginInfo(false, false, false, true));
		try {
			Label createdLbl = new Label("Created:");
			createdLbl.setSizeUndefined();
			layout.addComponent(createdLbl, 0, 0);

			String createdUserName = (String) PropertyUtils.getProperty(bean,
					"createduser");
			String createdUserAvatarId = (String) PropertyUtils.getProperty(
					bean, "createdUserAvatarId");
			String createdUserDisplayName = (String) PropertyUtils.getProperty(
					bean, "createdUserFullName");

			UserLink createdUserLink = new UserLink(createdUserName,
					createdUserAvatarId, createdUserDisplayName);
			layout.addComponent(createdUserLink, 1, 0);
			layout.setColumnExpandRatio(1, 1.0f);

			Label assigneeLbl = new Label("Assignee: ");
			assigneeLbl.setSizeUndefined();
			layout.addComponent(assigneeLbl, 0, 1);
			String assignUserName = (String) PropertyUtils.getProperty(bean,
					"assignuser");
			String assignUserAvatarId = (String) PropertyUtils.getProperty(
					bean, "assignUserAvatarId");
			String assignUserDisplayName = (String) PropertyUtils.getProperty(
					bean, "assignUserFullName");

			UserLink assignUserLink = new UserLink(assignUserName,
					assignUserAvatarId, assignUserDisplayName);
			layout.addComponent(assignUserLink, 1, 1);
		} catch (Exception e) {
			log.error("Can not build user link {} ",
					BeanUtility.printBeanObj(bean));
		}

		this.addComponent(layout);

	}
}
