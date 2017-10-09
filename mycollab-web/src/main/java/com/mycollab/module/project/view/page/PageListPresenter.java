package com.mycollab.module.project.view.page;

import com.mycollab.core.SecureAccessException;
import com.mycollab.module.page.domain.PageResource;
import com.mycollab.module.page.service.PageService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class PageListPresenter extends ProjectGenericPresenter<PageListView> {
    private static final long serialVersionUID = 1L;

    private PageService pageService;

    public PageListPresenter() {
        super(PageListView.class);
        pageService = AppContextUtil.getSpringBean(PageService.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.PAGES)) {
            PageContainer pageContainer = (PageContainer) container;
            pageContainer.navigateToContainer(ProjectTypeConstants.PAGE);

            String path = (String) data.getParams();
            if (path == null) {
                path = CurrentProjectVariables.getCurrentPagePath();
            } else {
                CurrentProjectVariables.setCurrentPagePath(path);
            }
            List<PageResource> resources = pageService.getResources(path, UserUIContext.getUsername());
            if (!CollectionUtils.isEmpty(resources)) {
                pageContainer.setContent(view);
                view.displayDefaultPages(resources);
            } else {
                pageContainer.setContent(view);
                view.showNoItemView();
            }

            ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoPageList();
        } else {
            throw new SecureAccessException();
        }
    }

}
