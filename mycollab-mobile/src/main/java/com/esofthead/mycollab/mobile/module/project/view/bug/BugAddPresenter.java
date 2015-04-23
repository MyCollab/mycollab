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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 * 
 */

/*
 * TODO: Add support for Attachments, Components, Versions when they're ready
 */
public class BugAddPresenter extends AbstractMobilePresenter<BugAddView> {

	private static final long serialVersionUID = 1L;

	public BugAddPresenter() {
		super(BugAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleBug>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSave(final SimpleBug bug) {
						saveBug(bug);
						EventBusFactory.getInstance().post(
								new ShellEvent.NavigateBack(this, null));
					}

					@Override
					public void onCancel() {
					}

					@Override
					public void onSaveAndNew(final SimpleBug bug) {
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS)) {
			InsideProjectNavigationMenu projectModuleMenu = (InsideProjectNavigationMenu) ((MobileNavigationManager) UI
					.getCurrent().getContent()).getNavigationMenu();
			projectModuleMenu.selectButton(AppContext
					.getMessage(ProjectCommonI18nEnum.VIEW_BUG));
			super.onGo(container, data);
			SimpleBug bug = (SimpleBug) data.getParams();
			view.editItem(bug);

			if (bug.getId() == null) {
				AppContext
						.addFragment(
								"project/bug/add/"
										+ GenericLinkUtils
												.encodeParam(CurrentProjectVariables
														.getProjectId()),
								AppContext
										.getMessage(BugI18nEnum.FORM_NEW_BUG_TITLE));
			} else {
				AppContext.addFragment(
						ProjectLinkGenerator.generateBugEditLink(
								bug.getBugkey(), bug.getProjectShortName()),
						AppContext.getMessage(BugI18nEnum.FORM_EDIT_BUG_TITLE));
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	private void saveBug(SimpleBug bug) {
		BugService bugService = ApplicationContextUtil
				.getSpringBean(BugService.class);
		bug.setProjectid(CurrentProjectVariables.getProjectId());
		bug.setSaccountid(AppContext.getAccountId());
		ProjectFormAttachmentUploadField uploadField = view
				.getAttachUploadField();
		if (bug.getId() == null) {
			bug.setStatus(BugStatus.Open.name());
			bug.setResolution(BugResolution.Newissue.name());
			bug.setLogby(AppContext.getUsername());
			bug.setSaccountid(AppContext.getAccountId());
			int bugId = bugService.saveWithSession(bug,
					AppContext.getUsername());
			uploadField.saveContentsToRepo(
					CurrentProjectVariables.getProjectId(),
					ProjectTypeConstants.BUG, bugId);
		} else {
			bugService.updateWithSession(bug, AppContext.getUsername());
			uploadField.saveContentsToRepo();
		}

		CacheUtils.cleanCache(AppContext.getAccountId(),
				BugService.class.getName());

	}
}