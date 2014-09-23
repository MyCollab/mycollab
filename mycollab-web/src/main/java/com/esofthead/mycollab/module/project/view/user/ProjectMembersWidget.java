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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
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
		super(
				AppContext
						.getMessage(ProjectCommonI18nEnum.WIDGET_MEMBERS_TITLE),
				new VerticalLayout());
		this.addStyleName("project-member-widget");

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

	public static class MemberRowDisplayHandler extends
			BeanList.RowDisplayHandler<SimpleProjectMember> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleProjectMember member,
				int rowIndex) {
			HorizontalLayout layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setStyleName("activity-stream");
			layout.addStyleName("odd");
			layout.setSpacing(true);
			layout.addComponent(new Image(null, UserAvatarControlFactory
					.createAvatarResource(member.getMemberAvatarId(), 48)));

			VerticalLayout content = new VerticalLayout();
			content.setStyleName("stream-content");
			Button userLink = new Button(member.getDisplayName(),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ProjectMemberEvent.GotoRead(this,
											member.getUsername()));
						}
					});
			userLink.addStyleName("link");
			userLink.addStyleName("username");
			content.addComponent(userLink);
			layout.addComponent(content);
			layout.setExpandRatio(content, 1.0f);

			CssLayout footer = new CssLayout();
			footer.setStyleName("activity-date");

			Label memberRole = new Label();
			memberRole.setContentMode(ContentMode.HTML);
			String textRole = "";
			if (member.getIsadmin() != null
					&& member.getIsadmin() == Boolean.TRUE) {
				textRole = AppContext
						.getMessage(ProjectRoleI18nEnum.OPT_ADMIN_ROLE_DISPLAY);
			} else {
				textRole = member.getRoleName();
			}
			textRole += AppContext.getMessage(
					ProjectMemberI18nEnum.OPT_MEMBER_JOIN_DATE, DateTimeUtils
							.getPrettyDateValue(member.getJoindate(),
									AppContext.getUserLocale()));
			memberRole.setValue(textRole);

			footer.addComponent(memberRole);
			content.addComponent(footer);
			return layout;
		}

	}
}
