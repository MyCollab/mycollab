/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.event.ClientEvent;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.event.ReportEvent;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.service.TicketComponentFactory;
import com.mycollab.module.project.view.user.ProjectPagedList;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractSingleContainerPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.web.IDesktopModule;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
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
            serviceMenuContainer = new MHorizontalLayout().withHeight("45px").withMargin(new MarginInfo(false, true,
                    false, true)).withStyleName("service-menu");
            serviceMenuContainer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            MButton boardBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_BOARD), clickEvent -> {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoUserDashboard(this, null));
            });
            serviceMenuContainer.with(boardBtn);

            Button switchPrjBtn = new SwitchProjectPopupButton();
            serviceMenuContainer.with(switchPrjBtn);

            if (!SiteConfiguration.isCommunityEdition()) {
                MButton clientBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_CLIENTS), clickEvent -> {
                    EventBusFactory.getInstance().post(new ClientEvent.GotoList(this, null));
                });

                MButton reportBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_REPORTS), clickEvent -> {
                    EventBusFactory.getInstance().post(new ReportEvent.GotoConsole(this));
                });
                serviceMenuContainer.with(clientBtn, reportBtn);
            }

            PopupButton newBtn = new PopupButton(UserUIContext.getMessage(GenericI18Enum.ACTION_NEW));
            newBtn.addStyleName("add-btn-popup");
            newBtn.setIcon(VaadinIcons.PLUS_CIRCLE);
            OptionPopupContent contentLayout = new OptionPopupContent();

            if (UserUIContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT)) {
                MButton newPrjButton = new MButton(UserUIContext.getMessage(ProjectI18nEnum.SINGLE), clickEvent -> {
                    UI.getCurrent().addWindow(ViewManager.getCacheComponent(AbstractProjectAddWindow.class));
                    newBtn.setPopupVisible(false);
                }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT));
                contentLayout.addOption(newPrjButton);
            }

            MButton newTicketButton = new MButton(UserUIContext.getMessage(TicketI18nEnum.SINGLE), clickEvent -> {
                UI.getCurrent().addWindow(AppContextUtil.getSpringBean(TicketComponentFactory.class)
                        .createNewTicketWindow(null, null, null, false));
                newBtn.setPopupVisible(false);
            }).withIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TICKET));
            contentLayout.addOption(newTicketButton);

            newBtn.setContent(contentLayout);

            serviceMenuContainer.with(newBtn).withAlign(newBtn, Alignment.MIDDLE_LEFT);
        }

        return serviceMenuContainer;
    }

    private class SwitchProjectPopupButton extends PopupButton {
        private boolean isSortAsc = true;
        private ProjectSearchCriteria searchCriteria;

        private ELabel titleLbl;
        private ProjectPagedList projectList;

        SwitchProjectPopupButton() {
            super(UserUIContext.getMessage(ProjectI18nEnum.LIST));
            addStyleName("myprojectlist add-btn-popup");
            projectList = new ProjectPagedList();

            searchCriteria = new ProjectSearchCriteria();
            searchCriteria.setInvolvedMember(StringSearchField.and(UserUIContext.getUsername()));
            searchCriteria.setProjectStatuses(new SetSearchField<>(StatusI18nEnum.Open.name()));

            titleLbl = ELabel.h2(UserUIContext.getMessage(ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, 0));
            OptionPopupContent contentLayout = new OptionPopupContent();
            contentLayout.setWidth("550px");

            final MButton sortBtn = new MButton(VaadinIcons.CARET_UP);
            sortBtn.withListener(clickEvent -> {
                isSortAsc = !isSortAsc;
                if (searchCriteria != null) {
                    if (isSortAsc) {
                        sortBtn.setIcon(VaadinIcons.CARET_UP);
                        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.ASC)));
                    } else {
                        sortBtn.setIcon(VaadinIcons.CARET_DOWN);
                        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField("name", SearchCriteria.DESC)));
                    }
                    displayResults();
                }
            }).withStyleName(WebThemes.BUTTON_ICON_ONLY);

            final TextField searchField = new TextField();
//            searchField.setInputPrompt(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchField.setWidth("200px");
            MButton searchBtn = new MButton("", clickEvent -> {
                searchCriteria.setProjectName(StringSearchField.and(searchField.getValue()));
                displayResults();
            }).withIcon(VaadinIcons.SEARCH).withStyleName(WebThemes.BUTTON_ACTION);

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
