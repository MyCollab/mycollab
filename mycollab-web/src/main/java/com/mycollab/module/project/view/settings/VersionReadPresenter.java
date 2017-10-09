package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugVersionEvent;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.vaadin.reporting.FormReportLayout;
import com.mycollab.vaadin.reporting.PrintButton;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class VersionReadPresenter extends AbstractPresenter<VersionReadView> {
    private static final long serialVersionUID = 1L;

    public VersionReadPresenter() {
        super(VersionReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<Version>() {
            @Override
            public void onEdit(Version data) {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(Version data) {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(Version data) {
                VersionService versionService = AppContextUtil.getSpringBean(VersionService.class);
                versionService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
            }

            @Override
            public void onClone(Version data) {
                Version cloneData = (Version) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoList(this, null));
            }

            @Override
            public void onPrint(Object source, Version data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(ProjectTypeConstants.BUG_VERSION, Version.Field.name.name(),
                        VersionDefaultFormLayoutFactory.getForm(), Version.Field.id.name()));
            }

            @Override
            public void gotoNext(Version data) {
                VersionService componentService = AppContextUtil.getSpringBean(VersionService.class);
                VersionSearchCriteria criteria = new VersionSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                Integer nextId = componentService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(Version data) {
                VersionService componentService = AppContextUtil.getSpringBean(VersionService.class);
                VersionSearchCriteria criteria = new VersionSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN));
                Integer nextId = componentService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new BugVersionEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.VERSIONS)) {
            if (data.getParams() instanceof Integer) {
                VersionService componentService = AppContextUtil.getSpringBean(VersionService.class);
                Version version = componentService.findById((Integer) data.getParams(), AppUI.getAccountId());
                if (version != null) {
                    VersionContainer versionContainer = (VersionContainer) container;
                    versionContainer.removeAllComponents();
                    versionContainer.addComponent(view);
                    view.previewItem(version);

                    ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                    breadcrumb.gotoVersionRead(version);
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
