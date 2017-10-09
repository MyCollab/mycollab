package com.mycollab.module.crm.ui.components;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.UserLink;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class PeopleInfoComp extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PeopleInfoComp.class);

    public void displayEntryPeople(ValuedBean bean) {
        this.removeAllComponents();
        this.withMargin(false);

        Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " +
                UserUIContext.getMessage(CrmCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
        peopleInfoHeader.setStyleName("info-hdr");
        this.addComponent(peopleInfoHeader);

        GridLayout layout = new GridLayout(2, 2);
        layout.setSpacing(true);
        layout.setWidth("100%");
        layout.setMargin(new MarginInfo(false, false, false, true));
        try {
            Label createdLbl = new Label(UserUIContext.getMessage(CrmCommonI18nEnum.ITEM_CREATED_PEOPLE));
            createdLbl.setSizeUndefined();
            layout.addComponent(createdLbl, 0, 0);

            String createdUserName = (String) PropertyUtils.getProperty(bean, "createduser");
            String createdUserAvatarId = (String) PropertyUtils.getProperty(bean, "createdUserAvatarId");
            String createdUserDisplayName = (String) PropertyUtils.getProperty(bean, "createdUserFullName");

            UserLink createdUserLink = new UserLink(createdUserName, createdUserAvatarId, createdUserDisplayName);
            layout.addComponent(createdUserLink, 1, 0);
            layout.setColumnExpandRatio(1, 1.0f);

            Label assigneeLbl = new Label(UserUIContext.getMessage(CrmCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
            assigneeLbl.setSizeUndefined();
            layout.addComponent(assigneeLbl, 0, 1);
            String assignUserName = (String) PropertyUtils.getProperty(bean, "assignuser");
            String assignUserAvatarId = (String) PropertyUtils.getProperty(bean, "assignUserAvatarId");
            String assignUserDisplayName = (String) PropertyUtils.getProperty(bean, "assignUserFullName");

            UserLink assignUserLink = new UserLink(assignUserName, assignUserAvatarId, assignUserDisplayName);
            layout.addComponent(assignUserLink, 1, 1);
        } catch (Exception e) {
            LOG.error("Can not build user link {} ", BeanUtility.printBeanObj(bean));
        }

        this.addComponent(layout);

    }
}
