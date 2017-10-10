/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.event.BugEvent;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugRelation;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.*;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.SimpleRelatedBug;
import com.mycollab.module.tracker.service.BugRelationService;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.VerticalRemoveInlineComponentMarker;
import com.mycollab.vaadin.web.ui.*;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

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
    private ProjectActivityComponent activityComponent;

    public BugReadViewImpl() {
        super(UserUIContext.getMessage(BugI18nEnum.DETAIL),
                ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG), new BugPreviewFormLayout());
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
        if (BugStatus.Open.name().equals(beanItem.getStatus()) || BugStatus.ReOpen.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();

            MButton resolveBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.BUTTON_RESOLVED),
                    clickEvent -> UI.getCurrent().addWindow(new ResolvedInputWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);
            navButton.addButton(resolveBtn);
            bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Verified.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            MButton reopenBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> UI.getCurrent().addWindow(new ReOpenWindow(beanItem))).withStyleName(WebThemes.BUTTON_ACTION);
            navButton.addButton(reopenBtn);

            bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Resolved.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            MButton reopenBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> UI.getCurrent().addWindow(new ReOpenWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);
            navButton.addButton(reopenBtn);

            MButton approveNCloseBtn = new MButton(UserUIContext.getMessage(BugI18nEnum.BUTTON_APPROVE_CLOSE),
                    clickEvent -> UI.getCurrent().addWindow(new ApproveInputWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);
            navButton.addButton(approveNCloseBtn);
            bugWorkflowControl.addComponent(navButton);
        } else if (BugStatus.Resolved.name().equals(beanItem.getStatus())) {
            bugWorkflowControl.removeAllComponents();
            ButtonGroup navButton = new ButtonGroup();
            MButton reopenBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_REOPEN),
                    clickEvent -> UI.getCurrent().addWindow(new ReOpenWindow(beanItem)))
                    .withStyleName(WebThemes.BUTTON_ACTION);
            navButton.addButton(reopenBtn);

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
        bugFollowersList = new ProjectFollowersComp<>(ProjectTypeConstants.BUG, ProjectRolePermissionCollections.BUGS);

        if (SiteConfiguration.isCommunityEdition()) {
            addToSideBar(dateInfoComp, peopleInfoComp, bugFollowersList);
        } else {
            bugTimeLogList = ViewManager.getCacheComponent(BugTimeLogSheet.class);
            addToSideBar(dateInfoComp, peopleInfoComp, bugTimeLogList, bugFollowersList);
        }
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

        void displayBugHeader(final SimpleBug bug) {
            MVerticalLayout header = new VerticalRemoveInlineComponentMarker().withFullWidth().withMargin(false);
            toggleBugSummaryField = new ToggleBugSummaryField(bug);
            toggleBugSummaryField.addLabelStyleName(ValoTheme.LABEL_H3);
            toggleBugSummaryField.addLabelStyleName(ValoTheme.LABEL_NO_MARGIN);
            header.with(toggleBugSummaryField).expand(toggleBugSummaryField);
            this.addHeader(header);

            if (bug.isCompleted()) {
                toggleBugSummaryField.addLabelStyleName(WebThemes.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                toggleBugSummaryField.addLabelStyleName(WebThemes.LABEL_OVERDUE);
            }

            BugRelationService bugRelationService = AppContextUtil.getSpringBean(BugRelationService.class);
            List<SimpleRelatedBug> relatedBugs = bugRelationService.findRelatedBugs(bug.getId());
            if (CollectionUtils.isNotEmpty(relatedBugs)) {
                for (final SimpleRelatedBug relatedBug : relatedBugs) {
                    if (relatedBug.getRelated()) {
                        ELabel relatedLink = new ELabel(UserUIContext.getMessage(BugRelation.class,
                                relatedBug.getRelatedType())).withStyleName(WebThemes.ARROW_BTN).withWidthUndefined();
                        ToggleBugSummaryWithDependentField toggleRelatedBugField = new ToggleBugSummaryWithDependentField(bug, relatedBug.getRelatedBug());
                        MHorizontalLayout bugContainer = new MHorizontalLayout(relatedLink, toggleRelatedBugField)
                                .expand(toggleRelatedBugField).withFullWidth();
                        header.with(bugContainer);
                    } else {
                        Enum relatedEnum = BugRelation.valueOf(relatedBug.getRelatedType()).getReverse();
                        ELabel relatedLink = new ELabel(UserUIContext.getMessage(relatedEnum)).withStyleName(WebThemes.ARROW_BTN)
                                .withWidthUndefined();
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
            toggleBugSummaryField.addLabelStyleName(styleName);
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
                    .withIcon(FontAwesome.BOLT);
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
                .withIcon(FontAwesome.SHARE).withStyleName(WebThemes.BUTTON_ACTION);
        assignBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));

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

        private void displayEntryPeople(ValuedBean bean) {
            this.removeAllComponents();
            this.withMargin(false);

            Label peopleInfoHeader = ELabel.html(FontAwesome.USER.getHtml() + " " +
                    UserUIContext.getMessage(ProjectCommonI18nEnum.SUB_INFO_PEOPLE));
            peopleInfoHeader.setStyleName("info-hdr");
            this.addComponent(peopleInfoHeader);

            GridLayout layout = new GridLayout(2, 2);
            layout.setSpacing(true);
            layout.setWidth("100%");
            layout.setMargin(new MarginInfo(false, false, false, true));
            try {
                Label createdLbl = new Label(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE));
                createdLbl.setSizeUndefined();
                layout.addComponent(createdLbl, 0, 0);

                String createdUserName = (String) PropertyUtils.getProperty(bean, "createduser");
                String createdUserAvatarId = (String) PropertyUtils.getProperty(bean, "loguserAvatarId");
                String createdUserDisplayName = (String) PropertyUtils.getProperty(bean, "loguserFullName");

                ProjectMemberLink createdUserLink = new ProjectMemberLink(createdUserName, createdUserAvatarId, createdUserDisplayName);
                layout.addComponent(createdUserLink, 1, 0);
                layout.setColumnExpandRatio(1, 1.0f);

                Label assigneeLbl = new Label(UserUIContext.getMessage(ProjectCommonI18nEnum.ITEM_ASSIGN_PEOPLE));
                assigneeLbl.setSizeUndefined();
                layout.addComponent(assigneeLbl, 0, 1);
                String assignUserName = (String) PropertyUtils.getProperty(bean, "assignuser");
                String assignUserAvatarId = (String) PropertyUtils.getProperty(bean, "assignUserAvatarId");
                String assignUserDisplayName = (String) PropertyUtils.getProperty(bean, "assignuserFullName");

                ProjectMemberLink assignUserLink = new ProjectMemberLink(assignUserName, assignUserAvatarId, assignUserDisplayName);
                layout.addComponent(assignUserLink, 1, 1);
            } catch (Exception e) {
                LOG.error("Can not build user link {} ", BeanUtility.printBeanObj(bean));
            }

            this.addComponent(layout);

        }
    }
}
