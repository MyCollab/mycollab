package com.mycollab.module.project.view.milestone;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.parameters.MilestoneScreenData;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestonePresenter extends AbstractPresenter<MilestoneContainer> {
    private static final long serialVersionUID = 1L;

    public MilestonePresenter() {
        super(MilestoneContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.MILESTONE);

        IPresenter presenter;
        if (data instanceof MilestoneScreenData.Search) {
            presenter = PresenterResolver.getPresenter(MilestoneListPresenter.class);
        } else if (data instanceof MilestoneScreenData.Add || data instanceof MilestoneScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(MilestoneAddPresenter.class);
        } else if (data instanceof MilestoneScreenData.Read) {
            presenter = PresenterResolver.getPresenter(MilestoneReadPresenter.class);
        } else if (data instanceof MilestoneScreenData.Roadmap) {
            presenter = PresenterResolver.getPresenter(MilestoneRoadmapPresenter.class);
        } else if (data instanceof MilestoneScreenData.Kanban) {
            presenter = PresenterResolver.getPresenter(IMilestoneKanbanPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data " + data);
        }

        presenter.go(view, data);
    }
}
