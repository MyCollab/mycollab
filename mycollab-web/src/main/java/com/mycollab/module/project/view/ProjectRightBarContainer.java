package com.mycollab.module.project.view;

import com.google.common.base.MoreObjects;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.event.ProjectNotificationEvent;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import org.vaadin.addons.stackpanel.StackPanel;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class ProjectRightBarContainer extends MVerticalLayout {

    private SimpleProject project;

    private MVerticalLayout modulePanel;

    public ProjectRightBarContainer(SimpleProject project) {
        this.project = project;

        this.setStyleName("project-right-bar");
        this.setHeight("100%");
        modulePanel = new MVerticalLayout().withMargin(false);
        this.with(buildProjectActionPanel(), modulePanel).expand(modulePanel);
    }

    private Panel buildProjectActionPanel() {
        Panel projectActionPanel = new Panel(UserUIContext.getMessage(GenericI18Enum.OPT_ACTIONS));
        MVerticalLayout projectActionLayout = new MVerticalLayout();

        if (project.isProjectArchived()) {
            MButton activeProjectBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_ACTIVE_PROJECT), clickEvent -> {
                ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
                project.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
                projectService.updateSelectiveWithSession(project, UserUIContext.getUsername());

                PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(CurrentProjectVariables.getProjectId()));
                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
            }).withStyleName(WebThemes.BUTTON_LINK);
            projectActionLayout.with(activeProjectBtn);
        }

        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS)) {
            MButton inviteMemberBtn = new MButton(UserUIContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES), clickEvent -> {
                EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null));
            }).withIcon(VaadinIcons.PAPERPLANE).withStyleName(WebThemes.BUTTON_LINK);
            projectActionLayout.with(inviteMemberBtn);
        }

        MButton settingBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS), clickEvent -> {
            EventBusFactory.getInstance().post(new ProjectNotificationEvent.GotoList(this, null));
        }).withIcon(VaadinIcons.COG).withStyleName(WebThemes.BUTTON_LINK);
        projectActionLayout.with(settingBtn);

        if (UserUIContext.canAccess(RolePermissionCollections.CREATE_NEW_PROJECT)) {
            final MButton markProjectTemplateBtn = new MButton().withIcon(VaadinIcons.ANCHOR).withStyleName(WebThemes.BUTTON_LINK);
            markProjectTemplateBtn.addClickListener(clickEvent -> {
                Boolean isTemplate = !MoreObjects.firstNonNull(project.getIstemplate(), Boolean.FALSE);
                project.setIstemplate(isTemplate);
                ProjectService prjService = AppContextUtil.getSpringBean(ProjectService.class);
                prjService.updateWithSession(project, UserUIContext.getUsername());
                if (project.getIstemplate()) {
                    markProjectTemplateBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_UNMARK_TEMPLATE));
                } else {
                    markProjectTemplateBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_MARK_TEMPLATE));
                }
            });

            Boolean isTemplate = MoreObjects.firstNonNull(project.getIstemplate(), Boolean.FALSE);
            if (isTemplate) {
                markProjectTemplateBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_UNMARK_TEMPLATE));
            } else {
                markProjectTemplateBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_MARK_TEMPLATE));
            }
            projectActionLayout.with(markProjectTemplateBtn);
        }

        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PROJECT)) {
            MButton editProjectBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.EDIT), clickEvent -> {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoEdit(ProjectRightBarContainer.this, project));
            }).withIcon(VaadinIcons.EDIT).withStyleName(WebThemes.BUTTON_LINK);
            projectActionLayout.with(editProjectBtn);
        }

        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PROJECT)) {
            MButton archiveProjectBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_ARCHIVE_PROJECT), clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_ARCHIVE_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
                                project.setStatus(OptionI18nEnum.StatusI18nEnum.Archived.name());
                                projectService.updateSelectiveWithSession(project, UserUIContext.getUsername());

                                PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(CurrentProjectVariables.getProjectId()));
                                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                            }
                        });
            }).withIcon(VaadinIcons.ARCHIVE).withStyleName(WebThemes.BUTTON_LINK);
            projectActionLayout.with(archiveProjectBtn);
        }

        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PROJECT)) {
            MButton deleteProjectBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_DELETE_PROJECT), clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_DELETE_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                        UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
                                projectService.removeWithSession(CurrentProjectVariables.getProject(),
                                        UserUIContext.getUsername(), AppUI.getAccountId());
                                EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
                            }
                        });
            }).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_LINK, WebThemes.DANGER);
            projectActionLayout.with(deleteProjectBtn);
        }

        projectActionPanel.setContent(projectActionLayout);
        StackPanel.extend(projectActionPanel);
        return projectActionPanel;
    }

    public void clearViewComponents() {
        modulePanel.removeAllComponents();
    }

    public void addViewComponent(Component component) {
        modulePanel.removeAllComponents();
        modulePanel.add(component);
    }
}
