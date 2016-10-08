/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.file;

import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.file.view.ResourcesDisplayComponent;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.shared.ui.MarginInfo;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class FileDashboardViewImpl extends AbstractPageView implements FileDashboardView {
    private static final long serialVersionUID = 1L;

    @Override
    public void displayProjectFiles() {
        this.withMargin(new MarginInfo(false, true, false, true)).withFullWidth();
        int projectId = CurrentProjectVariables.getProjectId();
        String rootPath = String.format("%d/project/%d", MyCollabUI.getAccountId(), projectId);
        addComponent(new ResourcesDisplayComponent(new Folder(rootPath)));
    }
}