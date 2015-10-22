/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.view.user.ProjectPagedList;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ServiceMenu;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.web.IDesktopModule;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectModule extends AbstractPageView implements IDesktopModule {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout serviceMenuContainer;

    public ProjectModule() {
        setStyleName("project-module");
        setSizeFull();
        ControllerRegistry.addController(new ProjectModuleController(this));
    }

    public void gotoProjectPage() {
        UserDashboardPresenter presenter = PresenterResolver.getPresenter(UserDashboardPresenter.class);
        presenter.go(this, null);
    }

    @Override
    public MHorizontalLayout buildMenu() {
        if (serviceMenuContainer == null) {
            serviceMenuContainer = new MHorizontalLayout();
            final ServiceMenu serviceMenu = new ServiceMenu();
            serviceMenu.addService("Projects", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{"dashboard"}));
                    serviceMenu.selectService(0);
                }
            });
            serviceMenu.addService("Timesheet", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{"timetracking"}));
                    serviceMenu.selectService(1);
                }
            });
            serviceMenu.addService("Calendar", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{"calendar"}));
                    serviceMenu.selectService(1);

                }
            });

            serviceMenuContainer.with(serviceMenu);

            if (AppContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT)) {
                Button newPrjBtn = new Button(AppContext.getMessage(ProjectCommonI18nEnum.BUTTON_NEW_PROJECT), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        UI.getCurrent().addWindow(new ProjectAddWindow());
                    }
                });
                newPrjBtn.addStyleName("add-btn-popup");
                newPrjBtn.setIcon(FontAwesome.PLUS_CIRCLE);
                serviceMenuContainer.with(newPrjBtn).withAlign(newPrjBtn, Alignment.MIDDLE_LEFT);
            }

            Button switchPrjBtn = buildSwitchProjectBtn();
            serviceMenuContainer.with(switchPrjBtn).withAlign(switchPrjBtn, Alignment.MIDDLE_LEFT);
        }

        return serviceMenuContainer;
    }

    private PopupButton buildSwitchProjectBtn() {
        final PopupButton switchProjectPopup = new PopupButton("Switch Project");
        switchProjectPopup.setStyleName("myprojectlist");
        switchProjectPopup.addStyleName("add-btn-popup");
        switchProjectPopup.setIcon(VaadinIcons.ARROW_CIRCLE_RIGHT_O);
        final ProjectPagedList projectList = new ProjectPagedList();
        projectList.addStyleName("contentWrapper");
        final Label titleLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, 0));
        titleLbl.setStyleName("h2");
        MVerticalLayout contentLayout = new MVerticalLayout().withWidth("500px");

        final TextField searchField = new TextField();
        searchField.setInputPrompt("Search");
        searchField.setWidth("200px");
        Button searchBtn = new Button("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
                searchCriteria.setInvolvedMember(new StringSearchField(AppContext.getUsername()));
                searchCriteria.setProjectStatuses(new SetSearchField<>(
                        new String[]{OptionI18nEnum.StatusI18nEnum.Open.name()}));
                searchCriteria.setProjectName(new StringSearchField(searchField.getValue()));
                int count = projectList.setSearchCriteria(searchCriteria);
                titleLbl.setValue(AppContext.getMessage(
                        ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, count));
            }
        });
        searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        searchBtn.setIcon(FontAwesome.SEARCH);

        MHorizontalLayout popupHeader = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false))
                .withWidth("100%");
        MHorizontalLayout searchPanel = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false));
        searchPanel.with(searchField, searchBtn);
        popupHeader.with(titleLbl, searchPanel).withAlign(titleLbl, Alignment.MIDDLE_LEFT).withAlign
                (searchPanel, Alignment.MIDDLE_RIGHT);
        contentLayout.with(popupHeader, projectList);
        switchProjectPopup.setContent(contentLayout);

        switchProjectPopup.addPopupVisibilityListener(new PopupButton.PopupVisibilityListener() {
            @Override
            public void popupVisibilityChange(PopupButton.PopupVisibilityEvent event) {
                if (event.isPopupVisible()) {
                    ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
                    searchCriteria.setInvolvedMember(new StringSearchField(AppContext.getUsername()));
                    searchCriteria.setProjectStatuses(new SetSearchField<>(new String[]{OptionI18nEnum.StatusI18nEnum.Open.name()}));
                    int count = projectList.setSearchCriteria(searchCriteria);
                    titleLbl.setValue(AppContext.getMessage(
                            ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, count));
                }
            }
        });
        return switchProjectPopup;
    }
}
