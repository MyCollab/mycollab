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

package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.components.AbstractPreviewItemComp2;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.DateInfoComp;
import com.esofthead.mycollab.module.project.ui.components.DynaFormLayout;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DateViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class MilestoneReadViewImpl extends
        AbstractPreviewItemComp2<SimpleMilestone> implements MilestoneReadView {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory
            .getLogger(MilestoneReadViewImpl.class);

    private CommentDisplay commentListComp;

    private MilestoneHistoryLogList historyListComp;

    private DateInfoComp dateInfoComp;

    private PeopleInfoComp peopleInfoComp;

    public MilestoneReadViewImpl() {
        super(AppContext.getMessage(MilestoneI18nEnum.VIEW_DETAIL_TITLE),
                MyCollabResource.newResource(WebResourceIds._24_project_phase));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleMilestone> controlsGenerator = new ProjectPreviewFormControlsGenerator<>(
                this.previewForm);
        return controlsGenerator
                .createButtonControls(ProjectRolePermissionCollections.MILESTONES);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
        tabContainer.setWidth("100%");

        tabContainer.addTab(this.commentListComp, AppContext
                        .getMessage(ProjectCommonI18nEnum.TAB_COMMENT),
                MyCollabResource
                        .newResource(WebResourceIds._16_project_gray_comment));
        tabContainer.addTab(historyListComp, AppContext
                        .getMessage(ProjectCommonI18nEnum.TAB_HISTORY),
                MyCollabResource
                        .newResource(WebResourceIds._16_project_gray_history));

        return tabContainer;
    }

    @Override
    protected void initRelatedComponents() {
        this.historyListComp = new MilestoneHistoryLogList(
                ModuleNameConstants.PRJ, ProjectTypeConstants.MILESTONE);
        this.commentListComp = new CommentDisplay(CommentType.PRJ_MILESTONE,
                CurrentProjectVariables.getProjectId(), true, true,
                ProjectMilestoneRelayEmailNotificationAction.class);
        this.commentListComp.setMargin(true);

        dateInfoComp = new DateInfoComp();
        addToSideBar(dateInfoComp);

        peopleInfoComp = new PeopleInfoComp();
        addToSideBar(peopleInfoComp);
    }

    @Override
    public SimpleMilestone getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleMilestone> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    protected void onPreviewItem() {
        this.commentListComp.loadComments("" + this.beanItem.getId());

        historyListComp.loadHistory(beanItem.getId());

        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);

        if (OptionI18nEnum.StatusI18nEnum.Closed.name().equals(beanItem.getStatus())) {
            addLayoutStyleName(UIConstants.LINK_COMPLETED);
        }
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getName();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.RISK,
                MilestoneDefaultFormLayoutFactory.getForm(),
                Milestone.Field.name.name());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
        return new MilestoneFormFieldFactory(previewForm);
    }

    private class MilestoneFormFieldFactory extends
            AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {

        private static final long serialVersionUID = 1L;

        public MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleMilestone milestone = attachForm.getBean();
            if (Milestone.Field.startdate.equalTo(propertyId)) {
                return new DateViewField(
                        milestone.getStartdate());
            } else if (Milestone.Field.enddate.equalTo(propertyId)) {
                return new DateViewField(
                        milestone.getEnddate());
            } else if (Milestone.Field.owner.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(milestone.getOwner(),
                        milestone.getOwnerAvatarId(),
                        milestone.getOwnerFullName());
            } else if (Milestone.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(
                        milestone.getDescription());
            } else if (Milestone.Field.status.equalTo(propertyId)) {
                final ContainerHorizontalViewField statusField = new ContainerHorizontalViewField();
                Image icon = new Image();
                icon.setSource(new ExternalResource(ProjectResources
                        .getIconResource12LinkOfPhaseStatus(beanItem
                                .getStatus())));
                statusField.addComponentField(icon);
                Label statusLbl = new Label(AppContext.getMessage(
                        MilestoneStatus.class, beanItem.getStatus()));
                statusField.addComponentField(statusLbl);
                statusField.getLayout().setExpandRatio(statusLbl, 1.0f);
                return statusField;
            } else if (Milestone.Field.id.equalTo(propertyId)) {
                ContainerViewField containerField = new ContainerViewField();

                containerField.addComponentField(new AssignmentsComp());
                return containerField;
            }
            return null;
        }
    }

    private class AssignmentsComp extends MVerticalLayout {
        private ProjectGenericTaskSearchCriteria searchCriteria;
        private DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> assignmentsList;

        AssignmentsComp() {
           withMargin(false).withWidth("100%");
            MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");
            CheckBox openSelection = new CheckBox("Open", true);
            CheckBox overdueSelection = new CheckBox("Overdue", false);
            Label spacingLbl1 = new Label("");
            Label spacingLbl2 = new Label("");
            CheckBox taskSelection = new CheckBox("Tasks", true);
            CheckBox bugSelection = new CheckBox("Bugs", true);
            Button chartBtn = new Button("");
            chartBtn.setIcon(MyCollabResource.newResource(WebResourceIds._16_project_bug_advanced_display));

            header.with(openSelection, overdueSelection, spacingLbl1, taskSelection, bugSelection, spacingLbl2, chartBtn)
                    .withAlign(openSelection, Alignment.MIDDLE_LEFT).withAlign(overdueSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(taskSelection, Alignment.MIDDLE_LEFT).withAlign(bugSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(chartBtn, Alignment.MIDDLE_RIGHT).expand(spacingLbl1, spacingLbl2);

            assignmentsList = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class), new
                    AssignmentRowDisplay());

            this.with(header, assignmentsList);

            searchCriteria = new ProjectGenericTaskSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setIsOpenned(new SearchField());
            searchCriteria.setTypes(new SetSearchField<>(new String[]{ProjectTypeConstants.BUG, ProjectTypeConstants.TASK}));
            assignmentsList.setSearchCriteria(searchCriteria);
        }
    }

    private static class AssignmentRowDisplay implements AbstractBeanPagedList.RowDisplayHandler<ProjectGenericTask> {
        @Override
        public Component generateRow(ProjectGenericTask task, int rowIndex) {
            return new Label("Hello world");
        }
    }

    private class PeopleInfoComp extends VerticalLayout {
        private static final long serialVersionUID = 1L;

        public void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.setSpacing(true);
            this.setMargin(new MarginInfo(false, false, false, true));

            Label peopleInfoHeader = new Label(
                    AppContext
                            .getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE));
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(
                        AppContext
                                .getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(
                        bean, "createduser");
                String createdUserAvatarId = (String) PropertyUtils
                        .getProperty(bean, "createdUserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "createdUserFullName");

                UserLink createdUserLink = new UserLink(createdUserName,
                        createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(
                        AppContext
                                .getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(
                        bean, "owner");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(
                        bean, "ownerAvatarId");
                String assignUserDisplayName = (String) PropertyUtils
                        .getProperty(bean, "ownerFullName");

                UserLink assignUserLink = new UserLink(assignUserName,
                        assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ",
                        BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);

        }
    }
}
