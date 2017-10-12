/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.file;

import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.file.view.ResourcesDisplayComponent;
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
    public void displayFiles() {
        this.withMargin(new MarginInfo(false, true, false, true)).withFullWidth();
        String rootPath = String.format("%d/.crm", AppUI.getAccountId());
        addComponent(new ResourcesDisplayComponent(new Folder(rootPath)));
    }
}
