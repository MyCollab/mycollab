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
package com.esofthead.mycollab.mobile.module.project.view.message;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.i18n.BreadcrumbI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class MessageReadPresenter extends
		AbstractMobilePresenter<MessageReadView> {

	private static final long serialVersionUID = 334720221360960772L;

	public MessageReadPresenter() {
		super(MessageReadView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.MESSAGES)) {
			if (data.getParams() instanceof Integer) {
				MessageService messageService = ApplicationContextUtil
						.getSpringBean(MessageService.class);
				SimpleMessage message = messageService.findMessageById(
						(Integer) data.getParams(), AppContext.getAccountId());
				view.previewItem(message);
				super.onGo(container, data);

				AppContext.addFragment(ProjectLinkGenerator
						.generateMessagePreviewLink(
								CurrentProjectVariables.getProjectId(),
								message.getId()),
						AppContext.getMessage(
								BreadcrumbI18nEnum.FRA_MESSAGE_READ,
								message.getTitle()));
			} else {
				throw new MyCollabException("Unhanddle this case yet");
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

}
