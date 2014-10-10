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
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.mobile.module.project.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class ProjectMemberListViewImpl extends
		AbstractListViewComp<ProjectMemberSearchCriteria, SimpleProjectMember>
		implements ProjectMemberListView {

	private static final long serialVersionUID = 3008732621100597514L;

	@Override
	protected AbstractPagedBeanList<ProjectMemberSearchCriteria, SimpleProjectMember> createBeanTable() {
		return new ProjectMemberListDisplay();
	}

	@Override
	protected Component createRightComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
