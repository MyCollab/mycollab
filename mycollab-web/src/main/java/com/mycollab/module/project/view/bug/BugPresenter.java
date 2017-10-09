package com.mycollab.module.project.view.bug;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.parameters.BugScreenData;
import com.mycollab.module.project.view.ticket.TicketContainer;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugPresenter extends AbstractPresenter<BugContainer> {
    private static final long serialVersionUID = 1L;

    public BugPresenter() {
        super(BugContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        TicketContainer ticketContainer = (TicketContainer) projectViewContainer.gotoSubView(ProjectTypeConstants.TICKET);
        ticketContainer.setContent(view);
        view.removeAllComponents();

        AbstractPresenter<?> presenter;

        if (data instanceof BugScreenData.Add || data instanceof BugScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(BugAddPresenter.class);
        } else if (data instanceof BugScreenData.Read) {
            presenter = PresenterResolver.getPresenter(BugReadPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data");
        }

        presenter.go(view, data);
    }

}
