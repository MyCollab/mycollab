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
package com.mycollab.module.project.view.settings;

import com.mycollab.core.SecureAccessException;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.ProjectRole;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.ViewItemAction;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.mycollab.vaadin.web.ui.MailFormWindow;
import com.vaadin.ui.HasComponents;
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
        projectRoleService = AppContextUtil.getSpringBean(ProjectRoleService.class);

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {

            @Override
            protected void onSelectExtra(String id) {
                if (ViewItemAction.MAIL_ACTION.equals(id)) {
                    UI.getCurrent().addWindow(new MailFormWindow());
                }
            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(ProjectRoleI18nEnum.LIST);
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
                        NotificationUtil.showErrorNotification(UserUIContext.
                                getMessage(ProjectMemberI18nEnum.CAN_NOT_DELETE_ROLE_MESSAGE, item.getRolename()));
                    } else {
                        keyList.add(item);
                    }
                }
            }

            if (keyList.size() > 0) {
                projectRoleService.massRemoveWithSession(keyList, UserUIContext.getUsername(), AppUI.getAccountId());
                doSearch(searchCriteria);
                checkWhetherEnableTableActionControl();
            }
        } else {
            projectRoleService.removeByCriteria(searchCriteria, AppUI.getAccountId());
            doSearch(searchCriteria);
        }

    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.ROLES)) {
            ProjectRoleContainer roleContainer = (ProjectRoleContainer) container;
            roleContainer.removeAllComponents();
            roleContainer.addComponent(view);
            searchCriteria = (ProjectRoleSearchCriteria) data.getParams();
            doSearch(searchCriteria);

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoRoleList();
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    public ISearchableService<ProjectRoleSearchCriteria> getSearchService() {
        return AppContextUtil.getSpringBean(ProjectRoleService.class);
    }
}
