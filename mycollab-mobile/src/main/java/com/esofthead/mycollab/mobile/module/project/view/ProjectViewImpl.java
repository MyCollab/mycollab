package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */

@ViewComponent
public class ProjectViewImpl extends AbstractMobilePageView implements ProjectView {
    public ProjectViewImpl() {
        ((MobileNavigationManager) UI.getCurrent().getContent()).setNavigationMenu(new InsideProjectNavigationMenu());
    }
}
