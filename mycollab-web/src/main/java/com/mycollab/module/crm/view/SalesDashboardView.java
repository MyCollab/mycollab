package com.mycollab.module.crm.view;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.view.opportunity.OpportunityLeadSourceDashboard;
import com.mycollab.module.crm.view.opportunity.OpportunitySalesStageDashboard;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.Depot;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.hene.popupbutton.PopupButton;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SalesDashboardView extends Depot {
    private static final long serialVersionUID = 1L;
    private final String[] reportDashboard = {"OpportunitySalesStage", "OpportunityLeadSource"};
    private int currentReportIndex = 0;

    public SalesDashboardView() {
        super(UserUIContext.getMessage(OpportunityI18nEnum.OPT_SALES_DASHBOARD), new VerticalLayout());
        this.bodyContent.setSizeFull();
        this.initUI();
        this.setContentBorder(true);
    }

    void displayReport() {
        final String reportName = this.reportDashboard[this.currentReportIndex];

        final VerticalLayout bodyContent = (VerticalLayout) this.bodyContent;
        bodyContent.removeAllComponents();
        bodyContent.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        if ("OpportunitySalesStage".equals(reportName)) {
            this.setTitle(UserUIContext.getMessage(OpportunityI18nEnum.OPT_SALES_STAGE));
            OpportunitySalesStageDashboard salesStageDashboard = new OpportunitySalesStageDashboard();
            bodyContent.addComponent(salesStageDashboard);

            final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
            criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            salesStageDashboard.displayChart(criteria);
        } else if ("OpportunityLeadSource".equals(reportName)) {
            this.setTitle(UserUIContext.getMessage(OpportunityI18nEnum.OPT_LEAD_SOURCES));
            OpportunityLeadSourceDashboard leadSourceDashboard = new OpportunityLeadSourceDashboard();
            bodyContent.addComponent(leadSourceDashboard);

            final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
            criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            leadSourceDashboard.displayChart(criteria);
        }
    }

    private void initUI() {
        final PopupButton saleChartPopup = new PopupButton("");
        saleChartPopup.setIcon(FontAwesome.CARET_SQUARE_O_DOWN);
        saleChartPopup.setStyleName(WebThemes.BUTTON_ICON_ONLY);

        final OptionPopupContent filterBtnLayout = new OptionPopupContent();

        final Button btnOpportunitySales = new Button(UserUIContext.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE), clickEvent -> {
            saleChartPopup.setPopupVisible(false);
            currentReportIndex = 0;
            displayReport();
        });
        filterBtnLayout.addOption(btnOpportunitySales);

        final Button btnOpportunityLead = new Button(UserUIContext.getMessage(OpportunityI18nEnum.FORM_LEAD_SOURCE), clickEvent -> {
            saleChartPopup.setPopupVisible(false);
            currentReportIndex = 1;
            displayReport();
        });
        filterBtnLayout.addOption(btnOpportunityLead);

        this.displayReport();
        saleChartPopup.setContent(filterBtnLayout);
        this.addHeaderElement(saleChartPopup);
    }
}
