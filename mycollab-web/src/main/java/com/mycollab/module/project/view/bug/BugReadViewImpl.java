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
package com.mycollab.module.project.view.bug;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugRelation;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.*;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.domain.SimpleBug;
import com.mycollab.module.tracker.domain.SimpleRelatedBug;
import com.mycollab.module.tracker.service.BugRelationService;
import com.mycollab.module.project.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.ui.VerticalRemoveInlineComponentMarker;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class BugReadViewImpl extends AbstractPreviewItemComp<SimpleBug> implements BugReadView {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BugReadViewImpl.class);

    private ApplicationEventListener<BugEvent.BugChanged> bugChangedHandler = new
            ApplicationEventListener<BugEvent.BugChanged>() {
                @Override
                @Subscribe
                public void handle(BugEvent.BugChanged event) {
                    Integer bugChangeId = event.getData();
                    BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                    SimpleBug bugChange = bugService.findById(bugChangeId, AppUI.getAccountId());
                    previewItem(bugChange);
                }
            };

    private TagViewComponent tagViewComponent;
    private CssLayout bugWorkflowControl;
    private ProjectFollowersComp<SimpleBug> bugFollowersList;
    private BugTimeLogSheet bugTimeLogList;
    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private PlanningInfoComp planningInfoComp;
    private ProjectActivityComponent activityComponent;

    public BugReadViewImpl() {
        super(UserUIContext.getMessage(BugI18nEnum.DETAIL), ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG), new BugPreviewFormLayout());
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(bugChangedHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(bugChangedHandler);
        super.detach();
    }

    private void displayWorkflowControl() {
        if (InProgress.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();

            MButton openBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> {
                        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                        beanItem.setStatus(ReOpen.name());
                        bugService.updateSelectiveWithSession(beanItem, UserUIContext.getUsername());
                        EventBusFactory.getInstance().post(new BugEvent.BugChanged(this, beanItem.getId()));
                    })
                    .withStyleName(WebThemes.BUTTON_ACTION);

            MButton resolveBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
                    clickEvent -> UI.getCurrent().addWindow(new ResolvedInputWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);

            navButton.addButtons(openBtn, resolveBtn);
            bugWorkflowControl.addComponent(navButton);
        } else if (Open.name().equals(beanItem.getStatus()) || ReOpen.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();

            MButton inProgressBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.BUTTON_INPROGRESS),
                    clickEvent -> {
                        BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                        beanItem.setStatus(InProgress.name());
                        bugService.updateSelectiveWithSession(beanItem, UserUIContext.getUsername());
                        EventBusFactory.getInstance().post(new BugEvent.BugChanged(this, beanItem.getId()));
                    })
                    .withStyleName(WebThemes.BUTTON_ACTION);

            MButton resolveBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
                    clickEvent -> UI.getCurrent().addWindow(new ResolvedInputWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);

            navButton.addButtons(inProgressBtn, resolveBtn);
            bugWorkflowControl.addComponent(navButton);
        } else if (Verified.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            MButton reopenBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> UI.getCurrent().addWindow(new ReOpenWindow(beanItem))).withStyleName(WebThemes.BUTTON_ACTION);
            navButton.addButton(reopenBtn);

            bugWorkflowControl.addComponent(navButton);
        } else if (Resolved.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            MButton reopenBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> UI.getCurrent().addWindow(new ReOpenWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);

            MButton approveNCloseBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.BUTTON_APPROVE_CLOSE),
                    clickEvent -> UI.getCurrent().addWindow(new ApproveInputWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);
            navButton.addButtons(reopenBtn, approveNCloseBtn);
            bugWorkflowControl.addComponent(navButton);
        }
        bugWorkflowControl.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
    }

    @Override
    public SimpleBug getItem() {
        return beanItem;
    }

    @Override
    public void previewItem(SimpleBug item) {
        super.previewItem(item);
        displayWorkflowControl();
        ((BugPreviewFormLayout) previewLayout).displayBugHeader(beanItem);
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new ProjectActivityComponent(ProjectTypeConstants.BUG, CurrentProjectVariables.getProjectId());
        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        planningInfoComp = new PlanningInfoComp();
        bugFollowersList = new ProjectFollowersComp<>(ProjectTypeConstants.BUG, ProjectRolePermissionCollections.BUGS);

        ProjectView projectView = UIUtils.getRoot(this, ProjectView.class);
        MVerticalLayout detailLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, true, true));

        if (SiteConfiguration.isCommunityEdition()) {
            detailLayout.with(peopleInfoComp, planningInfoComp, bugFollowersList, dateInfoComp);
        } else {
            bugTimeLogList = ViewManager.getCacheComponent(BugTimeLogSheet.class);
            detailLayout.with(peopleInfoComp, planningInfoComp, bugTimeLogList, bugFollowersList, dateInfoComp);
        }

        Panel detailPanel = new Panel(UserUIContext.getMessage(GenericI18Enum.OPT_DETAILS), detailLayout);
        UIUtils.makeStackPanel(detailPanel);
        projectView.addComponentToRightBar(detailPanel);
    }

    @Override
    protected void onPreviewItem() {
        if (tagViewComponent != null) {
            tagViewComponent.display(ProjectTypeConstants.BUG, beanItem.getId());
        }
        if (bugTimeLogList != null) {
            bugTimeLogList.displayTime(beanItem);
        }
        activityComponent.loadActivities("" + beanItem.getId());

        bugFollowersList.displayFollowers(beanItem);
        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        planningInfoComp.displayPlanningInfo(beanItem);
    }

    @Override
    protected String initFormTitle() {
        return "";
    }

    @Override
    protected String getType() {
        return ProjectTypeConstants.BUG;
    }

    private static class BugPreviewFormLayout extends ReadViewLayout {
        private ToggleBugSummaryField toggleBugSummaryField;

        void displayBugHeader(SimpleBug bug) {
            MVerticalLayout header = new VerticalRemoveInlineComponentMarker().withMargin(false).withFullWidth();
            toggleBugSummaryField = new ToggleBugSummaryField(bug);
            toggleBugSummaryField.addLabelStyleNames(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
            header.with(toggleBugSummaryField).expand(toggleBugSummaryField);
            this.addHeader(header);

            if (bug.isCompleted()) {
                toggleBugSummaryField.addLabelStyleNames(WebThemes.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                toggleBugSummaryField.addLabelStyleNames(WebThemes.LABEL_OVERDUE);
            }

            BugRelationService bugRelationService = AppContextUtil.getSpringBean(BugRelationService.class);
            List<SimpleRelatedBug> relatedBugs = bugRelationService.findRelatedBugs(bug.getId());
            if (CollectionUtils.isNotEmpty(relatedBugs)) {
                for (SimpleRelatedBug relatedBug : relatedBugs) {
                    if (Boolean.TRUE.equals(relatedBug.getRelated())) {
                        ELabel relatedLink = new ELabel(UserUIContext.getMessage(BugRelation.class,
                                relatedBug.getRelatedType())).withStyleName(WebThemes.ARROW_BTN).withUndefinedWidth();
                        ToggleBugSummaryWithDependentField toggleRelatedBugField = new ToggleBugSummaryWithDependentField(bug, relatedBug.getRelatedBug());
                        MHorizontalLayout bugContainer = new MHorizontalLayout(relatedLink, toggleRelatedBugField)
                                .expand(toggleRelatedBugField).withFullWidth();
                        header.with(bugContainer);
                    } else {
                        Enum relatedEnum = BugRelation.valueOf(relatedBug.getRelatedType()).getReverse();
                        ELabel relatedLink = new ELabel(UserUIContext.getMessage(relatedEnum)).withStyleName(WebThemes.ARROW_BTN)
                                .withUndefinedWidth();
                        ToggleBugSummaryWithDependentField toggleRelatedBugField = new ToggleBugSummaryWithDependentField(bug, relatedBug.getRelatedBug());
                        MHorizontalLayout bugContainer = new MHorizontalLayout(relatedLink, toggleRelatedBugField)
                                .expand(toggleRelatedBugField).withFullWidth();
                        header.with(bugContainer);
                    }
                }
            }
        }

        @Override
        public void addTitleStyleName(String styleName) {
            toggleBugSummaryField.addLabelStyleNames(styleName);
        }

        @Override
        public void removeTitleStyleName(String styleName) {
            toggleBugSummaryField.removeLabelStyleName(styleName);
        }
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleBug> initPreviewForm() {
        return new BugPreviewForm();
    }

    @Override
    protected HorizontalLayout createButtonControls() {
        ProjectPreviewFormControlsGenerator<SimpleBug> bugPreviewFormControls = new ProjectPreviewFormControlsGenerator<>(previewForm);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
            MButton linkBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.OPT_BUG_DEPENDENCIES),
                    clickEvent -> UI.getCurrent().addWindow(new LinkIssueWindow(beanItem)))
                    .withIcon(VaadinIcons.BOLT);
            bugPreviewFormControls.addOptionButton(linkBtn);
        }

        HorizontalLayout topPanel = bugPreviewFormControls.createButtonControls(
                ProjectPreviewFormControlsGenerator.ADD_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.DELETE_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.EDIT_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.PRINT_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.CLONE_BTN_PRESENTED
                        | ProjectPreviewFormControlsGenerator.NAVIGATOR_BTN_PRESENTED,
                ProjectRolePermissionCollections.BUGS);

        MButton assignBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ASSIGN),
                clickEvent -> UI.getCurrent().addWindow(new AssignBugWindow(beanItem)))
                .withIcon(VaadinIcons.SHARE).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));

        bugWorkflowControl = new CssLayout();
        bugPreviewFormControls.insertToControlBlock(bugWorkflowControl);
        bugPreviewFormControls.insertToControlBlock(assignBtn);
        topPanel.setSizeUndefined();

        return topPanel;
    }

    protected ComponentContainer createExtraControls() {
        if (SiteConfiguration.isCommunityEdition()) {
            return null;
        } else {
            tagViewComponent = new TagViewComponent(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
            return tagViewComponent;
        }
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    public HasPreviewFormHandlers<SimpleBug> getPreviewFormHandlers() {
        return this.previewForm;
    }

    private static class PeopleInfoComp extends MVerticalLayout {
        private static final long serialVersionUID = 1L;

        private void displayEntryPeople(SimpleBug bug) {
            this.removeAllComponents();
            this.withMargin(false);

            Label peopleInfoHeader = ELabel.html(VaadinIcons.USER.getHtml() + " " + UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE));
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));

            ELabel createdLbl = new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE)).withStyleName(WebThemes.META_COLOR).withUndefinedWidth();
            layout.addComponent(createdLbl, 0, 0);

            String createdUserName = bug.getCreateduser();
            String createdUserAvatarId = bug.getLoguserAvatarId();
            String createdUserDisplayName = bug.getLoguserFullName();

            ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName, createdUserAvatarId, createdUserDisplayName);
            layout.addComponent(createdUserLink, 1, 0);
            layout.setColumnExpandRatio(1, 1.0f);

            ELabel assigneeLbl = new ELabel(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE)).withStyleName(WebThemes.META_COLOR).withUndefinedWidth();
            layout.addComponent(assigneeLbl, 0, 1);
            String assignUserName = bug.getAssignuser();
            String assignUserAvatarId = bug.getAssignUserAvatarId();
            String assignUserDisplayName = bug.getAssignuserFullName();

            ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName, assignUserAvatarId, assignUserDisplayName);
            layout.addComponent(assignUserLink, 1, 1);

            this.addComponent(layout);
        }
    }

    private static class PlanningInfoComp extends MVerticalLayout {
        private void displayPlanningInfo(SimpleBug bug) {
            this.removeAllComponents();
            this.withMargin(false);

            Label peopleInfoHeader = ELabel.html(VaadinIcons.CALENDAR_CLOCK.getHtml() + " " + UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PLANNING));
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 3);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));

            ELabel startDateLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE)).withStyleName(WebThemes.META_COLOR)
                    .withUndefinedWidth();
            layout.addComponent(startDateLbl, 0, 0);

            ELabel startDateVal = new ELabel(UserUIContext.formatDate(bug.getStartdate()));
            layout.addComponent(startDateVal, 1, 0);

            ELabel endDateLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE)).withStyleName(WebThemes.META_COLOR).withUndefinedWidth();
            layout.addComponent(endDateLbl, 0, 1);

            ELabel endDateVal = new ELabel(UserUIContext.formatDate(bug.getEnddate()));
            layout.addComponent(endDateVal, 1, 1);

            ELabel dueDateLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE)).withStyleName(WebThemes.META_COLOR).withUndefinedWidth();
            layout.addComponent(dueDateLbl, 0, 2);

            ELabel dueDateVal = new ELabel(UserUIContext.formatDate(bug.getDuedate()));
            layout.addComponent(dueDateVal, 1, 2);

            layout.setColumnExpandRatio(1, 1.0f);

            this.addComponent(layout);
        }
    }
}
