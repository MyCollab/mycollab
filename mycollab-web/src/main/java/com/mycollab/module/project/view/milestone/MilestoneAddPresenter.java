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
package com.mycollab.module.project.view.milestone;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.MilestoneEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class MilestoneAddPresenter extends AbstractPresenter<MilestoneAddView> {
    private static final long serialVersionUID = 1L;

    public MilestoneAddPresenter() {
        super(MilestoneAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleMilestone>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(SimpleMilestone milestone) {
                int milestoneId = saveMilestone(milestone);
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoRead(this, milestoneId));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(SimpleMilestone milestone) {
                saveMilestone(milestone);
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES)) {
            MilestoneContainer milestoneContainer = (MilestoneContainer) container;
            milestoneContainer.navigateToContainer(ProjectTypeConstants.MILESTONE);
            milestoneContainer.removeAllComponents();
            milestoneContainer.addComponent(view);

            SimpleMilestone milestone = (SimpleMilestone) data.getParams();
            view.editItem(milestone);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
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
        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        milestone.setProjectid(CurrentProjectVariables.getProjectId());
        milestone.setSaccountid(MyCollabUI.getAccountId());

        if (milestone.getId() == null) {
            milestone.setCreateduser(UserUIContext.getUsername());
            milestoneService.saveWithSession(milestone, UserUIContext.getUsername());
        } else {
            milestoneService.updateWithSession(milestone, UserUIContext.getUsername());
        }
        AttachmentUploadField uploadField = view.getAttachUploadField();
        String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(), milestone.getProjectid(),
                ProjectTypeConstants.MILESTONE, "" + milestone.getId());
        uploadField.saveContentsToRepo(attachPath);

        if (!SiteConfiguration.isCommunityEdition() && MilestoneStatus.Closed.name().equals(milestone.getStatus())) {
            ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.RISK,
                    ProjectTypeConstants.TASK));
            searchCriteria.setMilestoneId(NumberSearchField.equal(milestone.getId()));
            searchCriteria.setIsOpenned(new SearchField());
            ProjectTicketService genericTaskService = AppContextUtil.getSpringBean(ProjectTicketService.class);
            int openAssignmentsCount = genericTaskService.getTotalCount(searchCriteria);
            if (openAssignmentsCount > 0) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.OPT_QUESTION, MyCollabUI.getSiteName()),
                        UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_CLOSE_SUB_ASSIGNMENTS),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                genericTaskService.closeSubAssignmentOfMilestone(milestone.getId());
                            }
                        });
            }
        }
        return milestone.getId();
    }

}
