package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.fielddef.CampaignTableFieldDef;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private CampaignTableDisplay tableItem;
    private FieldSelection<CampaignWithBLOBs> fieldSelection;

    public CampaignSelectionWindow(FieldSelection<CampaignWithBLOBs> fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(CampaignI18nEnum.SINGLE)));
        this.withModal(true).withResizable(false).withWidth("1000px").withCenter();
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createCampaignList();
        CampaignSearchPanel campaignSimpleSearchPanel = new CampaignSearchPanel(false);
        campaignSimpleSearchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        this.setContent(new MVerticalLayout(campaignSimpleSearchPanel, tableItem));
        tableItem.setSearchCriteria(new CampaignSearchCriteria());
    }

    private void createCampaignList() {
        tableItem = new CampaignTableDisplay(Arrays.asList(CampaignTableFieldDef.campaignname, CampaignTableFieldDef.type,
                CampaignTableFieldDef.status, CampaignTableFieldDef.endDate, CampaignTableFieldDef.assignUser));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("campaignname", (source, itemId, columnId) -> {
            final SimpleCampaign campaign = tableItem.getBeanByIndex(itemId);

            return new MButton(campaign.getCampaignname(), clickEvent -> {
                fieldSelection.fireValueChange(campaign);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateTooltipCampaign(UserUIContext.getUserLocale(),
                    AppUI.getDateFormat(), campaign, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
    }
}
