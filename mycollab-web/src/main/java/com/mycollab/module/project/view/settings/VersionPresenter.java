package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.view.parameters.VersionScreenData;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class VersionPresenter extends AbstractPresenter<VersionContainer> {
    private static final long serialVersionUID = 1L;

    public VersionPresenter() {
        super(VersionContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        view.removeAllComponents();
        AbstractPresenter<?> presenter;

        if (data instanceof VersionScreenData.Add) {
            presenter = PresenterResolver.getPresenter(VersionAddPresenter.class);
        } else if (data instanceof VersionScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(VersionAddPresenter.class);
        } else if (data instanceof VersionScreenData.Search) {
            presenter = PresenterResolver.getPresenter(VersionListPresenter.class);
        } else if (data instanceof VersionScreenData.Read) {
            presenter = PresenterResolver.getPresenter(VersionReadPresenter.class);
        } else if (data == null) {
            VersionSearchCriteria criteria = new VersionSearchCriteria();
            criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            data = new VersionScreenData.Search(criteria);
            presenter = PresenterResolver.getPresenter(VersionListPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data");
        }

        presenter.go(view, data);

    }

}
