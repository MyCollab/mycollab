package com.mycollab.mobile.module.crm.view;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class AbstractCrmPresenter<V extends PageView> extends AbstractPresenter<V> {
    public AbstractCrmPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        if (navigator instanceof NavigationManager) {
            NavigationManager navManager = ((NavigationManager) navigator);
            navManager.navigateTo(getView());
            if (navManager.getPreviousComponent() == null) {
                AllActivitiesPresenter activitiesPresenter = PresenterResolver.getPresenter(AllActivitiesPresenter.class);
                navManager.setPreviousComponent(activitiesPresenter.getView());
            }
        } else {
            throw new MyCollabException("Invalid flow");
        }
    }
}
