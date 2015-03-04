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

package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectViewPresenter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class MilestoneAddPresenter extends AbstractPresenter<MilestoneAddView> {

    private static final long serialVersionUID = 1L;

    public MilestoneAddPresenter() {
        super(MilestoneAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(
                new EditFormHandler<Milestone>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onSave(final Milestone milestone) {
                        int milestoneId = saveMilestone(milestone);
                        EventBusFactory.getInstance().post(new MilestoneEvent.GotoRead(this, milestoneId));
                    }

                    @Override
                    public void onCancel() {
                        ViewState viewState = HistoryViewManager.back();
                        if (viewState.hasPresenters(NullViewState.EmptyPresenter.class, ProjectViewPresenter.class)) {
                            EventBusFactory.getInstance().post(
                                    new MilestoneEvent.GotoList(this, null));
                        }
                    }

                    @Override
                    public void onSaveAndNew(final Milestone milestone) {
                        saveMilestone(milestone);
                        EventBusFactory.getInstance().post(
                                new MilestoneEvent.GotoAdd(this, null));
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.MILESTONES)) {
            MilestoneContainer milestoneContainer = (MilestoneContainer) container;
            milestoneContainer.removeAllComponents();
            milestoneContainer.addComponent(view.getWidget());

            Milestone milestone = (Milestone) data.getParams();
            view.editItem(milestone);

            ProjectBreadcrumb breadcrumb = ViewManager
                    .getCacheComponent(ProjectBreadcrumb.class);
            if (milestone.getId() == null) {
                breadcrumb.gotoMilestoneAdd();
            } else {
                breadcrumb.gotoMilestoneEdit(milestone);
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private int saveMilestone(Milestone milestone) {
        MilestoneService milestoneService = ApplicationContextUtil
                .getSpringBean(MilestoneService.class);
        milestone.setProjectid(CurrentProjectVariables.getProjectId());
        milestone.setSaccountid(AppContext.getAccountId());

        if (milestone.getId() == null) {
            milestone.setCreateduser(AppContext.getUsername());
            milestoneService.saveWithSession(milestone,
                    AppContext.getUsername());
        } else {
            milestoneService.updateWithSession(milestone,
                    AppContext.getUsername());
        }
        return milestone.getId();
    }

}
