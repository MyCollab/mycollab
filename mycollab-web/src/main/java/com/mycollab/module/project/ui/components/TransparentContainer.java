package com.mycollab.module.project.ui.components;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.vaadin.mvp.AbstractSingleContainerPageView;
import com.mycollab.vaadin.ui.UIUtils;

/**
 * @author MyCollab Ltd
 * @since 5.0.4
 */
public class TransparentContainer extends AbstractSingleContainerPageView {

    public void navigateToContainer(String viewId) {
        ProjectView projectView = UIUtils.getRoot(this, ProjectView.class);
        if (projectView != null) {
            projectView.gotoSubView(viewId);
        } else {
            throw new MyCollabException("Can not find ProjectView parent");
        }
    }
}
