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

package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMembersWidget extends Depot {
	private static final long serialVersionUID = 1L;

	private BeanList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember> memberList;

	public ProjectMembersWidget() {
		super("Members", new VerticalLayout());

		memberList = new BeanList<ProjectMemberService, ProjectMemberSearchCriteria, SimpleProjectMember>(
				ApplicationContextUtil
						.getSpringBean(ProjectMemberService.class),
				MemberRowDisplayHandler.class);
		this.addStyleName("activity-panel");
		((VerticalLayout) this.bodyContent).setMargin(false);
	}

	public void showInformation() {
		this.bodyContent.removeAllComponents();
		this.bodyContent.addComponent(memberList);
		ProjectMemberSearchCriteria searchCriteria = new ProjectMemberSearchCriteria();
		searchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		searchCriteria.setStatus(new StringSearchField(
				ProjectMemberStatusConstants.ACTIVE));
		memberList.setSearchCriteria(searchCriteria);
	}

	public static class MemberRowDisplayHandler implements
			BeanList.RowDisplayHandler<SimpleProjectMember> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(SimpleProjectMember member, int rowIndex) {
			CssLayout layout = new CssLayout();
			layout.setWidth("100%");
			layout.setStyleName("activity-stream");

			CssLayout header = new CssLayout();
			header.setStyleName("stream-content");
			header.addComponent(new ProjectUserLink(member.getUsername(),
					member.getMemberAvatarId(), member.getDisplayName(), false,
					true));
			layout.addComponent(header);

			CssLayout body = new CssLayout();
			body.setStyleName("activity-date");

			Label memberRole = new Label();
			memberRole.setContentMode(ContentMode.HTML);
			String textRole = "";
			if (member.getIsadmin() != null
					&& member.getIsadmin() == Boolean.TRUE) {
				textRole = "<a style=\"color: #b00000;\"> Project Admin </a>";
			} else {
				textRole = member.getRoleName();
			}
			textRole += " - Joined from "
					+ DateTimeUtils.getStringDateFromNow(member.getJoindate());
			memberRole.setValue(textRole);

			body.addComponent(memberRole);
			layout.addComponent(body);
			return layout;
		}

	}
}
