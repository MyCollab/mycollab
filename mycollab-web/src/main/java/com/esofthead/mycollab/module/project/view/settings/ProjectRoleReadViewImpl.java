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

package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectRoleReadViewImpl extends AbstractPageView implements
ProjectRoleReadView {

	private static final long serialVersionUID = 1L;

	private ProjectRoleReadComp roleReadComp;

	public ProjectRoleReadViewImpl() {
		super();
		this.roleReadComp = new ProjectRoleReadComp();
		this.addComponent(this.roleReadComp);
	}

	@Override
	public void previewItem(final SimpleProjectRole role) {
		roleReadComp.previewItem(role);
	}

	@Override
	public HasPreviewFormHandlers<SimpleProjectRole> getPreviewFormHandlers() {
		return this.roleReadComp.getPreviewForm();
	}

	@Override
	public SimpleProjectRole getItem() {
		return this.roleReadComp.getBeanItem();
	}
}
