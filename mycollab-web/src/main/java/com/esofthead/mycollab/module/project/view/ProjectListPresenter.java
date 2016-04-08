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

import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.RiskSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.ViewItemAction;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.esofthead.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ProjectListPresenter extends ListSelectionPresenter<ProjectListView, ProjectSearchCriteria, SimpleProject> {

    private ProjectService projectService;

    public ProjectListPresenter() {
        super(ProjectListView.class);
        projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {
            @Override
            protected void onSelectExtra(String id) {

            }

            @Override
            protected String getReportTitle() {
                return "Projects";
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleProject.class;
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        searchCriteria = (ProjectSearchCriteria) data.getParams();
        doSearch(searchCriteria);
    }

    @Override
    public ISearchableService<ProjectSearchCriteria> getSearchService() {
        return projectService;
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleProject> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<Project> keyList = new ArrayList<>();
            for (SimpleProject item : currentDataList) {
                if (item.isSelected()) {
                    keyList.add(item);
                }
            }

            if (keyList.size() > 0) {
                projectService.massRemoveWithSession(keyList, AppContext.getUsername(), AppContext.getAccountId());
            }
        } else {
            projectService.removeByCriteria(searchCriteria, AppContext.getAccountId());
        }

        int totalCount = projectService.getTotalCount(searchCriteria);

        if (totalCount > 0) {
            doSearch(searchCriteria);
        } else {

        }
    }
}
