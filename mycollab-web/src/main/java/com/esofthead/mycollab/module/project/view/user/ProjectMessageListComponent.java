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

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectMessageListComponent extends Depot {
	public static class MessageRowDisplayHandler implements
			DefaultBeanPagedList.RowDisplayHandler<SimpleMessage> {

		@Override
		public Component generateRow(final SimpleMessage message,
				final int rowIndex) {
			final CssLayout layout = new CssLayout();
			layout.setWidth("100%");
			layout.setStyleName("activity-stream");

			if ((rowIndex + 1) % 2 != 0) {
				layout.addStyleName("odd");
			}

			final CssLayout header = new CssLayout();
			header.setStyleName("stream-content");

			final String content = AppContext.getMessage(
					ProjectCommonI18nEnum.FEED_PROJECT_MESSAGE_TITLE,
					SiteConfiguration.getAvatarLink(
							message.getPostedUserAvatarId(), 16),
					ProjectLinkBuilder.generateProjectMemberFullLink(
							message.getProjectid(), message.getPosteduser()),
					message.getFullPostedUserName(), MyCollabResource
							.newResourceLink("icons/16/project/message.png"),
					ProjectLinkBuilder.generateMessagePreviewFullLink(
							message.getProjectid(), message.getId(),
							GenericLinkUtils.URL_PREFIX_PARAM), message
							.getTitle());
			final Label actionLbl = new Label(content, ContentMode.HTML);

			header.addComponent(actionLbl);

			layout.addComponent(header);

			final CssLayout body = new CssLayout();
			body.setStyleName("activity-date");
			final Label dateLbl = new Label(AppContext.getMessage(
					MessageI18nEnum.OPT_FROM_TIME_ENTRY, DateTimeUtils
							.getStringDateFromNow(message.getPosteddate(),
									AppContext.getUserLocale())));
			body.addComponent(dateLbl);

			layout.addComponent(body);
			return layout;
		}
	}

	private static final long serialVersionUID = 1L;

	private final DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage> messageList;

	public ProjectMessageListComponent() {
		super(AppContext.getMessage(MessageI18nEnum.WIDGET_LASTEST_NEWS),
				new VerticalLayout());

		messageList = new DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage>(
				ApplicationContextUtil.getSpringBean(MessageService.class),
				new MessageRowDisplayHandler(), 5);
		addStyleName("activity-panel");
		((VerticalLayout) bodyContent).setMargin(false);
	}

	public void showLatestMessages() {
		bodyContent.removeAllComponents();
		bodyContent.addComponent(messageList);
		final MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
		searchCriteria.setProjectids(new SetSearchField<Integer>(
				CurrentProjectVariables.getProjectId()));

		messageList.setSearchCriteria(searchCriteria);
	}
}
