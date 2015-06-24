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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
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
import org.vaadin.dialogs.ConfirmDialog;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleReadPresenter extends AbstractPresenter<ProjectRoleReadView> {
    private static final long serialVersionUID = 1L;

    private ProjectRoleService projectRoleService = ApplicationContextUtil.getSpringBean(ProjectRoleService.class);

    public ProjectRoleReadPresenter() {
        super(ProjectRoleReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(
                new DefaultPreviewFormHandler<SimpleProjectRole>() {
                    @Override
                    public void onEdit(SimpleProjectRole data) {
                        EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoEdit(this, data));
                    }

                    @Override
                    public void onAdd(SimpleProjectRole data) {
                        EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, null));
                    }

                    @Override
                    public void onDelete(final SimpleProjectRole role) {
                        if (Boolean.FALSE.equals(role.getIssystemrole())) {
                            ConfirmDialogExt.show(UI.getCurrent(),
                                    AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                                            AppContext.getSiteName()),
                                    AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                    new ConfirmDialog.Listener() {
                                        private static final long serialVersionUID = 1L;

                                        @Override
                                        public void onClose(ConfirmDialog dialog) {
                                            if (dialog.isConfirmed()) {
                                                projectRoleService.removeWithSession(role.getId(),
                                                        AppContext.getUsername(), AppContext.getAccountId());
                                                EventBusFactory.getInstance().post(
                                                        new ProjectRoleEvent.GotoList(this, null));
                                            }
                                        }
                                    });
                        } else {
                            NotificationUtil.showErrorNotification(AppContext
                                    .getMessage(ProjectMemberI18nEnum.CAN_NOT_DELETE_ROLE_MESSAGE, role.getRolename()));
                        }
                    }

                    @Override
                    public void onClone(SimpleProjectRole data) {
                        SimpleProjectRole cloneData = (SimpleProjectRole) data.copy();
                        cloneData.setRolename(null);
                        EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, cloneData));
                    }

                    @Override
                    public void onCancel() {
                        EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoList(this, null));
                    }

                    @Override
                    public void gotoNext(SimpleProjectRole data) {
                        ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                        Integer nextId = projectRoleService.getNextItemKey(criteria);
                        if (nextId != null) {
                            EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoRead(this, nextId));
                        } else {
                            NotificationUtil.showGotoLastRecordNotification();
                        }
                    }

                    @Override
                    public void gotoPrevious(SimpleProjectRole data) {
                        ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
                        Integer nextId = projectRoleService.getPreviousItemKey(criteria);
                        if (nextId != null) {
                            EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoRead(this, nextId));
                        } else {
                            NotificationUtil.showGotoFirstRecordNotification();
                        }
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        ProjectRoleContainer roleContainer = (ProjectRoleContainer) container;

        if (data.getParams() instanceof Integer) {
            SimpleProjectRole role = projectRoleService.findById((Integer) data.getParams(), AppContext.getAccountId());
            if (role == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            } else {
                roleContainer.removeAllComponents();
                roleContainer.addComponent(view.getWidget());
                view.previewItem(role);

                ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                breadCrumb.gotoRoleRead(role);
            }
        } else {
            throw new MyCollabException("Do not support screen data: " + data);
        }
    }
}
