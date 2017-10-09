package com.mycollab.module.crm.ui.components;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class DateInfoComp extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(DateInfoComp.class);

    public void displayEntryDateTime(ValuedBean bean) {
        this.removeAllComponents();
        this.withMargin(false);
        Label dateInfoHeader = new Label(FontAwesome.CALENDAR.getHtml() + " " +
                UserUIContext.getMessage(CrmCommonI18nEnum.SUB_INFO_DATES), ContentMode.HTML);
        dateInfoHeader.setStyleName("info-hdr");
        this.addComponent(dateInfoHeader);

        MVerticalLayout layout = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true)).withFullWidth();
        try {
            Date createdDate = (Date) PropertyUtils.getProperty(bean, "createdtime");
            Label createdDateLbl = new Label(UserUIContext.getMessage(CrmCommonI18nEnum.ITEM_CREATED_DATE,
                    UserUIContext.formatPrettyTime(createdDate)));
            createdDateLbl.setDescription(UserUIContext.formatDateTime(createdDate));

            Date updatedDate = (Date) PropertyUtils.getProperty(bean, "lastupdatedtime");
            Label updatedDateLbl = new Label(UserUIContext.getMessage(CrmCommonI18nEnum.ITEM_UPDATED_DATE,
                    UserUIContext.formatPrettyTime(updatedDate)));
            updatedDateLbl.setDescription(UserUIContext.formatDateTime(updatedDate));

            layout.with(createdDateLbl, updatedDateLbl);
            this.addComponent(layout);
        } catch (Exception e) {
            LOG.error("Get date is failed {}", BeanUtility.printBeanObj(bean));
        }
    }
}
