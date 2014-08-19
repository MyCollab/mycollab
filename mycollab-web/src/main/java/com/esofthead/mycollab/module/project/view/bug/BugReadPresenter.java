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
package com.esofthead.mycollab.module.project.view.bug;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BugReadPresenter extends AbstractPresenter<BugReadView> {

	private static final long serialVersionUID = 1L;

	public BugReadPresenter() {
		super(BugReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleBug>() {
					@Override
					public void onEdit(SimpleBug data) {
						EventBusFactory.getInstance().post(
								new BugEvent.GotoEdit(this, data));
					}

					@Override
					public void onAdd(SimpleBug data) {
						EventBusFactory.getInstance().post(
								new BugEvent.GotoAdd(this, null));
					}

					@Override
					public void onDelete(final SimpleBug data) {
						ConfirmDialogExt.show(
								UI.getCurrent(),
								AppContext.getMessage(
										GenericI18Enum.DIALOG_DELETE_TITLE,
										SiteConfiguration.getSiteName()),
								AppContext
										.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
								new ConfirmDialog.Listener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClose(
											final ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											final BugService bugService = ApplicationContextUtil
													.getSpringBean(BugService.class);
											bugService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											EventBusFactory.getInstance().post(
													new BugEvent.GotoList(this,
															null));
										}
									}
								});
					}

					@Override
					public void onClone(SimpleBug data) {
						SimpleBug cloneData = (SimpleBug) data.copy();
						cloneData.setId(null);
						EventBusFactory.getInstance().post(
								new BugEvent.GotoEdit(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBusFactory.getInstance().post(
								new BugEvent.GotoList(this, null));
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {

		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.BUGS)) {
			if (data.getParams() instanceof Integer) {
				BugService bugService = ApplicationContextUtil
						.getSpringBean(BugService.class);
				SimpleBug bug = bugService.findById((Integer) data.getParams(),
						AppContext.getAccountId());
				if (bug != null) {
					BugContainer bugContainer = (BugContainer) container;
					bugContainer.removeAllComponents();
					bugContainer.addComponent(view.getWidget());
					view.previewItem(bug);

					ProjectBreadcrumb breadcrumb = ViewManager
							.getCacheComponent(ProjectBreadcrumb.class);
					breadcrumb.gotoBugRead(bug);
				} else {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}
}
