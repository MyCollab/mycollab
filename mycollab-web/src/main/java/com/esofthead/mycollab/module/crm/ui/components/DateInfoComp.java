package com.esofthead.mycollab.module.crm.ui.components;

import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class DateInfoComp extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(DateInfoComp.class);

	public void displayEntryDateTime(ValuedBean bean) {
		this.removeAllComponents();
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false, false, false, true));
		Label dateInfoHeader = new Label("Dates");
		dateInfoHeader.setStyleName("info-hdr");
		this.addComponent(dateInfoHeader);

		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(false, false, false, true));
		try {
			String createdDate = AppContext.formatDate((Date) PropertyUtils
					.getProperty(bean, "createdtime"));
			layout.addComponent(new Label("Created: " + createdDate));

			String updatedDate = AppContext.formatDate((Date) PropertyUtils
					.getProperty(bean, "lastupdatedtime"));
			layout.addComponent(new Label("Updated: " + updatedDate));
			this.addComponent(layout);
		} catch (Exception e) {
			log.error("Get date is failed {}", BeanUtility.printBeanObj(bean));
		}
	}
}
