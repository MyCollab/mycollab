package com.mycollab.community.module.project.view.reports;

import com.hp.gagawa.java.elements.A;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectReportI18nEnum;
import com.mycollab.module.project.view.reports.IReportContainer;
import com.mycollab.module.project.view.reports.ReportBreadcrumb;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
@ViewComponent
public class ReportContainerImpl extends AbstractVerticalPageView implements IReportContainer {
    private MVerticalLayout body;

    public ReportContainerImpl() {
        withMargin(true);
        ReportBreadcrumb breadcrumb = ViewManager.getCacheComponent(ReportBreadcrumb.class);
        body = new MVerticalLayout().withMargin(new MarginInfo(true, false, true, false));
        with(breadcrumb, ELabel.hr(), body).expand(body);
    }

    @Override
    public void showDashboard() {
        body.removeAllComponents();
        body.with(ELabel.h2(VaadinIcons.PIE_CHART.getHtml() + " " + UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_REPORTS)));
        MCssLayout contentLayout = new MCssLayout().withStyleName(WebThemes.FLEX_DISPLAY);

        MVerticalLayout standupConsole = new MVerticalLayout().withWidth("300px").withStyleName("member-block");
        standupConsole.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        standupConsole.addComponent(ELabel.fontIcon(VaadinIcons.CALENDAR_CLOCK).withStyleName("icon-38px"));
        A standupReportLink = new A(ProjectLinkGenerator.generateStandupDashboardLink())
                .appendText(UserUIContext.getMessage(ProjectReportI18nEnum.REPORT_STANDUP));
        standupConsole.addComponent(ELabel.h3(standupReportLink.write()).withUndefinedWidth());
        standupConsole.addComponent(new ELabel(UserUIContext.getMessage(ProjectReportI18nEnum.REPORT_STANDUP_HELP)).withFullWidth());
        contentLayout.addComponent(standupConsole);


        MVerticalLayout userWorkloadReport = new MVerticalLayout().withWidth("300px").withStyleName("member-block");
        userWorkloadReport.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        userWorkloadReport.addComponent(ELabel.fontIcon(VaadinIcons.CALENDAR_CLOCK).withStyleName("icon-38px"));
        A userWorkloadReportLink = new A(ProjectLinkGenerator.generateUsersWorkloadReportLink())
                .appendText(UserUIContext.getMessage(ProjectReportI18nEnum.REPORT_USERS_WORKLOAD));
        userWorkloadReport.addComponent(ELabel.h3(userWorkloadReportLink.write()).withUndefinedWidth());
        userWorkloadReport.addComponent(new ELabel(UserUIContext.getMessage(ProjectReportI18nEnum.REPORT_USERS_WORKLOAD_HELP)).withFullWidth());
        contentLayout.addComponent(userWorkloadReport);

        body.with(contentLayout).expand(contentLayout).withAlign(contentLayout, Alignment.TOP_LEFT);
    }

    @Override
    public void addView(PageView view) {
        body.removeAllComponents();
        body.with(view).expand(view);
    }
}
