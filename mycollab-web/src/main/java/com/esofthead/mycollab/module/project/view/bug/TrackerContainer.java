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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.TabSheetDecorator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TrackerContainer extends AbstractPageView {
    private static final long serialVersionUID = 1L;

    private BugDashboardPresenter dashboardPresenter;
    private BugPresenter bugPresenter;
    private ComponentPresenter componentPresenter;
    private VersionPresenter versionPresenter;
    private final TabSheetDecorator myProjectTab;

    private String selectedTabId = "";

    public TrackerContainer() {
        this.myProjectTab = new TabSheetDecorator();
        this.myProjectTab.setStyleName(UIConstants.THEME_TAB_STYLE3);
        this.addComponent(myProjectTab);
        this.setWidth("100%");
        this.buildComponents();
    }

    private void buildComponents() {
        dashboardPresenter = PresenterResolver.getPresenter(BugDashboardPresenter.class);

        this.myProjectTab.addTab(this.dashboardPresenter.getView(), AppContext.getMessage(BugI18nEnum.TAB_DASHBOARD));

        bugPresenter = PresenterResolver.getPresenter(BugPresenter.class);
        this.myProjectTab.addTab(bugPresenter.getView(), AppContext.getMessage(BugI18nEnum.TAB_BUG));

        componentPresenter = PresenterResolver.getPresenter(ComponentPresenter.class);
        this.myProjectTab.addTab(componentPresenter.getView(), AppContext.getMessage(BugI18nEnum.TAB_COMPONENT));

        versionPresenter = PresenterResolver.getPresenter(VersionPresenter.class);
        this.myProjectTab.addTab(versionPresenter.getView(), AppContext.getMessage(BugI18nEnum.TAB_VERSION));

        this.myProjectTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                Tab tab = ((TabSheetDecorator) event.getTabSheet()).getSelectedTabInfo();
                String caption = tab.getCaption();
                SimpleProject project = CurrentProjectVariables.getProject();

                if (AppContext.getMessage(BugI18nEnum.TAB_DASHBOARD).equals(caption)
                        && !AppContext.getMessage(BugI18nEnum.TAB_DASHBOARD).equals(selectedTabId)) {
                    dashboardPresenter.go(TrackerContainer.this, null);
                } else if (AppContext.getMessage(BugI18nEnum.TAB_BUG).equals(caption) &&
                        !AppContext.getMessage(BugI18nEnum.TAB_BUG).equals(selectedTabId)) {
                    BugSearchCriteria criteria = new BugSearchCriteria();
                    criteria.setProjectId(new NumberSearchField(project.getId()));
                    bugPresenter.go(TrackerContainer.this, new BugScreenData.Search(criteria));
                } else if (AppContext.getMessage(BugI18nEnum.TAB_COMPONENT).equals(caption)
                        && !AppContext.getMessage(BugI18nEnum.TAB_COMPONENT).equals(selectedTabId)) {
                    componentPresenter.go(TrackerContainer.this, null);
                } else if (AppContext.getMessage(BugI18nEnum.TAB_VERSION).equals(caption)
                        && !AppContext.getMessage(BugI18nEnum.TAB_VERSION).equals(selectedTabId)) {
                    versionPresenter.go(TrackerContainer.this, null);
                }
                selectedTabId = "";
            }
        });
    }

    public Component gotoSubView(String name) {
        selectedTabId = name;
        return myProjectTab.selectTab(name).getComponent();
    }
}
