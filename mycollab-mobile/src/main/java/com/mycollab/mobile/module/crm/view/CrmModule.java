package com.mycollab.mobile.module.crm.view;

import com.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.IModule;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.2
 */
@ViewComponent
public class CrmModule extends AbstractMobileMenuPageView implements IModule {
    private static final long serialVersionUID = 1741055981807436733L;

    public static final String TYPE = "Crm";

    public CrmModule() {
        ControllerRegistry.addController(new CrmModuleController((NavigationManager) UI.getCurrent().getContent()));
    }

    @Override
    protected void buildNavigateMenu() {

    }
}
