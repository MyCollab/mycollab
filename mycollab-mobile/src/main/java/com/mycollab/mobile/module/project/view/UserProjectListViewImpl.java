/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view;

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.ProjectEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class UserProjectListViewImpl extends AbstractListPageView<ProjectSearchCriteria, SimpleProject> implements UserProjectListView {
    private static final long serialVersionUID = 664947871255886622L;

    public UserProjectListViewImpl() {
        this.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST));
    }

    @Override
    protected AbstractPagedBeanList<ProjectSearchCriteria, SimpleProject> createBeanList() {
        return new ProjectListDisplay();
    }

    @Override
    protected SearchInputField<ProjectSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected void doSearch() {
        if (getPagedBeanTable().getSearchRequest() == null) {
            ProjectSearchCriteria criteria = new ProjectSearchCriteria();
            criteria.setInvolvedMember(StringSearchField.and(AppContext.getUsername()));
            criteria.setProjectStatuses(new SetSearchField(OptionI18nEnum.StatusI18nEnum.Open.name()));
            getPagedBeanTable().setSearchCriteria(criteria);
        }
        getPagedBeanTable().refresh();
    }

    @Override
    protected void buildNavigateMenu() {
        addSection("Views:");

        // Buttons with styling (slightly smaller with left-aligned text)
        MButton activityBtn = new MButton(AppContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.AllActivities(this));
        }).withIcon(FontAwesome.INBOX);
        addMenuItem(activityBtn);

        MButton prjBtn = new MButton(AppContext.getMessage(ProjectI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
        }).withIcon(FontAwesome.BUILDING);
        addMenuItem(prjBtn);

        addSection(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));

        MButton logoutBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this));
        }).withIcon(FontAwesome.SIGN_OUT);
        addMenuItem(logoutBtn);
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(AppContext.getMessage(ProjectI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoAdd(this, null))));
        menu.setContent(content);
        return menu;
    }


}
