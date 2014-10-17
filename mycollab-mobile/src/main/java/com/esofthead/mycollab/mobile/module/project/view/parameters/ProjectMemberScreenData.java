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
package com.esofthead.mycollab.mobile.module.project.view.parameters;

import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public class ProjectMemberScreenData {
	public static class Search extends ScreenData<ProjectMemberSearchCriteria> {

		public Search(ProjectMemberSearchCriteria params) {
			super(params);
		}
	}

	public static class Add extends ScreenData<ProjectMember> {

		public Add(ProjectMember params) {
			super(params);
		}
	}

	public static class InviteProjectMembers extends ScreenData<Object> {

		public InviteProjectMembers() {
			super(null);
		}
	}

	public static class Read extends ScreenData<Object> {

		public Read(Object params) {
			super(params);
		}
	}

	public static class Edit extends ScreenData<SimpleProjectMember> {

		public Edit(SimpleProjectMember params) {
			super(params);
		}
	}
}
