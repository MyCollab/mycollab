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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.vaadin.thomas.slidemenu.SlideMenu;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class ProjectListViewImpl extends AbstractListPageView<ProjectSearchCriteria, SimpleProject> implements ProjectListView {
    private static final long serialVersionUID = 664947871255886622L;

    public ProjectListViewImpl() {
        this.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST));
    }

    @Override
    protected AbstractPagedBeanList<ProjectSearchCriteria, SimpleProject> createBeanList() {
        return new ProjectListDisplay();
    }

    @Override
    protected void buildNavigateMenu() {
        Label l = new Label("Views:");
        l.addStyleName(SlideMenu.STYLENAME_SECTIONLABEL);
        getMenu().addComponent(l);

        // Buttons with styling (slightly smaller with left-aligned text)
        Button activityBtn = new Button("Activities", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getMenu().close();
            }
        });
        activityBtn.setIcon(FontAwesome.INBOX);
        activityBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(activityBtn);

        Button prjBtn = new Button("Projects", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getMenu().close();
            }
        });
        prjBtn.setIcon(FontAwesome.BUILDING);
        prjBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(prjBtn);

        l = new Label("Settings:");
        l.addStyleName(SlideMenu.STYLENAME_SECTIONLABEL);
        getMenu().addComponent(l);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setIcon(FontAwesome.SIGN_OUT);
        logoutBtn.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(logoutBtn);
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(AppContext.getMessage(ProjectCommonI18nEnum.BUTTON_NEW_PROJECT), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoAdd(this, null));
            }
        }));
        menu.setContent(content);
        return menu;
    }

}
