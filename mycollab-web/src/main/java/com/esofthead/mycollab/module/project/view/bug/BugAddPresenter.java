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

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.ui.form.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectViewPresenter;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugRelatedItemService;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class BugAddPresenter extends AbstractPresenter<BugAddView> {
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
                    public void onSave(SimpleBug bug) {
                        int bugId = saveBug(bug);
                        EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, bugId));
                    }

                    @Override
                    public void onCancel() {
                        ViewState viewState = HistoryViewManager.back();
                        if (viewState.hasPresenters(NullViewState.EmptyPresenter.class, ProjectViewPresenter.class)) {
                            EventBusFactory.getInstance().post(
                                    new BugEvent.GotoDashboard(this, null));
                        }
                    }

                    @Override
                    public void onSaveAndNew(SimpleBug bug) {
                        saveBug(bug);
                        EventBusFactory.getInstance().post(
                                new BugEvent.GotoAdd(this, null));
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
            BugContainer bugContainer = (BugContainer) container;
            bugContainer.removeAllComponents();
            bugContainer.addComponent(view.getWidget());

            SimpleBug bug = (SimpleBug) data.getParams();
            view.editItem(bug);

            ProjectBreadcrumb breadcrumb = ViewManager
                    .getCacheComponent(ProjectBreadcrumb.class);
            if (bug.getId() == null) {
                breadcrumb.gotoBugAdd();
            } else {
                breadcrumb.gotoBugEdit(bug);
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private int saveBug(SimpleBug bug) {
        BugService bugService = ApplicationContextUtil
                .getSpringBean(BugService.class);
        bug.setProjectid(CurrentProjectVariables.getProjectId());
        bug.setSaccountid(AppContext.getAccountId());
        if (bug.getId() == null) {
            bug.setStatus(BugStatus.Open.name());
            bug.setResolution(BugResolution.Newissue.name());
            bug.setLogby(AppContext.getUsername());
            bug.setSaccountid(AppContext.getAccountId());
            int bugId = bugService.saveWithSession(bug,
                    AppContext.getUsername());
            ProjectFormAttachmentUploadField uploadField = view
                    .getAttachUploadField();
            uploadField.saveContentsToRepo(bug.getProjectid(),
                    ProjectTypeConstants.BUG, bugId);

            // save component
            BugRelatedItemService bugRelatedItemService = ApplicationContextUtil
                    .getSpringBean(BugRelatedItemService.class);
            bugRelatedItemService.saveAffectedVersionsOfBug(bugId,
                    view.getAffectedVersions());
            bugRelatedItemService.saveFixedVersionsOfBug(bugId,
                    view.getFixedVersion());
            bugRelatedItemService.saveComponentsOfBug(bugId,
                    view.getComponents());
            CacheUtils.cleanCache(AppContext.getAccountId(),
                    BugService.class.getName());

            List<String> followers = view.getFollowers();
            if (followers.size() > 0) {
                List<MonitorItem> monitorItems = new ArrayList<>();
                for (String follower : followers) {
                    MonitorItem monitorItem = new MonitorItem();
                    monitorItem.setMonitorDate(new GregorianCalendar().getTime());
                    monitorItem.setSaccountid(AppContext.getAccountId());
                    monitorItem.setType(ProjectTypeConstants.BUG);
                    monitorItem.setTypeid(bugId);
                    monitorItem.setUser(follower);
                    monitorItem.setExtratypeid(CurrentProjectVariables.getProjectId());
                    monitorItems.add(monitorItem);
                }
                MonitorItemService monitorItemService = ApplicationContextUtil.getSpringBean(MonitorItemService.class);
                monitorItemService.saveMonitorItems(monitorItems);
            }
        } else {
            bugService.updateWithSession(bug, AppContext.getUsername());
            ProjectFormAttachmentUploadField uploadField = view
                    .getAttachUploadField();
            uploadField.saveContentsToRepo(bug.getProjectid(),
                    ProjectTypeConstants.BUG, bug.getId());

            int bugId = bug.getId();
            BugRelatedItemService bugRelatedItemService = ApplicationContextUtil
                    .getSpringBean(BugRelatedItemService.class);
            bugRelatedItemService.updateAfftedVersionsOfBug(bugId,
                    view.getAffectedVersions());
            bugRelatedItemService.updateFixedVersionsOfBug(bugId,
                    view.getFixedVersion());
            bugRelatedItemService.updateComponentsOfBug(bugId,
                    view.getComponents());
            CacheUtils.cleanCache(AppContext.getAccountId(),
                    BugService.class.getName());
        }

        return bug.getId();
    }
}