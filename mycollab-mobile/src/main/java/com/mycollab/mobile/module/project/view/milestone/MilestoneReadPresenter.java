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
package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.common.GenericLinkUtils;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.MilestoneEvent;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestoneReadPresenter extends AbstractProjectPresenter<MilestoneReadView> {
    private static final long serialVersionUID = 1L;

    public MilestoneReadPresenter() {
        super(MilestoneReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleMilestone>() {

            @Override
            public void onAdd(SimpleMilestone data) {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleMilestone data) {
                ConfirmDialog.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        dialog -> {
                            if (dialog.isConfirmed()) {
                                MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
                                milestoneService.removeWithSession(data, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null));
                            }
                        });
            }

            @Override
            public void onClone(SimpleMilestone data) {
                SimpleMilestone cloneData = (SimpleMilestone) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoEdit(this, cloneData));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MILESTONES)) {
            if (data.getParams() instanceof Integer) {
                MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
                SimpleMilestone milestone = milestoneService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (milestone != null) {
                    view.previewItem(milestone);
                    super.onGo(container, data);

                    MyCollabUI.addFragment("project/milestone/preview/" + GenericLinkUtils.encodeParam(CurrentProjectVariables.getProjectId(), milestone.getId()),
                            milestone.getName());
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            } else {
                throw new MyCollabException("Unhanddle this case yet");
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
