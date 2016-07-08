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
package com.mycollab.module.project.view.milestone;

import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.*;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.ProjectPreviewFormControlsGenerator;
import com.mycollab.vaadin.web.ui.ReadViewLayout;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.PropertyUtils;
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

    public MilestoneReadViewImpl() {
        super(AppContext.getMessage(MilestoneI18nEnum.DETAIL), ProjectAssetsManager.getAsset
                (ProjectTypeConstants.MILESTONE), new MilestonePreviewFormLayout());
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleMilestone> initPreviewForm() {
        return new MilestonePreviewForm();
    }

    @Override
    protected ComponentContainer createButtonControls() {
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

        if (SiteConfiguration.isCommunityEdition()) {
            addToSideBar(dateInfoComp, peopleInfoComp);
        } else {
            milestoneTimeLogComp = new MilestoneTimeLogComp();
            addToSideBar(dateInfoComp, peopleInfoComp, milestoneTimeLogComp);
        }
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
            toggleMilestoneSummaryField = new ToggleMilestoneSummaryField(milestone);
            toggleMilestoneSummaryField.addLabelStyleName(ValoTheme.LABEL_H3);
            toggleMilestoneSummaryField.addLabelStyleName(ValoTheme.LABEL_NO_MARGIN);
            if (OptionI18nEnum.StatusI18nEnum.Closed.name().equals(milestone.getStatus())) {
                toggleMilestoneSummaryField.addLabelStyleName(UIConstants.LINK_COMPLETED);
            } else {
                toggleMilestoneSummaryField.removeLabelStyleName(UIConstants.LINK_COMPLETED);
            }
            this.addHeader(toggleMilestoneSummaryField);
        }

        @Override
        public void removeTitleStyleName(String styleName) {
            toggleMilestoneSummaryField.removeLabelStyleName(styleName);
        }

        @Override
        public void addTitleStyleName(String styleName) {
            toggleMilestoneSummaryField.addLabelStyleName(styleName);
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