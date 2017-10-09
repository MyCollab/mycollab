package com.mycollab.mobile.shell.view;

import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MainViewPresenter extends AbstractPresenter<MainView> {
    private static final long serialVersionUID = 7699660189568510585L;

    public MainViewPresenter() {
        super(MainView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        // if user type remember URL, instead of going to main page, to to his url
        String url = ((MobileApplication) UI.getCurrent()).getCurrentFragmentUrl();
        if (!StringUtils.isBlank(url)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            MobileApplication.rootUrlResolver.navigateByFragement(url);
        } else {
            EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
        }
    }
}
