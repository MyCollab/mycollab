package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.module.project.view.parameters.MilestoneScreenData;
import com.mycollab.mobile.mvp.view.PresenterOptionUtil;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestonePresenter extends AbstractProjectPresenter<MilestoneContainer> {
    private static final long serialVersionUID = 5263058263047835714L;

    public MilestonePresenter() {
        super(MilestoneContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        IPresenter<?> presenter;

        if (data instanceof MilestoneScreenData.Search) {
            presenter = PresenterResolver.getPresenter(MilestoneListPresenter.class);
        } else if (data instanceof MilestoneScreenData.Add || data instanceof MilestoneScreenData.Edit) {
            presenter = PresenterOptionUtil.getPresenter(IMilestoneAddPresenter.class);
        } else if (data instanceof MilestoneScreenData.Read) {
            presenter = PresenterResolver.getPresenter(MilestoneReadPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data " + data);
        }

        presenter.go(container, data);
    }

}
