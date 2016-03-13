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

import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
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
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsUtil;
import com.esofthead.mycollab.module.project.ui.components.*;
import com.esofthead.mycollab.module.project.ui.format.MilestoneFieldFormatter;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.AsyncInvoker;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.DynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.ProjectPreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.field.ContainerViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.DateViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.RichTextViewField;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class MilestoneReadViewImpl extends AbstractPreviewItemComp<SimpleMilestone> implements MilestoneReadView {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(MilestoneReadViewImpl.class);

    private ProjectActivityComponent activityComponent;
    private TagViewComponent tagViewComponent;
    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private MilestoneTimeLogComp milestoneTimeLogComp;

    public MilestoneReadViewImpl() {
        super(AppContext.getMessage(MilestoneI18nEnum.VIEW_DETAIL_TITLE), ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleMilestone> controlsGenerator = new ProjectPreviewFormControlsGenerator<>(previewForm);
        return controlsGenerator.createButtonControls(ProjectRolePermissionCollections.MILESTONES);
    }

    @Override
    protected ComponentContainer createExtraControls() {
        tagViewComponent = new TagViewComponent();
        return tagViewComponent;
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.MILESTONE,
                CurrentProjectVariables.getProjectId(), MilestoneFieldFormatter.instance());
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        milestoneTimeLogComp = new MilestoneTimeLogComp();
        addToSideBar(dateInfoComp, peopleInfoComp, milestoneTimeLogComp);
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
        tagViewComponent.display(ProjectTypeConstants.MILESTONE, beanItem.getId());
        activityComponent.loadActivities("" + beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        milestoneTimeLogComp.displayTime(beanItem);
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
        return new DynaFormLayout(ProjectTypeConstants.MILESTONE, MilestoneDefaultFormLayoutFactory.getForm(),
                Milestone.Field.name.name());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> initBeanFormFieldFactory() {
        return new MilestoneFormFieldFactory(previewForm);
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.MILESTONE;
    }

    private class MilestoneFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {
        private static final long serialVersionUID = 1L;

        public MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleMilestone milestone = attachForm.getBean();
            if (Milestone.Field.startdate.equalTo(propertyId)) {
                return new DateViewField(milestone.getStartdate());
            } else if (Milestone.Field.enddate.equalTo(propertyId)) {
                return new DateViewField(milestone.getEnddate());
            } else if (Milestone.Field.owner.equalTo(propertyId)) {
                return new ProjectUserFormLinkField(milestone.getOwner(), milestone.getOwnerAvatarId(), milestone.getOwnerFullName());
            } else if (Milestone.Field.description.equalTo(propertyId)) {
                return new RichTextViewField(milestone.getDescription());
            } else if (Milestone.Field.status.equalTo(propertyId)) {
                String milestoneStatus = AppContext.getMessage(MilestoneStatus.class, beanItem.getStatus());
                FontAwesome statusIcon = ProjectAssetsUtil.getPhaseIcon(beanItem.getStatus());
                return new DefaultViewField(statusIcon.getHtml() + " " + milestoneStatus, ContentMode.HTML);
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
        private MVerticalLayout assignmentsLayout;

        AssignmentsComp() {
            withMargin(false).withWidth("100%");
            MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");

            final CheckBox openSelection = new CheckBox("Open", true);
            openSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if (openSelection.getValue()) {
                        searchCriteria.setIsOpenned(new SearchField());
                    } else {
                        searchCriteria.setIsOpenned(null);
                    }
                    updateSearchStatus();
                }
            });

            final CheckBox overdueSelection = new CheckBox("Overdue", false);
            overdueSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    if (overdueSelection.getValue()) {
                        searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS(), DateSearchField.LESSTHAN));
                    } else {
                        searchCriteria.setDueDate(null);
                    }
                    updateSearchStatus();
                }
            });

            Label spacingLbl1 = new Label("");
            Label spacingLbl2 = new Label("");

            final CheckBox taskSelection = new CheckBox("Tasks", true);
            taskSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    updateTypeSearchStatus(taskSelection.getValue(), ProjectTypeConstants.TASK);
                }
            });

            final CheckBox bugSelection = new CheckBox("Bugs", true);
            bugSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    updateTypeSearchStatus(bugSelection.getValue(), ProjectTypeConstants.BUG);
                }
            });

            final CheckBox riskSelection = new CheckBox("Risks", true);
            riskSelection.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    updateTypeSearchStatus(riskSelection.getValue(), ProjectTypeConstants.RISK);
                }
            });

            header.with(openSelection, overdueSelection, spacingLbl1, taskSelection, bugSelection, riskSelection, spacingLbl2)
                    .withAlign(openSelection, Alignment.MIDDLE_LEFT).withAlign(overdueSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(taskSelection, Alignment.MIDDLE_LEFT).withAlign(bugSelection, Alignment.MIDDLE_LEFT)
                    .withAlign(riskSelection, Alignment.MIDDLE_LEFT).expand(spacingLbl1, spacingLbl2);

            assignmentsLayout = new MVerticalLayout();
            this.with(header, assignmentsLayout);
            searchCriteria = new ProjectGenericTaskSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            searchCriteria.setIsOpenned(new SearchField());
            searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                    ProjectTypeConstants.RISK));
            searchCriteria.setMilestoneId(new NumberSearchField(beanItem.getId()));
            updateSearchStatus();
        }

        private void updateTypeSearchStatus(boolean selection, String type) {
            SetSearchField<String> types = searchCriteria.getTypes();
            if (types == null) {
                types = new SetSearchField<>();
            }
            if (selection) {
                types.addValue(type);
            } else {
                types.removeValue(type);
            }
            searchCriteria.setTypes(types);
            updateSearchStatus();
        }

        private void updateSearchStatus() {
            assignmentsLayout.removeAllComponents();

            AsyncInvoker.access(new AsyncInvoker.PageCommand() {
                @Override
                public void run() {
                    final ProjectGenericTaskService genericTaskService = ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class);
                    int totalCount = genericTaskService.getTotalCount(searchCriteria);
                    for (int i = 0; i < (totalCount / 20) + 1; i++) {
                        List<ProjectGenericTask> genericTasks = genericTaskService.findPagableListByCriteria(new SearchRequest<>(searchCriteria, i + 1, 20));
                        if (CollectionUtils.isNotEmpty(genericTasks)) {
                            for (ProjectGenericTask genericTask : genericTasks) {

                                MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName(UIConstants.HOVER_EFFECT_NOT_BOX);
                                rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
                                rowComp.with(new ELabel(ProjectAssetsManager.getAsset(genericTask.getType()).getHtml(),
                                        ContentMode.HTML).withWidthUndefined());
                                String avatarLink = StorageFactory.getInstance().getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
                                Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setTitle(genericTask
                                        .getAssignUserFullName());

                                ToggleGenericTaskSummaryField toggleGenericTaskSummaryField = new ToggleGenericTaskSummaryField(genericTask);
                                rowComp.with(new ELabel(img.write(), ContentMode.HTML).withWidthUndefined(),
                                        toggleGenericTaskSummaryField).expand(toggleGenericTaskSummaryField);
                                assignmentsLayout.add(rowComp);
                            }
                            this.push();
                        }
                    }
                }
            });
        }
    }

    private static class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(false);

            Label peopleInfoHeader = new Label(FontAwesome.USER.getHtml() + " " +
                    AppContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE), ContentMode.HTML);
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(bean, "createduser");
                String createdUserAvatarId = (String) PropertyUtils.getProperty(bean, "createdUserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils.getProperty(bean, "createdUserFullName");

                ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName,
                        createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(bean, "owner");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(bean, "ownerAvatarId");
                String assignUserDisplayName = (String) PropertyUtils.getProperty(bean, "ownerFullName");

                ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName, assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ", BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);

        }
    }
}