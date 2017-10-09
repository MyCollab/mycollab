package com.mycollab.module.project.view.page;

import com.mycollab.core.SecureAccessException;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.PageEvent;
import com.mycollab.module.project.service.ProjectPageService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class PageAddPresenter extends ProjectGenericPresenter<PageAddView> {
    private static final long serialVersionUID = 1L;

    public PageAddPresenter() {
        super(PageAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<Page>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(Page page) {
                savePage(page);
                EventBusFactory.getInstance().post(new PageEvent.GotoRead(this, page));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new PageEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(Page page) {
                savePage(page);
                EventBusFactory.getInstance().post(new PageEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES)) {
            PageContainer pageContainer = (PageContainer) container;
            pageContainer.navigateToContainer(ProjectTypeConstants.PAGE);
            pageContainer.setContent(view);

            Page page = (Page) data.getParams();
            view.editItem(page);

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (page.getPath().equals("")) {
                breadcrumb.gotoPageAdd();
            } else {
                breadcrumb.gotoPageEdit(page);
            }

        } else {
            throw new SecureAccessException();
        }
    }

    private void savePage(Page page) {
        ProjectPageService pageService = AppContextUtil.getSpringBean(ProjectPageService.class);

        pageService.savePage(page, UserUIContext.getUsername(), CurrentProjectVariables.getProjectId(),
                AppUI.getAccountId());
    }
}
