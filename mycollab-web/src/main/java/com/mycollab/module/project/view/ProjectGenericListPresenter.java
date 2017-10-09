package com.mycollab.module.project.view;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.vaadin.web.ui.IListView;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public abstract class ProjectGenericListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean>
        extends ListSelectionPresenter<V, S, B> {
    private static final long serialVersionUID = 7270489652418186012L;

    public ProjectGenericListPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);
        EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null));
    }
}
