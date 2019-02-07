package com.mycollab.module.project.view.reports;

import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class StandupListPresenter extends AbstractPresenter<StandupListView> {
    private static final long serialVersionUID = 1L;

    public StandupListPresenter() {
        super(StandupListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        IReportContainer projectModule = (IReportContainer) container;
        projectModule.addView(view);
        ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
        List<Integer> projectKeys = projectService.getOpenProjectKeysUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        if (CollectionUtils.isNotEmpty(projectKeys)) {
            LocalDate date = (LocalDate) data.getParams();
            view.display(projectKeys, date);
            ReportBreadcrumb breadCrumb = ViewManager.getCacheComponent(ReportBreadcrumb.class);
            breadCrumb.gotoStandupList(date);
        }
    }
}
