package com.mycollab.module.project.view;

import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FollowingTicketPresenter extends AbstractPresenter<FollowingTicketView> {
    private static final long serialVersionUID = 1L;

    public FollowingTicketPresenter() {
        super(FollowingTicketView.class);
    }

    @Override
    protected void viewAttached() {
        view.getSearchHandlers().addSearchHandler(criteria -> view.getPagedBeanTable().setSearchCriteria(criteria));
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ((ComponentContainer) container).addComponent(view);
        view.displayTickets();
    }
}
