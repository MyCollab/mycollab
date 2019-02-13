/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.milestone;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.*;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.AbstractPreviewItemComp;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.ReadViewLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;

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
    private PlanningInfoComp planningInfoComp;

    public MilestoneReadViewImpl() {
        super(UserUIContext.getMessage(MilestoneI18nEnum.DETAIL), ProjectAssetsManager.getAsset
                (ProjectTypeConstants.MILESTONE), new MilestonePreviewFormLayout());
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
        return new MilestonePreviewForm();
    }

    @Override
    protected HorizontalLayout createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleMilestone> controlsGenerator = new ProjectPreviewFormControlsGenerator<>(previewForm);
        return controlsGenerator.createButtonControls(ProjectRolePermissionCollections.MILESTONES);
    }

    @Override
    protected ComponentContainer createExtraControls() {
        if (SiteConfiguration.isCommunityEdition()) {
            return null;
        } else {
            tagViewComponent = new TagViewComponent(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));
            return tagViewComponent;
        }
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.MILESTONE, CurrentProjectVariables.getProjectId());
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        planningInfoComp = new PlanningInfoComp();

        ProjectView projectView = UIUtils.getRoot(this, ProjectView.class);
        MVerticalLayout detailLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
        if (SiteConfiguration.isCommunityEdition()) {
            detailLayout.with(peopleInfoComp, planningInfoComp, dateInfoComp);
        } else {
            milestoneTimeLogComp = new MilestoneTimeLogComp();
            detailLayout.with(peopleInfoComp, planningInfoComp, milestoneTimeLogComp, dateInfoComp);
        }

        Panel detailPanel = new Panel(UserUIContext.getMessage(GenericI18Enum.OPT_DETAILS), detailLayout);
        UIUtils.makeStackPanel(detailPanel);
        projectView.addComponentToRightBar(detailPanel);
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
        if (tagViewComponent != null) {
            tagViewComponent.display(ProjectTypeConstants.MILESTONE, beanItem.getId());
        }
        if (milestoneTimeLogComp != null) {
            milestoneTimeLogComp.displayTime(beanItem);
        }
        ((MilestonePreviewFormLayout) previewLayout).displayHeader(beanItem);
        activityComponent.loadActivities("" + beanItem.getId());
        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        planningInfoComp.displayPlanningInfo(beanItem);
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getName();
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.MILESTONE;
    }

    private static class MilestonePreviewFormLayout extends ReadViewLayout {
        private ToggleMilestoneSummaryField toggleMilestoneSummaryField;

        void displayHeader(SimpleMilestone milestone) {
            toggleMilestoneSummaryField = new ToggleMilestoneSummaryField(milestone, true, false);
            toggleMilestoneSummaryField.addLabelStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
            if (StatusI18nEnum.Closed.name().equals(milestone.getStatus())) {
                toggleMilestoneSummaryField.addLabelStyleNames(WebThemes.LINK_COMPLETED);
            } else {
                toggleMilestoneSummaryField.removeLabelStyleName(WebThemes.LINK_COMPLETED);
            }
            this.addHeader(toggleMilestoneSummaryField);
        }

        @Override
        public void removeTitleStyleName(String styleName) {
            toggleMilestoneSummaryField.removeLabelStyleName(styleName);
        }

        @Override
        public void addTitleStyleName(String styleName) {
            toggleMilestoneSummaryField.addLabelStyleNames(styleName);
        }
    }

    private static class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        void displayEntryPeople(SimpleMilestone milestone) {
            this.removeAllComponents();
            this.withMargin(false);

            ELabel peopleInfoHeader = ELabel.html(VaadinIcons.USER.getHtml() + " " +
                    UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE)).withStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));

            Label createdLbl = new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE)).withUndefinedWidth().withStyleName(WebThemes.META_COLOR);
            layout.addComponent(createdLbl, 0, 0);

            String createdUserName = milestone.getCreateduser();
            String createdUserAvatarId = milestone.getCreatedUserAvatarId();
            String createdUserDisplayName = milestone.getCreatedUserFullName();

            ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName, createdUserAvatarId, createdUserDisplayName);
            layout.addComponent(createdUserLink, 1, 0);
            layout.setColumnExpandRatio(1, 1.0f);

            ELabel assigneeLbl = new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE))
                    .withUndefinedWidth().withStyleName(WebThemes.META_COLOR);
            layout.addComponent(assigneeLbl, 0, 1);
            String assignUserName = milestone.getAssignuser();
            String assignUserAvatarId = milestone.getOwnerAvatarId();
            String assignUserDisplayName = milestone.getOwnerFullName();

            ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName, assignUserAvatarId, assignUserDisplayName);
            layout.addComponent(assignUserLink, 1, 1);

            this.addComponent(layout);
        }
    }

    private static class PlanningInfoComp extends MVerticalLayout {
        private void displayPlanningInfo(SimpleMilestone milestone) {
            this.removeAllComponents();
            this.withMargin(false);

            Label peopleInfoHeader = ELabel.html(VaadinIcons.CALENDAR_CLOCK.getHtml() + " " + UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PLANNING));
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));

            ELabel startDateLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE)).withStyleName(WebThemes.META_COLOR)
                    .withUndefinedWidth();
            layout.addComponent(startDateLbl, 0, 0);

            ELabel startDateVal = new ELabel(UserUIContext.formatDate(milestone.getStartdate()));
            layout.addComponent(startDateVal, 1, 0);

            ELabel endDateLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE)).withStyleName(WebThemes.META_COLOR).withUndefinedWidth();
            layout.addComponent(endDateLbl, 0, 1);

            ELabel endDateVal = new ELabel(UserUIContext.formatDate(milestone.getEnddate()));
            layout.addComponent(endDateVal, 1, 1);

            layout.setColumnExpandRatio(1, 1.0f);

            this.addComponent(layout);
        }
    }
}