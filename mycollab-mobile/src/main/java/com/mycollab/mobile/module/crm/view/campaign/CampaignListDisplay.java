package com.mycollab.mobile.module.crm.view.campaign;

import com.hp.gagawa.java.elements.A;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.service.CampaignService;
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
public class CampaignListDisplay extends DefaultPagedBeanList<CampaignService, CampaignSearchCriteria, SimpleCampaign> {
    private static final long serialVersionUID = -2234454107835680053L;

    public CampaignListDisplay() {
        super(AppContextUtil.getSpringBean(CampaignService.class), new CampaignRowDisplayHandler());
    }

    private static class CampaignRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleCampaign> {

        @Override
        public Component generateRow(IBeanList<SimpleCampaign> host, final SimpleCampaign campaign, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
            A itemLink = new A(CrmLinkBuilder.generateCampaignPreviewLinkFull(campaign.getId())).appendText(campaign.getCampaignname());
            MCssLayout itemWrap = new MCssLayout(ELabel.html(itemLink.write()));
            rowLayout.addComponent(new MHorizontalLayout(ELabel.fontIcon(CrmAssetsManager.getAsset
                    (CrmTypeConstants.CAMPAIGN)), itemWrap).expand(itemWrap).withFullWidth());
            return rowLayout;
        }

    }
}
