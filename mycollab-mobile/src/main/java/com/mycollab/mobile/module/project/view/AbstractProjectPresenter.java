package com.mycollab.mobile.module.project.view;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public abstract class AbstractProjectPresenter<V extends PageView> extends AbstractPresenter<V> {
    public AbstractProjectPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        if (navigator instanceof NavigationManager) {
            NavigationManager navManager = ((NavigationManager) navigator);
            navManager.navigateTo(getView());
            if (navManager.getPreviousComponent() == null) {
                UserProjectListPresenter projectListPresenter = PresenterResolver.getPresenter(UserProjectListPresenter.class);
                navManager.setPreviousComponent(projectListPresenter.getView());
            }
        } else {
            throw new MyCollabException("Invalid flow");
        }
    }
}
