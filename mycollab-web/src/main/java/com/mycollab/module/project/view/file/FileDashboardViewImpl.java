package com.mycollab.module.project.view.file;

import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.file.view.ResourcesDisplayComponent;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.shared.ui.MarginInfo;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class FileDashboardViewImpl extends AbstractVerticalPageView implements FileDashboardView {
    private static final long serialVersionUID = 1L;

    @Override
    public void displayProjectFiles() {
        this.withMargin(new MarginInfo(false, true, false, true)).withFullWidth();
        int projectId = CurrentProjectVariables.getProjectId();
        String rootPath = String.format("%d/project/%d", AppUI.getAccountId(), projectId);
        addComponent(new ResourcesDisplayComponent(new Folder(rootPath)));
    }
}