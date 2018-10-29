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

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.domain.ProjectGenericItem;
import com.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectGenericItemService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.ui.components.GenericItemRowDisplayHandler;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.SearchTextField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserDashboardViewImpl extends AbstractVerticalPageView implements UserDashboardView {
    private static final long serialVersionUID = 1L;

    private ProjectService prjService;
    private List<Integer> prjKeys;

    private TabSheet tabSheet;

    public UserDashboardViewImpl() {
        this.withMargin(new MarginInfo(false, false, true, false)).withFullWidth();

        prjService = AppContextUtil.getSpringBean(ProjectService.class);
        prjKeys = prjService.getProjectKeysUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());

        tabSheet = new TabSheet();
        tabSheet.addTab(buildDashboardComp(), UserUIContext.getMessage(GenericI18Enum.VIEW_DASHBOARD), VaadinIcons.DASHBOARD);
        tabSheet.addTab(buildProjectListComp(), UserUIContext.getMessage(ProjectI18nEnum.LIST), VaadinIcons.BUILDING_O);
        tabSheet.addTab(buildFollowingTicketComp(), UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES), VaadinIcons.EYE);
        if (!SiteConfiguration.isCommunityEdition()) {
            tabSheet.addTab(buildCalendarComp(), UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_CALENDAR), VaadinIcons.CALENDAR);
        }

        tabSheet.addSelectedTabChangeListener(selectedTabChangeEvent -> {
            CssLayout comp = (CssLayout) tabSheet.getSelectedTab();
            comp.removeAllComponents();
            int tabIndex = tabSheet.getTabPosition(tabSheet.getTab(comp));
            if (tabIndex == 0) {
                UserProjectDashboardPresenter userProjectDashboardPresenter = PresenterResolver.getPresenterAndInitView
                        (UserProjectDashboardPresenter.class);
                userProjectDashboardPresenter.onGo(comp, null);
            } else if (tabIndex == 2) {
                FollowingTicketPresenter followingTicketPresenter = PresenterResolver.getPresenterAndInitView
                        (FollowingTicketPresenter.class);
                followingTicketPresenter.onGo(comp, null);
            } else if (tabIndex == 4) {
                SettingPresenter settingPresenter = PresenterResolver.getPresenter(SettingPresenter.class);
                settingPresenter.onGo(comp, null);
            } else if (tabIndex == 3) {
                ICalendarDashboardPresenter calendarPresenter = PresenterResolver.getPresenterAndInitView
                        (ICalendarDashboardPresenter.class);
                calendarPresenter.go(comp, null);
            } else if (tabIndex == 1) {
                ProjectListPresenter projectListPresenter = PresenterResolver.getPresenterAndInitView(ProjectListPresenter.class);
                projectListPresenter.onGo(comp, null);
            }
        });

        this.with(setupHeader(), tabSheet).expand(tabSheet);
    }

    @Override
    public void showDashboard() {
        CssLayout comp = (CssLayout) tabSheet.getSelectedTab();
        if (tabSheet.getTabPosition(tabSheet.getTab(comp)) == 0) {
            UserProjectDashboardPresenter userProjectDashboardPresenter = PresenterResolver.getPresenterAndInitView
                    (UserProjectDashboardPresenter.class);
            userProjectDashboardPresenter.onGo(comp, null);
        } else {
            tabSheet.setSelectedTab(0);
        }

        if (UserUIContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT)) {
            int countActiveProjects = prjService.getTotalActiveProjectsOfInvolvedUsers(UserUIContext.getUsername(), AppUI.getAccountId());
            if (countActiveProjects == 0) {
                UI.getCurrent().addWindow(new AskCreateNewProjectWindow());
            }
        }
    }

    @Override
    public void showProjectList() {
        tabSheet.setSelectedTab(1);
    }

    @Override
    public List<Integer> getInvolvedProjectKeys() {
        return prjKeys;
    }

    private Component buildDashboardComp() {
        return new MCssLayout().withFullWidth();
    }

    private Component buildProjectListComp() {
        return new MCssLayout().withFullWidth();
    }

    private Component buildFollowingTicketComp() {
        return new MCssLayout().withFullWidth();
    }

    private Component buildCalendarComp() {
        return new MCssLayout().withFullWidth();
    }

    private Component buildSettingComp() {
        return new MCssLayout().withFullWidth();
    }

    private ComponentContainer setupHeader() {
        MHorizontalLayout headerWrapper = new MHorizontalLayout().withFullWidth().withStyleName("projectfeed-hdr-wrapper");

        Image avatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(UserUIContext.getUserAvatarId(), 64);
        avatar.setStyleName(UIConstants.CIRCLE_BOX);
        headerWrapper.addComponent(avatar);

        MVerticalLayout headerContent = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true));

        ELabel headerLabel = ELabel.h2(UserUIContext.getUser().getDisplayName()).withStyleName(UIConstants.TEXT_ELLIPSIS);
        MHorizontalLayout headerContentTop = new MHorizontalLayout();
        headerContentTop.with(headerLabel).withAlign(headerLabel, Alignment.TOP_LEFT).expand(headerLabel);

        SearchTextField searchTextField = new SearchTextField() {
            @Override
            public void doSearch(String value) {
                displaySearchResult(value);
            }

            @Override
            public void emptySearch() {

            }
        };
        headerContentTop.with(searchTextField).withAlign(searchTextField, Alignment.TOP_RIGHT);
        headerContent.with(headerContentTop);
        MHorizontalLayout metaInfoLayout = new MHorizontalLayout();
        if (Boolean.TRUE.equals(AppUI.getBillingAccount().getDisplayemailpublicly())) {
            metaInfoLayout.with(new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL) + ": ").withStyleName(UIConstants.META_INFO),
                    ELabel.html(new A(String.format("mailto:%s", UserUIContext.getUsername())).appendText(UserUIContext.getUsername()).write()));
        }
        metaInfoLayout.with(ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_SINCE,
                UserUIContext.formatPrettyTime(UserUIContext.getUser().getRegisteredtime()))));
        metaInfoLayout.with(ELabel.html(UserUIContext.getMessage(UserI18nEnum.OPT_MEMBER_LOGGED_IN,
                UserUIContext.formatPrettyTime(UserUIContext.getUser().getLastaccessedtime()))));
        metaInfoLayout.alignAll(Alignment.TOP_LEFT);
        headerContent.addComponent(metaInfoLayout);
        headerWrapper.with(headerContent).expand(headerContent);
        return headerWrapper;
    }

    private static final String headerTitle = String.format("%s Search for '%%s' (Found: %%d)", VaadinIcons.SEARCH.getHtml());

    private void displaySearchResult(String value) {
        removeAllComponents();
        Component headerWrapper = setupHeader();

        MVerticalLayout layout = new MVerticalLayout().withFullWidth();
        with(headerWrapper, layout).expand(layout);

        MHorizontalLayout headerComp = new MHorizontalLayout();
        ELabel headerLbl = ELabel.h2(String.format(headerTitle, value, 0));
        Button backDashboard = new Button("Back to workboard", clickEvent -> showDashboard());
        backDashboard.setStyleName(WebThemes.BUTTON_ACTION);
        headerComp.with(headerLbl, backDashboard).alignAll(Alignment.MIDDLE_LEFT);
        layout.with(headerComp);

        ProjectService prjService = AppContextUtil.getSpringBean(ProjectService.class);
        prjKeys = prjService.getProjectKeysUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        if (CollectionUtils.isNotEmpty(prjKeys)) {
            ProjectGenericItemSearchCriteria searchCriteria = new ProjectGenericItemSearchCriteria();
            searchCriteria.setPrjKeys(new SetSearchField<>(prjKeys.toArray(new Integer[prjKeys.size()])));
            searchCriteria.setTxtValue(StringSearchField.and(value));

            DefaultBeanPagedList<ProjectGenericItemService, ProjectGenericItemSearchCriteria, ProjectGenericItem>
                    searchItemsTable = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(ProjectGenericItemService.class),
                    new GenericItemRowDisplayHandler());
            searchItemsTable.setControlStyle("borderlessControl");
            int foundNum = searchItemsTable.setSearchCriteria(searchCriteria);
            headerLbl.setValue(String.format(headerTitle, value, foundNum));
            layout.with(searchItemsTable).expand(searchItemsTable);
        }
    }

    private static class AskCreateNewProjectWindow extends MWindow {
        AskCreateNewProjectWindow() {
            super(UserUIContext.getMessage(GenericI18Enum.OPT_QUESTION));
            MVerticalLayout content = new MVerticalLayout();
            this.withWidth("600px").withResizable(false).withModal(true).withCenter().withContent(content);

            content.with(new Label(UserUIContext.getMessage(ProjectI18nEnum.OPT_TO_ADD_PROJECT)));

            MButton skipBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_SKIP), clickEvent -> close())
                    .withStyleName(WebThemes.BUTTON_OPTION);

            MButton createNewBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.NEW), clickEvent -> {
                UI.getCurrent().addWindow(ViewManager.getCacheComponent(AbstractProjectAddWindow.class));
                close();
            }).withStyleName(WebThemes.BUTTON_ACTION);

            MHorizontalLayout btnControls = new MHorizontalLayout(skipBtn, createNewBtn);
            content.with(btnControls).withAlign(btnControls, Alignment.MIDDLE_RIGHT);
        }
    }
}