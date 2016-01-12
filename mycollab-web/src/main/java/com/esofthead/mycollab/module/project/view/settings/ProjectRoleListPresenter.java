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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.esofthead.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.esofthead.mycollab.vaadin.events.ViewItemAction;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.web.ui.MailFormWindow;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleListPresenter extends ListSelectionPresenter<ProjectRoleListView, ProjectRoleSearchCriteria, SimpleProjectRole> {
    private static final long serialVersionUID = 1L;
    private ProjectRoleService projectRoleService;

    public ProjectRoleListPresenter() {
        super(ProjectRoleListView.class);
    }

    @Override
    protected void postInitView() {
        super.postInitView();

        projectRoleService = ApplicationContextUtil.getSpringBean(ProjectRoleService.class);

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {

            @Override
            protected void onSelectExtra(String id) {
                if (ViewItemAction.MAIL_ACTION().equals(id)) {
                    UI.getCurrent().addWindow(new MailFormWindow());
                }
            }

            @Override
            protected String getReportTitle() {
                return "Roles";
            }

            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            protected Class getReportModelClassType() {
                return SimpleProjectRole.class;
            }
        });
    }

    @Override
    protected void deleteSelectedItems() {
        if (!isSelectAll) {
            Collection<SimpleProjectRole> currentDataList = view.getPagedBeanTable().getCurrentDataList();
            List<ProjectRole> keyList = new ArrayList<>();
            for (ProjectRole item : currentDataList) {
                if (item.isSelected()) {
                    if (Boolean.TRUE.equals(item.getIssystemrole())) {
                        NotificationUtil.showErrorNotification(AppContext.
                                getMessage(ProjectMemberI18nEnum.CAN_NOT_DELETE_ROLE_MESSAGE, item.getRolename()));
                    } else {
                        keyList.add(item);
                    }
                }
            }

            if (keyList.size() > 0) {
                projectRoleService.massRemoveWithSession(keyList, AppContext.getUsername(), AppContext.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            projectRoleService.removeByCriteria(searchCriteria, AppContext.getAccountId());
            doSearch(searchCriteria);
        }

    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        ProjectRoleContainer roleContainer = (ProjectRoleContainer) container;
        roleContainer.removeAllComponents();
        roleContainer.addComponent(view.getWidget());
        searchCriteria = (ProjectRoleSearchCriteria) data.getParams();
        doSearch(searchCriteria);

        ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadCrumb.gotoRoleList();
    }

    @Override
    public ISearchableService<ProjectRoleSearchCriteria> getSearchService() {
        return ApplicationContextUtil.getSpringBean(ProjectRoleService.class);
    }
}
