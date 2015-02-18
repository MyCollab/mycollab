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
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MyProjectListComponent extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private ProjectPagedList projectList;

    private Label titleLbl;

    public MyProjectListComponent() {
        withSpacing(false).withMargin(false);
        this.addStyleName("myprojectlist");

        MHorizontalLayout header = new MHorizontalLayout().withSpacing(false).withMargin(new MarginInfo(false, true,
                false, true)).withHeight("34px");
        header.addStyleName("panel-header");
        titleLbl = new Label(AppContext.getMessage(
                ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, 0));

        final PopupButton projectsPopup = new PopupButton("");
        projectsPopup.addStyleName("popuplistindicator");

        final MVerticalLayout filterBtnLayout = new MVerticalLayout().withWidth("200px");

        ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
        int allProjectCount = projectService.getTotalCount(getAllProjectsSearchCriteria());
        Button allProjectsBtn = new Button(AppContext.getMessage(
                ProjectCommonI18nEnum.BUTTON_ALL_PROJECTS, allProjectCount),
                new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        displayAllProjects();
                        projectsPopup.setPopupVisible(false);
                    }
                });
        allProjectsBtn.setStyleName("link");
        filterBtnLayout.addComponent(allProjectsBtn);

        int activeProjectsCount = projectService
                .getTotalCount(getActiveProjectsSearchCriteria());
        Button activeProjectsBtn = new Button(AppContext.getMessage(
                ProjectCommonI18nEnum.BUTTON_ACTIVE_PROJECTS,
                activeProjectsCount), new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                displayActiveProjects();
                projectsPopup.setPopupVisible(false);
            }
        });
        activeProjectsBtn.setStyleName("link");
        filterBtnLayout.addComponent(activeProjectsBtn);

        int archiveProjectsCount = projectService
                .getTotalCount(getArchivedProjectsSearchCriteria());
        Button archiveProjectsBtn = new Button(AppContext.getMessage(
                ProjectCommonI18nEnum.BUTTON_ARCHIVE_PROJECTS,
                archiveProjectsCount), new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                displayArchiveProjects();
                projectsPopup.setPopupVisible(false);
            }
        });
        archiveProjectsBtn.setStyleName("link");
        filterBtnLayout.addComponent(archiveProjectsBtn);
        projectsPopup.setContent(filterBtnLayout);

        header.with(titleLbl, projectsPopup).withAlign(titleLbl, Alignment.MIDDLE_LEFT).withAlign(projectsPopup,
                Alignment.MIDDLE_RIGHT).expand(titleLbl);

        this.projectList = new ProjectPagedList();
        this.with(header, projectList);
    }

    public void displayDefaultProjectsList() {
        displayActiveProjects();
    }

    private ProjectSearchCriteria getAllProjectsSearchCriteria() {
        final ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
                AppContext.getUsername()));
        return searchCriteria;
    }

    private ProjectSearchCriteria getActiveProjectsSearchCriteria() {
        final ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
                AppContext.getUsername()));
        searchCriteria.setProjectStatuses(new SetSearchField<>(
                new String[]{StatusI18nEnum.Open.name()}));
        return searchCriteria;
    }

    private ProjectSearchCriteria getArchivedProjectsSearchCriteria() {
        final ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
        searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
                AppContext.getUsername()));
        searchCriteria.setProjectStatuses(new SetSearchField<>(
                new String[]{StatusI18nEnum.Archived.name()}));
        return searchCriteria;
    }

    private void displayAllProjects() {
        final ProjectSearchCriteria searchCriteria = getAllProjectsSearchCriteria();
        this.projectList.setSearchCriteria(searchCriteria);

        int totalCount = this.projectList.getTotalCount();
        titleLbl.setValue(AppContext.getMessage(
                ProjectCommonI18nEnum.WIDGET_ALL_PROJECTS_TITLE, totalCount));
    }

    private void displayActiveProjects() {
        final ProjectSearchCriteria searchCriteria = getActiveProjectsSearchCriteria();
        this.projectList.setSearchCriteria(searchCriteria);

        int totalCount = this.projectList.getTotalCount();
        titleLbl.setValue(AppContext.getMessage(
                ProjectCommonI18nEnum.WIDGET_ACTIVE_PROJECTS_TITLE, totalCount));
    }

    private void displayArchiveProjects() {
        ProjectSearchCriteria searchCriteria = getArchivedProjectsSearchCriteria();
        this.projectList.setSearchCriteria(searchCriteria);

        int totalCount = this.projectList.getTotalCount();
        titleLbl.setValue(AppContext
                .getMessage(
                        ProjectCommonI18nEnum.WIDGET_ARCHIVE_PROJECTS_TITLE,
                        totalCount));
    }
}