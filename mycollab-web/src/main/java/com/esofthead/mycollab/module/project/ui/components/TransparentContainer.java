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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.ui.UIUtils;

/**
 * @author MyCollab Ltd
 * @since 5.0.4
 */
public class TransparentContainer extends AbstractPageView {

    public void navigateToContainer(String viewId) {
        ProjectView projectView = UIUtils.getRoot(this, ProjectView.class);
        if (projectView != null) {
            projectView.gotoSubView(viewId);
        } else {
            throw new MyCollabException("Can not find ProjectView parent");
        }
    }
}
