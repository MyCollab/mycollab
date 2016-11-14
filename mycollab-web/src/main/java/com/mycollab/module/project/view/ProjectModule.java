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
package com.mycollab.module.project.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.event.ClientEvent;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.event.ReportEvent;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.view.user.ProjectPagedList;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractSingleContainerPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.ServiceMenu;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.web.IDesktopModule;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectModule extends AbstractSingleContainerPageView implements IDesktopModule {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout serviceMenuContainer;

    public ProjectModule() {
        addStyleName("module");
        setSizeFull();
        ControllerRegistry.addController(new ProjectModuleController(this));
    }

    @Override
    public MHorizontalLayout buildMenu() {
        if (serviceMenuContainer == null) {
            serviceMenuContainer = new MHorizontalLayout();
            final ServiceMenu serviceMenu = new ServiceMenu();
            serviceMenu.addService(UserUIContext.getMessage(ProjectI18nEnum.LIST), clickEvent -> {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoUserDashboard(this, null));
                serviceMenu.selectService(0);
            });
            serviceMenu.selectService(0);

            if (!SiteConfiguration.isCommunityEdition()) {
                serviceMenu.addService(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_CLIENTS), clickEvent -> {
                    EventBusFactory.getInstance().post(new ClientEvent.GotoList(this, null));
                    serviceMenu.selectService(1);
                });

                serviceMenu.addService(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_REPORTS), clickEvent -> {
                    EventBusFactory.getInstance().post(new ReportEvent.GotoConsole(this));
                    serviceMenu.selectService(2);
                });
            }

            serviceMenuContainer.with(serviceMenu);

            MButton newPrjBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.NEW),
                    clickEvent -> UI.getCurrent().addWindow(ViewManager.getCacheComponent(AbstractProjectAddWindow.class)))
                    .withStyleName("add-btn-popup").withIcon(FontAwesome.PLUS_CIRCLE);
            newPrjBtn.setVisible(UserUIContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT));
            serviceMenuContainer.with(newPrjBtn).withAlign(newPrjBtn, Alignment.MIDDLE_LEFT);

            Button switchPrjBtn = new SwitchProjectPopupButton();
            serviceMenuContainer.with(switchPrjBtn).withAlign(switchPrjBtn, Alignment.MIDDLE_LEFT);
        }

        return serviceMenuContainer;
    }

    private class SwitchProjectPopupButton extends PopupButton {
        private boolean isSortAsc = true;
        private ProjectSearchCriteria searchCriteria;

        private ELabel titleLbl;
        private ProjectPagedList projectList;

        SwitchProjectPopupButton() {
            super(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_SWITCH_PROJECT));
            setStyleName("myprojectlist");
            addStyleName("add-btn-popup");
            setIcon(VaadinIcons.ARROW_CIRCLE_RIGHT_O);
            projectList = new ProjectPagedList();

            searchCriteria = new ProjectSearchCriteria();
            searchCriteria.setInvolvedMember(StringSearchField.and(UserUIContext.getUsername()));
            searchCriteria.setProjectStatuses(new SetSearchField<>(OptionI18nEnum.StatusI18nEnum.Open.name()));

            titleLbl = ELabel.h2(UserUIContext.getMessage(ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, 0));
            OptionPopupContent contentLayout = new OptionPopupContent();
            contentLayout.setWidth("550px");

            final MButton sortBtn = new MButton(FontAwesome.SORT_ALPHA_ASC);
            sortBtn.withListener(clickEvent -> {
                isSortAsc = !isSortAsc;
                if (searchCriteria != null) {
                    if (isSortAsc) {
                        sortBtn.setIcon(FontAwesome.SORT_ALPHA_ASC);
                        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.ASC)));
                    } else {
                        sortBtn.setIcon(FontAwesome.SORT_ALPHA_DESC);
                        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.DESC)));
                    }
                    displayResults();
                }
            }).withStyleName(WebThemes.BUTTON_ICON_ONLY);

            final TextField searchField = new TextField();
            searchField.setInputPrompt(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchField.setWidth("200px");
            MButton searchBtn = new MButton("", clickEvent -> {
                searchCriteria.setProjectName(StringSearchField.and(searchField.getValue()));
                displayResults();
            }).withIcon(FontAwesome.SEARCH).withStyleName(WebThemes.BUTTON_ACTION);

            MHorizontalLayout popupHeader = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true))
                    .withFullWidth().withStyleName("border-bottom");
            MHorizontalLayout searchPanel = new MHorizontalLayout(searchField, searchBtn).withMargin(true);
            popupHeader.with(titleLbl, sortBtn, searchPanel).expand(titleLbl).alignAll(Alignment.MIDDLE_LEFT);
            contentLayout.addBlankOption(popupHeader);
            contentLayout.addBlankOption(projectList);
            setContent(contentLayout);

            addPopupVisibilityListener(popupVisibilityEvent -> {
                if (popupVisibilityEvent.isPopupVisible()) {
                    displayResults();
                }
            });
        }

        private void displayResults() {
            int count = projectList.setSearchCriteria(searchCriteria);
            titleLbl.setValue(UserUIContext.getMessage(ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, count));
        }
    }
}
