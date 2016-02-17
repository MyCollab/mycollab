/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ViewEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
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
            this.getLeftComponent().setCaption(AppContext.getMessage(GenericI18Enum.M_BUTTON_BACK));
        }

        buildNavigateMenu();
    }

    public void closeMenu() {
        getMenu().close();
    }

    public void addSection(String title) {
        Label l = new Label(title);
        l.addStyleName(SlideMenu.STYLENAME_SECTIONLABEL);
        getMenu().addComponent(l);
    }

    public void addMenuItem(Component comp) {
        comp.addStyleName(SlideMenu.STYLENAME_BUTTON);
        getMenu().addComponent(comp);
    }

    protected abstract void buildNavigateMenu();

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    @Override
    public void addViewListener(ViewListener listener) {
        addListener(ViewEvent.VIEW_IDENTIFIER(), ViewEvent.class, listener, ViewListener.viewInitMethod);
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