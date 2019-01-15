package com.mycollab.module.project.view;

import com.mycollab.common.i18n.ClientI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.view.client.IClientPresenter;
import com.mycollab.module.project.view.parameters.ClientScreenData;
import com.mycollab.module.project.view.reports.IReportPresenter;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.TabSheet;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
@ViewComponent
public class BoardContainer extends VerticalTabsheet implements PageView {

    private UserProjectDashboardPresenter userProjectDashboardPresenter;

    private FollowingTicketPresenter followingTicketPresenter;

    private ProjectListPresenter projectListPresenter;

    private IReportPresenter reportPresenter;

    private IClientPresenter clientPresenter;

    public BoardContainer() {
        this.setSizeFull();
        this.setNavigatorStyleName("sidebar-menu");
        this.setNavigatorWidth("200px");
        CssLayout contentWrapper = this.getContentWrapper();

        this.buildComponents();
    }

    private void buildComponents() {
        this.addTab(constructDashboardComponent(), "Dashboard",
                UserUIContext.getMessage(GenericI18Enum.VIEW_DASHBOARD), VaadinIcons.DASHBOARD);

        this.addTab(constructProjectsViewComponent(), "Projects",
                UserUIContext.getMessage(ProjectI18nEnum.LIST), VaadinIcons.BUILDING_O);

        this.addTab(constructFollowingTicketsComponent(), "FollowingTickets",
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES), VaadinIcons.EYE);

        this.addTab(constructReportViewComponent(), "Reports",
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_REPORTS), VaadinIcons.RETWEET);

        this.addTab(constructClientViewComponent(), "Clients",
                UserUIContext.getMessage(ClientI18nEnum.LIST), VaadinIcons.COIN_PILES);

        this.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                ButtonTab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                String tabId = tab.getTabId();
                if ("Dashboard".equals(tabId)) {
                    userProjectDashboardPresenter.go(BoardContainer.this, null);
                } else if ("Projects".equals(tabId)) {
                    projectListPresenter.go(BoardContainer.this, null);
                } else if ("FollowingTickets".equals(tabId)) {
                    followingTicketPresenter.go(BoardContainer.this, null);
                } else if ("Reports".equals(tabId)) {
                    reportPresenter.go(BoardContainer.this, null);
                } else if ("Clients".equals(tabId)) {
                    clientPresenter.go(BoardContainer.this, new ClientScreenData.Search(null));
                }
            }
        });
    }

    private HasComponents constructDashboardComponent() {
        userProjectDashboardPresenter = PresenterResolver.getPresenter(UserProjectDashboardPresenter.class);
        return userProjectDashboardPresenter.getView();
    }

    private HasComponents constructFollowingTicketsComponent() {
        followingTicketPresenter = PresenterResolver.getPresenter(FollowingTicketPresenter.class);
        return followingTicketPresenter.getView();
    }

    private HasComponents constructProjectsViewComponent() {
        projectListPresenter = PresenterResolver.getPresenter(ProjectListPresenter.class);
        return projectListPresenter.getView();
    }

    private HasComponents constructReportViewComponent() {
        reportPresenter = PresenterResolver.getPresenter(IReportPresenter.class);
        return reportPresenter.getView();
    }

    private HasComponents constructClientViewComponent() {
        clientPresenter = PresenterResolver.getPresenter(IClientPresenter.class);
        return clientPresenter.getView();
    }

    public void gotoSubView(String viewId) {
        this.selectTab(viewId);
    }
}
