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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */
public class MessageAddPresenter extends
		AbstractMobilePresenter<MessageAddView> {

	private static final long serialVersionUID = -6518878184021039341L;

	public MessageAddPresenter() {
		super(MessageAddView.class);
	}

	@Override
	protected void postInitView() {
		super.postInitView();
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleMessage>() {

					private static final long serialVersionUID = 2381946253040633727L;

					@Override
					public void onSaveAndNew(SimpleMessage bean) {
						// Do nothing
					}

					@Override
					public void onSave(SimpleMessage bean) {
						MessageService messageService = ApplicationContextUtil
								.getSpringBean(MessageService.class);
						messageService.saveWithSession(bean,
								AppContext.getUsername());
						ProjectFormAttachmentUploadField uploadField = view
								.getUploadField();
						uploadField.saveContentsToRepo(
								CurrentProjectVariables.getProjectId(),
								AttachmentType.PROJECT_MESSAGE, bean.getId());
						EventBusFactory.getInstance().post(
								new ShellEvent.NavigateBack(this, null));
					}

					@Override
					public void onCancel() {
						// Do nothing
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.MESSAGES)) {
			InsideProjectNavigationMenu projectModuleMenu = (InsideProjectNavigationMenu) ((MobileNavigationManager) UI
					.getCurrent().getContent()).getNavigationMenu();
			projectModuleMenu.selectButton(AppContext
					.getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE));

			super.onGo(navigator, data);
			AppContext.addFragment(ProjectLinkGenerator
					.generateMessageAddLink(CurrentProjectVariables
							.getProjectId()), AppContext
					.getMessage(MessageI18nEnum.M_VIEW_ADD_TITLE));
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
		super.onGo(navigator, data);
	}

}
