package com.mycollab.module.project.view;

import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.UserNotBelongProjectException;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.service.ProjectMemberService;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
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
            SimpleProject project = projectService.findById((Integer) data.getParams(), AppUI.getAccountId());

            if (project == null) {
                throw new ResourceNotFoundException();
            } else {
                ProjectMemberService projectMemberService = AppContextUtil.getSpringBean(ProjectMemberService.class);
                boolean userBelongToProject = projectMemberService.isUserBelongToProject(UserUIContext.getUsername(), project.getId(),
                        AppUI.getAccountId());
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
