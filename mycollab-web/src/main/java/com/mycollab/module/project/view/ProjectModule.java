/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.view.reports.IReportPresenter;
import com.mycollab.shell.event.ShellEvent.ShowAssociateAddActionsPerModule;
import com.mycollab.shell.view.AbstractMainView;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractSingleContainerPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.mycollab.web.IDesktopModule;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectModule extends AbstractSingleContainerPageView implements IDesktopModule {
    private static final long serialVersionUID = 1L;

    private VerticalTabsheet tabSheet;

    private UserProjectDashboardPresenter userProjectDashboardPresenter;

    private FollowingTicketPresenter followingTicketPresenter;

    private ProjectListPresenter projectListPresenter;

    private IReportPresenter reportPresenter;

//    private IClientPresenter clientPresenter;

    private ApplicationEventListener<ShowAssociateAddActionsPerModule> showAddActionsHandler = new
            ApplicationEventListener<ShowAssociateAddActionsPerModule>() {
                @Override
                @Subscribe
                public void handle(ShowAssociateAddActionsPerModule event) {
                    showProjectAddMenu();
                }
            };

    public ProjectModule() {
        addStyleName("module");
        setSizeFull();
        ControllerRegistry.addController(new ProjectModuleController(this));

        tabSheet = new VerticalTabsheet();
        tabSheet.setSizeFull();
        tabSheet.setNavigatorStyleName("sidebar-menu");
        CssLayout contentWrapper = tabSheet.getContentWrapper();
        contentWrapper.addStyleName("main-content");

        this.buildComponents();
        this.setContent(tabSheet);
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(showAddActionsHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(showAddActionsHandler);
        super.detach();
    }

    private void buildComponents() {
        tabSheet.addTab(constructDashboardComponent(), "Dashboard",
                UserUIContext.getMessage(GenericI18Enum.VIEW_DASHBOARD), VaadinIcons.DASHBOARD);

        tabSheet.addTab(constructProjectsViewComponent(), "Projects",
                UserUIContext.getMessage(ProjectI18nEnum.LIST), VaadinIcons.BUILDING_O);

        tabSheet.addTab(constructFollowingTicketsComponent(), "FollowingTickets",
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES), VaadinIcons.EYE);

        tabSheet.addTab(constructReportViewComponent(), "Reports",
                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_REPORTS), VaadinIcons.RETWEET);

//        tabSheet.addTab(constructClientViewComponent(), "Clients",
//                UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_REPORTS), VaadinIcons.RETWEET);

        tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                TabSheet.Tab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                String tabId = ((VerticalTabsheet.TabImpl) tab).getTabId();
                if ("Dashboard".equals(tabId)) {
                    userProjectDashboardPresenter.go(ProjectModule.this, null);
                } else if ("Projects".equals(tabId)) {
                    projectListPresenter.go(ProjectModule.this, null);
                } else if ("FollowingTickets".equals(tabId)) {
                    followingTicketPresenter.go(ProjectModule.this, null);
                } else if ("Reports".equals(tabId)) {
                    reportPresenter.go(ProjectModule.this, null);
//                } else if ("Clients".equals(tabId)) {
//                    clientPresenter.go(ProjectModule.this, null);
//                }
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

//    private HasComponents constructClientViewComponent() {
//        clientPresenter = PresenterResolver.getPresenter(IClientPresenter.class);
//        return clientPresenter.getView();
//    }

    public void gotoSubView(String viewId) {
        tabSheet.selectTab(viewId);
    }

    private void showProjectAddMenu() {
        MButton newProjectBtn = new MButton("New Project");
        MButton newTicketBtn = new MButton("New Ticket");
        MButton newDocumentBtn = new MButton("New Page");

        MVerticalLayout controlsLayout = new MVerticalLayout();
        controlsLayout.with(new Embedded("", AccountAssetsResolver.createLogoResource(AppUI.getBillingAccount().getLogopath(), 150)));
        controlsLayout.with(newProjectBtn, newTicketBtn, newDocumentBtn);

        SliderPanel topSlider = new SliderPanelBuilder(controlsLayout)
                .expanded(false)
                .flowInContent(true)
                .mode(SliderMode.LEFT)
                .caption("Top Slider")
                .tabPosition(SliderTabPosition.BEGINNING)
                .build();
        topSlider.expand();

        AbstractMainView mainView = UIUtils.getRoot(this, AbstractMainView.class);
        mainView.addComponent(topSlider, 0);

    }
}
