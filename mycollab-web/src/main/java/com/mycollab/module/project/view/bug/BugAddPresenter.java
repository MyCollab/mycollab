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
package com.mycollab.module.project.view.bug;

import com.google.common.eventbus.AsyncEventBus;
import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.core.SecureAccessException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugRelatedItemService;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.ui.ComponentContainer;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class BugAddPresenter extends ProjectGenericPresenter<BugAddView> {
    private static final long serialVersionUID = 1L;

    public BugAddPresenter() {
        super(BugAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleBug>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(SimpleBug bug) {
                int bugId = saveBug(bug);
                EventBusFactory.getInstance().post(new BugEvent.GotoRead(this, bugId));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null));
            }

            @Override
            public void onSaveAndNew(SimpleBug bug) {
                saveBug(bug);
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
            BugContainer bugContainer = (BugContainer) container;
            bugContainer.removeAllComponents();
            bugContainer.addComponent(view);

            SimpleBug bug = (SimpleBug) data.getParams();
            view.editItem(bug);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (bug.getId() == null) {
                breadcrumb.gotoBugAdd();
            } else {
                breadcrumb.gotoBugEdit(bug);
            }
        } else {
            throw new SecureAccessException();
        }
    }

    private int saveBug(SimpleBug bug) {
        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
        bug.setProjectid(CurrentProjectVariables.getProjectId());
        bug.setSaccountid(MyCollabUI.getAccountId());
        AsyncEventBus asyncEventBus = AppContextUtil.getSpringBean(AsyncEventBus.class);
        if (bug.getId() == null) {
            bug.setStatus(BugStatus.Open.name());
            bug.setCreateduser(UserUIContext.getUsername());
            int bugId = bugService.saveWithSession(bug, UserUIContext.getUsername());
            AttachmentUploadField uploadField = view.getAttachUploadField();
            String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(), bug.getProjectid(),
                    ProjectTypeConstants.BUG, "" + bugId);
            uploadField.saveContentsToRepo(attachPath);

            // save component
            BugRelatedItemService bugRelatedItemService = AppContextUtil.getSpringBean(BugRelatedItemService.class);
            bugRelatedItemService.saveAffectedVersionsOfBug(bugId, view.getAffectedVersions());
            bugRelatedItemService.saveFixedVersionsOfBug(bugId, view.getFixedVersion());
            bugRelatedItemService.saveComponentsOfBug(bugId, view.getComponents());
            asyncEventBus.post(new CleanCacheEvent(MyCollabUI.getAccountId(), new Class[]{BugService.class}));

            List<String> followers = view.getFollowers();
            if (followers.size() > 0) {
                List<MonitorItem> monitorItems = new ArrayList<>();
                for (String follower : followers) {
                    MonitorItem monitorItem = new MonitorItem();
                    monitorItem.setMonitorDate(new GregorianCalendar().getTime());
                    monitorItem.setSaccountid(MyCollabUI.getAccountId());
                    monitorItem.setType(ProjectTypeConstants.BUG);
                    monitorItem.setTypeid(bugId);
                    monitorItem.setUser(follower);
                    monitorItem.setExtratypeid(CurrentProjectVariables.getProjectId());
                    monitorItems.add(monitorItem);
                }
                MonitorItemService monitorItemService = AppContextUtil.getSpringBean(MonitorItemService.class);
                monitorItemService.saveMonitorItems(monitorItems);
            }
        } else {
            bugService.updateWithSession(bug, UserUIContext.getUsername());
            AttachmentUploadField uploadField = view.getAttachUploadField();
            String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(), bug.getProjectid(),
                    ProjectTypeConstants.BUG, "" + bug.getId());
            uploadField.saveContentsToRepo(attachPath);

            int bugId = bug.getId();
            BugRelatedItemService bugRelatedItemService = AppContextUtil.getSpringBean(BugRelatedItemService.class);
            bugRelatedItemService.updateAffectedVersionsOfBug(bugId, view.getAffectedVersions());
            bugRelatedItemService.updateFixedVersionsOfBug(bugId, view.getFixedVersion());
            bugRelatedItemService.updateComponentsOfBug(bugId, view.getComponents());
            asyncEventBus.post(new CleanCacheEvent(MyCollabUI.getAccountId(), new Class[]{BugService.class}));
        }

        return bug.getId();
    }
}