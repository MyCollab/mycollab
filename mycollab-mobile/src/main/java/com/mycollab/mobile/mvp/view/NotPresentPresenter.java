package com.mycollab.mobile.mvp.view;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.UserProjectListPresenter;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
public class NotPresentPresenter extends AbstractPresenter<NotPresenterView> {
    public NotPresentPresenter() {
        super(NotPresenterView.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        if (navigator instanceof NavigationManager) {
            NavigationManager navManager = ((NavigationManager) navigator);
            navManager.navigateTo(getView());
            getView().display();
            if (navManager.getPreviousComponent() == null) {
                UserProjectListPresenter projectListPresenter = PresenterResolver.getPresenter(UserProjectListPresenter.class);
                navManager.setPreviousComponent(projectListPresenter.getView());
            }
        } else {
            throw new MyCollabException("Invalid flow");
        }
    }
}
