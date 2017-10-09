package com.mycollab.module.project.view;

import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.DefaultMassEditActionHandler;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ProjectListPresenter extends ListSelectionPresenter<ProjectListView, ProjectSearchCriteria, SimpleProject> {

    private ProjectService projectService;

    public ProjectListPresenter() {
        super(ProjectListView.class);
        projectService = AppContextUtil.getSpringBean(ProjectService.class);
    }

    @Override
    protected void viewAttached() {
        super.viewAttached();

        view.getPopupActionHandlers().setMassActionHandler(new DefaultMassEditActionHandler(this) {
            @Override
            protected void onSelectExtra(String id) {

            }

            @Override
            protected String getReportTitle() {
                return UserUIContext.getMessage(ProjectI18nEnum.LIST);
            }

            @Override
            protected Class<?> getReportModelClassType() {
                return SimpleProject.class;
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ComponentContainer componentContainer = (ComponentContainer) container;
        componentContainer.removeAllComponents();
        componentContainer.addComponent(view);
        ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        doSearch(searchCriteria);
    }

    @Override
    public void doSearch(ProjectSearchCriteria searchCriteria) {
        Collection<Integer> prjKeys = projectService.getProjectKeysUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
        if (CollectionUtils.isNotEmpty(prjKeys)) {
            searchCriteria.setProjectKeys(new SetSearchField<>(prjKeys));
            super.doSearch(searchCriteria);
        }
    }

    @Override
    public ISearchableService<ProjectSearchCriteria> getSearchService() {
        return projectService;
    }

    @Override
    protected void deleteSelectedItems() {
        throw new UnsupportedOperationException("Not supported");
    }
}
