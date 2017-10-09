package com.mycollab.mobile.module.crm.view.lead;

import com.hp.gagawa.java.elements.A;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class LeadListDisplay extends DefaultPagedBeanList<LeadService, LeadSearchCriteria, SimpleLead> {
    private static final long serialVersionUID = -2350731660593521985L;

    public LeadListDisplay() {
        super(AppContextUtil.getSpringBean(LeadService.class), new LeadRowDisplayHandler());
    }

    private static class LeadRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleLead> {

        @Override
        public Component generateRow(IBeanList<SimpleLead> host, final SimpleLead lead, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
            A itemLink = new A(CrmLinkBuilder.generateLeadPreviewLinkFull(lead.getId())).appendText(lead.getLeadName());
            MCssLayout itemWrap = new MCssLayout(ELabel.html(itemLink.write()));
            rowLayout.addComponent(new MHorizontalLayout(ELabel.fontIcon(CrmAssetsManager.getAsset
                    (CrmTypeConstants.LEAD)), itemWrap).expand(itemWrap).withFullWidth());
            return rowLayout;
        }
    }
}
