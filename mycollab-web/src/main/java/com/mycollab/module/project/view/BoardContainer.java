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
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import org.vaadin.viritin.layouts.MCssLayout;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
@ViewComponent
public class BoardContainer extends VerticalTabsheet implements PageView {

    public BoardContainer() {
        this.setSizeFull();
        MCssLayout contentWrapper = this.getContentWrapper();
        contentWrapper.withStyleName("content-height");
        addToggleNavigatorControl();
        this.buildComponents();
    }

    private void buildComponents() {
        this.addTab("Dashboard", UserUIContext.getMessage(GenericI18Enum.VIEW_DASHBOARD), VaadinIcons.DASHBOARD);

        this.addTab("Projects", UserUIContext.getMessage(ProjectI18nEnum.LIST), VaadinIcons.BUILDING_O);

        this.addTab("FollowingTickets", UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES), VaadinIcons.EYE);

        this.addTab("Reports", UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_REPORTS), VaadinIcons.RETWEET);

        this.addTab("Clients", UserUIContext.getMessage(ClientI18nEnum.LIST), VaadinIcons.COIN_PILES);

        this.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                ButtonTab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                String tabId = tab.getTabId();
                if ("Dashboard".equals(tabId)) {
                    UserProjectDashboardPresenter presenter = PresenterResolver.getPresenter(UserProjectDashboardPresenter.class);
                    presenter.go(BoardContainer.this, null);
                } else if ("Projects".equals(tabId)) {
                    ProjectListPresenter presenter = PresenterResolver.getPresenter(ProjectListPresenter.class);
                    presenter.go(BoardContainer.this, null);
                } else if ("FollowingTickets".equals(tabId)) {
                    FollowingTicketPresenter presenter = PresenterResolver.getPresenter(FollowingTicketPresenter.class);
                    presenter.go(BoardContainer.this, null);
                } else if ("Reports".equals(tabId)) {
                    IReportPresenter presenter = PresenterResolver.getPresenter(IReportPresenter.class);
                    presenter.go(BoardContainer.this, null);
                } else if ("Clients".equals(tabId)) {
                    IClientPresenter presenter = PresenterResolver.getPresenter(IClientPresenter.class);
                    presenter.go(BoardContainer.this, new ClientScreenData.Search(null));
                }
            }
        });
    }

    public void gotoSubView(String viewId, Component viewDisplay) {
        this.selectTab(viewId, viewDisplay);
    }
}
