package com.mycollab.mobile.module.crm.view.opportunity;

import com.hp.gagawa.java.elements.A;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.service.OpportunityService;
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
public class OpportunityListDisplay extends DefaultPagedBeanList<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
    private static final long serialVersionUID = -2350731660593521985L;

    public OpportunityListDisplay() {
        super(AppContextUtil.getSpringBean(OpportunityService.class), new OpportunityRowDisplayHandler());
    }

    static public class OpportunityRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleOpportunity> {

        @Override
        public Component generateRow(IBeanList<SimpleOpportunity> host, final SimpleOpportunity opportunity, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
            A itemLink = new A(CrmLinkBuilder.generateOpportunityPreviewLinkFull(opportunity.getId())).appendText(opportunity.getOpportunityname());
            MCssLayout itemWrap = new MCssLayout(ELabel.html(itemLink.write()));
            rowLayout.addComponent(new MHorizontalLayout(ELabel.fontIcon(CrmAssetsManager.getAsset
                    (CrmTypeConstants.OPPORTUNITY)), itemWrap).expand(itemWrap).withFullWidth());
            return rowLayout;
        }
    }
}
