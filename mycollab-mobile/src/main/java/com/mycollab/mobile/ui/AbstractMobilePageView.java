package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.event.ViewEvent;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public abstract class AbstractMobilePageView extends NavigationView implements PageView {

    public AbstractMobilePageView() {
        if (this.getLeftComponent() != null && this.getLeftComponent() instanceof NavigationButton) {
            this.getLeftComponent().setCaption(getBackTitle());
        }

        if (this.getLeftComponent() == null) {
            this.setLeftComponent(new ELabel("").withWidth("72px"));
        }
    }

    private String getBackTitle() {
        return UserUIContext.getMessage(GenericI18Enum.M_BUTTON_BACK);
    }

    @Override
    public void addViewListener(ViewListener listener) {
        addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent.class, listener, ViewListener.Companion.getViewInitMethod());
    }

    @Override
    public NavigationManager getNavigationManager() {
        UI ui = UI.getCurrent();
        if (ui instanceof MobileApplication) {
            return (NavigationManager) (ui.getContent());
        }
        return null;
    }
}
