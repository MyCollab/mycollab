package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.ViewEvent;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.vaadin.thomas.slidemenu.SlideMenu;
import org.vaadin.thomas.slidemenu.SlideMenuView;

import java.io.Serializable;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class AbstractMobileMenuPageView extends SlideMenuView implements PageView, Serializable {
    private static final long serialVersionUID = 1L;


    public AbstractMobileMenuPageView() {
        super();
        if (this.getLeftComponent() != null && this.getLeftComponent() instanceof NavigationButton) {
            this.getLeftComponent().setCaption(UserUIContext.getMessage(GenericI18Enum.M_BUTTON_BACK));
        }

        buildNavigateMenu();
    }

    public void closeMenu() {
        getMenu().close();
    }

    public void addSection(String title) {
        getMenu().addComponent(new ELabel(title).withStyleName(SlideMenu.STYLENAME_SECTIONLABEL));
    }

    public void addMenuItem(Component comp) {
        comp.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(comp);
    }

    protected abstract void buildNavigateMenu();

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