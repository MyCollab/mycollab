package com.mycollab.module.project.view;

import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class SettingPresenter extends AbstractPresenter<SettingView> {
    public SettingPresenter() {
        super(SettingView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        view.display();
    }
}
