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

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ProjectAssignmentsWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private ProjectGenericTaskSearchCriteria searchCriteria;

    private DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> taskList;

    public ProjectAssignmentsWidget() {
        super(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, 0), new CssLayout());
        this.setWidth("100%");

        final CheckBox overdueSelection = new CheckBox("Overdue");
        overdueSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean isOverdueOption = overdueSelection.getValue();
                if (isOverdueOption) {
                    searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
                } else {
                    searchCriteria.setDueDate(null);
                }
                updateSearchResult();
            }
        });

        final CheckBox myItemsSelection = new CheckBox("My Items");
        myItemsSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean isMyItemsOption = myItemsSelection.getValue();
                if (isMyItemsOption) {
                    searchCriteria.setAssignUser(new StringSearchField(AppContext.getUsername()));
                } else {
                    searchCriteria.setAssignUser(null);
                }
                updateSearchResult();
            }
        });

        taskList = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class),
                new TaskRowDisplayHandler(), 10);
        this.addHeaderElement(overdueSelection);
        this.addHeaderElement(myItemsSelection);
        this.bodyContent.addComponent(taskList);
    }

    public void showOpenAssignments() {
        searchCriteria = new ProjectGenericTaskSearchCriteria();
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        updateSearchResult();
    }

    private void updateSearchResult() {
        taskList.setSearchCriteria(searchCriteria);
        this.setTitle(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, taskList.getTotalCount()));
    }

    public static class TaskRowDisplayHandler implements DefaultBeanPagedList.RowDisplayHandler<ProjectGenericTask> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, ProjectGenericTask genericTask, int rowIndex) {
            MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName("list-row").withWidth("100%");
            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
            Div issueDiv = new Div();
            String uid = UUID.randomUUID().toString();
            A taskLink = new A().setId("tag" + uid);

            taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, genericTask.getType(), genericTask.getTypeId() + ""));
            taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            if (ProjectTypeConstants.BUG.equals(genericTask.getType()) || ProjectTypeConstants.TASK.equals(genericTask.getType())) {
                taskLink.appendText(String.format("[#%d] - %s", genericTask.getExtraTypeId(), genericTask.getName()));
                taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                        genericTask.getProjectId(), genericTask.getType(), genericTask.getExtraTypeId() + ""));
            } else {
                taskLink.appendText(genericTask.getName());
                taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                        genericTask.getProjectId(), genericTask.getType(), genericTask.getTypeId() + ""));
            }

            issueDiv.appendChild(taskLink, TooltipHelper.buildDivTooltipEnable(uid));
            Label issueLbl = new Label(issueDiv.write(), ContentMode.HTML);
            if (genericTask.isClosed()) {
                issueLbl.addStyleName("completed");
            } else if (genericTask.isOverdue()) {
                issueLbl.addStyleName("overdue");
            }

            String avatarLink = StorageFactory.getInstance().getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
            Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setTitle(genericTask
                    .getAssignUserFullName());

            MHorizontalLayout iconsLayout = new MHorizontalLayout().with(new ELabel(ProjectAssetsManager.getAsset
                    (genericTask.getType()).getHtml(), ContentMode.HTML), new ELabel(img.write(), ContentMode.HTML));
            MCssLayout issueWrapper = new MCssLayout(issueLbl);
            rowComp.with(iconsLayout, issueWrapper).expand(issueWrapper);
            return rowComp;
        }
    }
}