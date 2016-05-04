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

import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.ProjectGenericItem;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectGenericItemService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.components.GenericItemRowDisplayHandler;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.web.ui.SearchTextField;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserDashboardViewImpl extends AbstractPageView implements UserDashboardView {
    private static final long serialVersionUID = 1L;

    private ProjectService prjService;
    private List<Integer> prjKeys;

    private TabSheet tabSheet;

    public UserDashboardViewImpl() {
        this.withMargin(false).withWidth("100%");

        prjService = ApplicationContextUtil.getSpringBean(ProjectService.class);
        prjKeys = prjService.getProjectKeysUserInvolved(AppContext.getUsername(), AppContext.getAccountId());

        tabSheet = new TabSheet();
        tabSheet.addTab(buildDashboardComp(), "Dashboard", FontAwesome.DASHBOARD);
        tabSheet.addTab(buildProjectListComp(), "Projects", FontAwesome.BUILDING_O);
        tabSheet.addTab(buildFollowingTicketComp(), "Following Items", FontAwesome.EYE);
        tabSheet.addTab(buildCalendarComp(), "Calendar", FontAwesome.CALENDAR);
//        tabSheet.addTab(buildSettingComp(), "Settings", FontAwesome.COG);

        tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
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

        if (AppContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT)) {
            int countActiveProjects = prjService.getTotalActiveProjectsOfInvolvedUsers(AppContext.getUsername(), AppContext.getAccountId());
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

        Image avatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(AppContext.getUserAvatarId(), 64);
        avatar.setStyleName(UIConstants.CIRCLE_BOX);
        headerWrapper.addComponent(avatar);

        MVerticalLayout headerContent = new MVerticalLayout().withMargin(new MarginInfo(false, false, false, true));

        ELabel headerLabel = ELabel.h2(AppContext.getUser().getDisplayName()).withStyleName(UIConstants.TEXT_ELLIPSIS);
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
        MHorizontalLayout metaInfoLayout = new MHorizontalLayout().with(new ELabel("Email:").withStyleName
                (UIConstants.LABEL_META_INFO), new ELabel(new A(String.format("mailto:%s", AppContext.getUsername()))
                .appendText(AppContext.getUsername()).write(), ContentMode.HTML));
        metaInfoLayout.with(new ELabel("Member since: ").withStyleName(UIConstants.LABEL_META_INFO),
                new ELabel(AppContext.formatPrettyTime(AppContext.getUser().getRegisteredtime())));
        metaInfoLayout.with(new ELabel("Logged in: ").withStyleName(UIConstants.LABEL_META_INFO),
                new ELabel(AppContext.formatPrettyTime(AppContext.getUser().getLastaccessedtime())));
        metaInfoLayout.alignAll(Alignment.TOP_LEFT);
        headerContent.addComponent(metaInfoLayout);
        headerWrapper.with(headerContent).expand(headerContent);
        return headerWrapper;
    }

    private static final String headerTitle = FontAwesome.SEARCH.getHtml() + " Search for '%s' (Found: %d)";

    private void displaySearchResult(String value) {
        removeAllComponents();
        Component headerWrapper = setupHeader();

        MVerticalLayout layout = new MVerticalLayout().withWidth("100%");
        with(headerWrapper, layout).expand(layout);

        MHorizontalLayout headerComp = new MHorizontalLayout();
        ELabel headerLbl = ELabel.h2(String.format(headerTitle, value, 0));
        Button backDashboard = new Button("Back to workboard", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showDashboard();
            }
        });
        backDashboard.setStyleName(UIConstants.BUTTON_ACTION);
        headerComp.with(headerLbl, backDashboard).alignAll(Alignment.MIDDLE_LEFT);
        layout.with(headerComp);

        ProjectService prjService = ApplicationContextUtil.getSpringBean(ProjectService.class);
        prjKeys = prjService.getProjectKeysUserInvolved(AppContext.getUsername(), AppContext.getAccountId());
        if (CollectionUtils.isNotEmpty(prjKeys)) {
            ProjectGenericItemSearchCriteria searchCriteria = new ProjectGenericItemSearchCriteria();
            searchCriteria.setPrjKeys(new SetSearchField<>(prjKeys.toArray(new Integer[prjKeys.size()])));
            searchCriteria.setTxtValue(StringSearchField.and(value));

            DefaultBeanPagedList<ProjectGenericItemService, ProjectGenericItemSearchCriteria, ProjectGenericItem>
                    searchItemsTable = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectGenericItemService.class),
                    new GenericItemRowDisplayHandler());
            searchItemsTable.setControlStyle("borderlessControl");
            int foundNum = searchItemsTable.setSearchCriteria(searchCriteria);
            headerLbl.setValue(String.format(headerTitle, value, foundNum));
            layout.with(searchItemsTable).expand(searchItemsTable);
        }
    }

    private static class AskCreateNewProjectWindow extends Window {
        AskCreateNewProjectWindow() {
            super("Question");
            this.setWidth("600px");
            this.setResizable(false);
            this.setModal(true);
            this.center();
            MVerticalLayout content = new MVerticalLayout();
            this.setContent(content);

            content.with(new Label("You do not have any active project. Do you want to create a new one?"));

            MHorizontalLayout btnControls = new MHorizontalLayout();
            Button skipBtn = new Button("Skip", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    close();
                }
            });
            skipBtn.setStyleName(UIConstants.BUTTON_OPTION);
            Button createNewBtn = new Button("New Project", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    close();
                    UI.getCurrent().addWindow(new ProjectAddWindow());
                }
            });
            createNewBtn.setStyleName(UIConstants.BUTTON_ACTION);
            btnControls.with(skipBtn, createNewBtn);
            content.with(btnControls).withAlign(btnControls, Alignment.MIDDLE_RIGHT);
        }
    }
}