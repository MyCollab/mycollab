package com.mycollab.mobile.module.project.view;

import com.mycollab.mobile.ui.AbstractMobileMenuPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.IModule;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
@ViewComponent
public class ProjectModule extends AbstractMobileMenuPageView implements IModule {
    private static final long serialVersionUID = -537762284500231520L;

    public ProjectModule() {
        ControllerRegistry.addController(new ProjectModuleController((NavigationManager) UI.getCurrent().getContent()));
    }

    @Override
    protected void buildNavigateMenu() {

    }
}
