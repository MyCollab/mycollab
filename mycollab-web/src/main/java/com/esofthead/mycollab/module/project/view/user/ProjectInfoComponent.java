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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.events.*;
import com.esofthead.mycollab.module.project.i18n.*;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.*;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public class ProjectInfoComponent extends MHorizontalLayout {

    private Label billableHoursLbl, nonBillableHoursLbl;

    private ApplicationEventListener<ProjectEvent.TimeLoggingChangedEvent>
            timeLoggingChangedEventApplicationEventListener = new ApplicationEventListener<ProjectEvent.TimeLoggingChangedEvent>() {
        @Subscribe
        @AllowConcurrentEvents
        @Override
        public void handle(ProjectEvent.TimeLoggingChangedEvent event) {
            ItemTimeLoggingSearchCriteria baseCriteria = new ItemTimeLoggingSearchCriteria();
            baseCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));

            //get Billable hours
            baseCriteria.setIsBillable(new BooleanSearchField(true));
            ItemTimeLoggingService loggingService = ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class);
            Double billableHours = loggingService.getTotalHoursByCriteria(baseCriteria);
            billableHoursLbl.setValue(FontAwesome.MONEY.getHtml() + " " + billableHours);

            // Get Non billable hours
            baseCriteria.setIsBillable(new BooleanSearchField(false));
            Double nonBillableHours = loggingService.getTotalHoursByCriteria(baseCriteria);
            nonBillableHoursLbl.setValue(FontAwesome.GIFT.getHtml() + " " + nonBillableHours);
        }
    };

    public ProjectInfoComponent(final SimpleProject project) {
        this.withMargin(true).withStyleName("project-info").withFullWidth().withHeight("80px");
        ELabel headerLbl = ELabel.h2(FontAwesome.BUILDING_O.getHtml() + String.format(" %s (%s)", project.getName(),
                project.getShortname()));
        headerLbl.setDescription(project.getDescription());
        headerLbl.addStyleName("header");
        MVerticalLayout headerLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));

        MHorizontalLayout footer = new MHorizontalLayout();
        footer.addStyleName("desc");
        if (project.getHomepage() != null) {
            ELabel homepageLbl = new ELabel(FontAwesome.WECHAT.getHtml() + " " + new A(project.getHomepage())
                    .appendText(project.getHomepage()).setTarget("_blank").write(), ContentMode.HTML)
                    .withStyleName(ValoTheme.LABEL_SMALL);
            homepageLbl.setDescription(AppContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE));
            footer.addComponent(homepageLbl);
        }

        if (project.getNumActiveMembers() > 0) {
            Label activeMembersLbl = new ELabel(FontAwesome.USERS.getHtml() + " " + project.getNumActiveMembers(),
                    ContentMode.HTML).withDescription("Active members").withStyleName(ValoTheme.LABEL_SMALL);
            footer.addComponent(activeMembersLbl);
        }

        Label createdTimeLbl = new ELabel(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime(project.getCreatedtime()),
                ContentMode.HTML).withDescription("Created time").withStyleName(ValoTheme.LABEL_SMALL);
        footer.add(createdTimeLbl);

        billableHoursLbl = new ELabel(FontAwesome.MONEY.getHtml() + " " + NumberUtils.roundDouble(2, project.getTotalBillableHours()),
                ContentMode.HTML).withDescription("Billable hours").withStyleName(ValoTheme.LABEL_SMALL);
        footer.addComponent(billableHoursLbl);

        nonBillableHoursLbl = new ELabel(FontAwesome.GIFT.getHtml() + " " + project.getTotalNonBillableHours(),
                ContentMode.HTML).withDescription("Non billable hours").withStyleName(ValoTheme.LABEL_SMALL);
        footer.addComponent(nonBillableHoursLbl);

        headerLayout.with(headerLbl, footer);
        this.with(headerLayout).expand(headerLayout);

        MHorizontalLayout topPanel = new MHorizontalLayout().withMargin(false);
        this.with(topPanel).withAlign(topPanel, Alignment.TOP_RIGHT);
        if (project.isProjectArchived()) {
            Button activeProjectBtn = new Button(AppContext.getMessage(ProjectCommonI18nEnum.BUTTON_ACTIVE_PROJECT), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
                    project.setProjectstatus(OptionI18nEnum.StatusI18nEnum.Open.name());
                    projectService.updateSelectiveWithSession(project, AppContext.getUsername());

                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));

                }
            });
            activeProjectBtn.setStyleName(UIConstants.BUTTON_ACTION);
            topPanel.with(activeProjectBtn).withAlign(activeProjectBtn, Alignment.MIDDLE_RIGHT);
        } else {
            SearchTextField searchField = new SearchTextField() {
                public void doSearch(String value) {
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectSearchItemsView(ProjectInfoComponent.this, value));
                }
            };

            final PopupButton controlsBtn = new PopupButton();
            controlsBtn.setIcon(FontAwesome.ELLIPSIS_H);

            OptionPopupContent popupButtonsControl = new OptionPopupContent();

            Button createPhaseBtn = new Button(AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new MilestoneEvent.GotoAdd(ProjectInfoComponent.this, null));
                }
            });
            createPhaseBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));
            createPhaseBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE));
            popupButtonsControl.addOption(createPhaseBtn);

            Button createTaskBtn = new Button(AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(ProjectInfoComponent.this, null));
                }
            });
            createTaskBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            createTaskBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));
            popupButtonsControl.addOption(createTaskBtn);

            Button createBugBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_BUG), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
                }
            });
            createBugBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS));
            createBugBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
            popupButtonsControl.addOption(createBugBtn);

            Button createComponentBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_COMPONENT), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new BugComponentEvent.GotoAdd(this, null));
                }
            });
            createComponentBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.COMPONENTS));
            createComponentBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_COMPONENT));
            popupButtonsControl.addOption(createComponentBtn);

            Button createVersionBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_VERSION), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new BugVersionEvent.GotoAdd(this, null));
                }
            });
            createVersionBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS));
            createVersionBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_VERSION));
            popupButtonsControl.addOption(createVersionBtn);

            Button createRiskBtn = new Button(AppContext.getMessage(RiskI18nEnum.BUTTON_NEW_RISK),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(new RiskEvent.GotoAdd(this, null));
                        }
                    });
            createRiskBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.RISKS));
            createRiskBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));
            popupButtonsControl.addOption(createRiskBtn);

            Button createProblemBtn = new Button(AppContext.getMessage(ProblemI18nEnum.BUTTON_NEW_PROBLEM), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ProblemEvent.GotoAdd(this, null));
                }
            });
            createProblemBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PROBLEMS));
            createProblemBtn.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROBLEM));
            popupButtonsControl.addOption(createProblemBtn);

            popupButtonsControl.addSeparator();
            Button inviteMemberBtn = new Button(AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES), new
                    Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            controlsBtn.setPopupVisible(false);
                            EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null));
                        }
                    });
            inviteMemberBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS));
            inviteMemberBtn.setIcon(FontAwesome.SEND);
            popupButtonsControl.addOption(inviteMemberBtn);

            Button settingBtn = new Button(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ProjectNotificationEvent.GotoList(this, null));
                }
            });
            settingBtn.setIcon(FontAwesome.COG);
            popupButtonsControl.addOption(settingBtn);

            popupButtonsControl.addSeparator();

            Button editProjectBtn = new Button(AppContext.getMessage(ProjectCommonI18nEnum.BUTTON_EDIT_PROJECT), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoEdit(ProjectInfoComponent.this,
                            project));
                }
            });
            editProjectBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PROJECT));
            editProjectBtn.setIcon(FontAwesome.EDIT);
            popupButtonsControl.addOption(editProjectBtn);

            Button archiveProjectBtn = new Button(AppContext.getMessage(ProjectCommonI18nEnum.BUTTON_ARCHIVE_PROJECT), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    controlsBtn.setPopupVisible(false);
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE, AppContext.getSiteName()),
                            AppContext.getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_ARCHIVE_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
                                        project.setProjectstatus(OptionI18nEnum.StatusI18nEnum.Archived.name());
                                        projectService.updateSelectiveWithSession(project, AppContext.getUsername());

                                        PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(CurrentProjectVariables.getProjectId()));
                                        EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                                    }
                                }
                            });
                }
            });
            archiveProjectBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PROJECT));
            archiveProjectBtn.setIcon(FontAwesome.ARCHIVE);
            popupButtonsControl.addOption(archiveProjectBtn);

            if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PROJECT)) {
                popupButtonsControl.addSeparator();
                Button deleteProjectBtn = new Button(AppContext.getMessage(ProjectCommonI18nEnum.BUTTON_DELETE_PROJECT), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        controlsBtn.setPopupVisible(false);
                        ConfirmDialogExt.show(UI.getCurrent(),
                                AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                                AppContext.getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_DELETE_MESSAGE),
                                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(
                                            ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            ProjectService projectService = ApplicationContextUtil.getSpringBean(ProjectService.class);
                                            projectService.removeWithSession(CurrentProjectVariables.getProject(),
                                                    AppContext.getUsername(), AppContext.getAccountId());
                                            EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
                                        }
                                    }
                                });
                    }
                });
                deleteProjectBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PROJECT));
                deleteProjectBtn.setIcon(FontAwesome.TRASH_O);
                popupButtonsControl.addDangerOption(deleteProjectBtn);
            }

            controlsBtn.setContent(popupButtonsControl);
            controlsBtn.setWidthUndefined();

            topPanel.with(searchField, controlsBtn).withAlign(searchField, Alignment.TOP_RIGHT).withAlign(controlsBtn,
                    Alignment.TOP_RIGHT);
        }
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(timeLoggingChangedEventApplicationEventListener);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(timeLoggingChangedEventApplicationEventListener);
        super.detach();
    }
}
