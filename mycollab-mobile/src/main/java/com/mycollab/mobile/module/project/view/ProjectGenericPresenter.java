package com.mycollab.mobile.module.project.view;

import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.project.event.ProjectEvent;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
public abstract class ProjectGenericPresenter<V extends PageView> extends AbstractPresenter<V> {
    public ProjectGenericPresenter(Class<V> view) {
        super(view);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);
        if (this instanceof ProjectViewPresenter) {
            EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{"dashboard"}));
        } else {
            EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null));
        }
    }
}
