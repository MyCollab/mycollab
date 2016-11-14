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

import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.UserNotBelongProjectException;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectViewPresenter extends ProjectGenericPresenter<ProjectView> {
    private static final long serialVersionUID = 1L;

    public ProjectViewPresenter() {
        super(ProjectView.class);
    }

    @Override
    public void onGo(HasComponents container, ScreenData<?> data) {
        ProjectModule prjContainer = (ProjectModule) container;
        prjContainer.setContent(view);
        if (data.getParams() instanceof Integer) {
            ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
            SimpleProject project = projectService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());

            if (project == null) {
                throw new ResourceNotFoundException();
            } else {
                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                boolean userBelongToProject = projectMemberService.isUserBelongToProject(UserUIContext.getUsername(), project.getId(),
                        MyCollabUI.getAccountId());
                if (userBelongToProject) {
                    CurrentProjectVariables.setProject(project);
                    view.initView(project);
                } else {
                    throw new UserNotBelongProjectException();
                }
            }
        }
    }

    @Override
    protected void onHandleChain(HasComponents container, PageActionChain pageActionChain) {
        ScreenData<?> pageAction = pageActionChain.peek();

        Class<? extends IPresenter> presenterCls = ProjectPresenterDataMapper.presenter(pageAction);
        if (presenterCls != null) {
            IPresenter<?> presenter = PresenterResolver.getPresenter(presenterCls);
            presenter.handleChain(view, pageActionChain);
        } else {
            throw new UnsupportedOperationException("Not support page action chain " + pageAction);
        }
    }
}
