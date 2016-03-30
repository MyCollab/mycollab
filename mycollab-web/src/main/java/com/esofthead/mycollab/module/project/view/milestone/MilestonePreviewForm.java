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

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsUtil;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserFormLinkField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.AsyncInvoker;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.DynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.field.ContainerViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.DateViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.RichTextViewField;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class MilestonePreviewForm extends AdvancedPreviewBeanForm<SimpleMilestone> {
    @Override
    public void setBean(SimpleMilestone bean) {
        this.setFormLayoutFactory(new DynaFormLayout(ProjectTypeConstants.MILESTONE, MilestoneDefaultFormLayoutFactory.getForm(),
                Milestone.Field.name.name()));
        this.setBeanFormFieldFactory(new MilestoneFormFieldFactory(this));
        super.setBean(bean);
    }

    private static class MilestoneFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMilestone> {
        private static final long serialVersionUID = 1L;

        public MilestoneFormFieldFactory(GenericBeanForm<SimpleMilestone> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            SimpleMilestone beanItem = attachForm.getBean();
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
                String milestoneStatus = AppContext.getMessage(OptionI18nEnum.MilestoneStatus.class, beanItem.getStatus());
                FontAwesome statusIcon = ProjectAssetsUtil.getPhaseIcon(beanItem.getStatus());
                return new DefaultViewField(statusIcon.getHtml() + " " + milestoneStatus, ContentMode.HTML)
                        .withStyleName(UIConstants.FIELD_NOTE);
            } else if (Milestone.Field.id.equalTo(propertyId)) {
                ContainerViewField containerField = new ContainerViewField();
                containerField.addComponentField(new AssignmentsComp(beanItem));
                return containerField;
            }
            return null;
        }
    }

    private static class AssignmentsComp extends MVerticalLayout {
        private ProjectGenericTaskSearchCriteria searchCriteria;
        private SimpleMilestone beanItem;
        private MVerticalLayout assignmentsLayout;

        AssignmentsComp(SimpleMilestone milestone) {
            this.beanItem = milestone;
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
}
