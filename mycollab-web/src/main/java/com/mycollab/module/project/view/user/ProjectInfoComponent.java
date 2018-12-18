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
package com.mycollab.module.project.view.user;

import com.google.common.base.MoreObjects;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.event.ProjectMemberEvent;
import com.mycollab.module.project.event.ProjectNotificationEvent;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.SearchTextField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public class ProjectInfoComponent extends MHorizontalLayout {

    public ProjectInfoComponent(SimpleProject project) {
        this.withMargin(false).withFullWidth();
        Component projectIcon = ProjectAssetsUtil.editableProjectLogoComp(project.getShortname(), project.getId(), project.getAvatarid(), 32);
        this.with(projectIcon).withAlign(projectIcon, Alignment.TOP_LEFT);

        ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadCrumb.setProject(project);
        MVerticalLayout headerLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, true, false, true));

        MHorizontalLayout footer = new MHorizontalLayout().withStyleName(UIConstants.META_INFO, WebThemes.FLEX_DISPLAY);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        headerLayout.with(breadCrumb, footer);

        if (project.getMemlead() != null) {
            Div leadAvatar = new DivLessFormatter().appendChild(new Img("", StorageUtils.getAvatarPath
                            (project.getLeadAvatarId(), 16)).setCSSClass(UIConstants.CIRCLE_BOX), DivLessFormatter.EMPTY_SPACE,
                    new A(ProjectLinkGenerator.generateProjectMemberLink(project.getId(), project.getMemlead()))
                            .appendText(StringUtils.trim(project.getLeadFullName(), 30, true)))
                    .setTitle(project.getLeadFullName());
            ELabel leadLbl = ELabel.html(UserUIContext.getMessage(ProjectI18nEnum.FORM_LEADER) + ": " + leadAvatar.write()).withUndefinedWidth();
            footer.with(leadLbl);
        }
        if (project.getHomepage() != null) {
            ELabel homepageLbl = ELabel.html(VaadinIcons.GLOBE.getHtml() + " " + new A(project.getHomepage())
                    .appendText(project.getHomepage()).setTarget("_blank").write())
                    .withStyleName(ValoTheme.LABEL_SMALL).withUndefinedWidth();
            homepageLbl.setDescription(UserUIContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE));
        }

        if (project.getAccountid() != null && !SiteConfiguration.isCommunityEdition()) {
            Div clientDiv = new Div();
            if (project.getClientAvatarId() == null) {
                clientDiv.appendText(VaadinIcons.INSTITUTION.getHtml() + " ");
            } else {
                Img clientImg = new Img("", StorageUtils.getEntityLogoPath(AppUI.getAccountId(), project.getClientAvatarId(), 16))
                        .setCSSClass(UIConstants.CIRCLE_BOX);
                clientDiv.appendChild(clientImg).appendChild(DivLessFormatter.EMPTY_SPACE);
            }
            clientDiv.appendChild(new A(ProjectLinkGenerator.generateClientPreviewLink(project.getAccountid()))
                    .appendText(StringUtils.trim(project.getClientName(), 30, true)));
            ELabel accountBtn = ELabel.html(clientDiv.write()).withStyleName(WebThemes.BUTTON_LINK)
                    .withUndefinedWidth();
            footer.addComponents(accountBtn);
        }

        if (!SiteConfiguration.isCommunityEdition()) {
            MButton tagBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_TAG), clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoTagListView(this, null)))
                    .withIcon(VaadinIcons.TAGS).withStyleName(WebThemes.BUTTON_SMALL_PADDING, WebThemes.BUTTON_LINK);
            footer.addComponents(tagBtn);

            MButton favoriteBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_FAVORITES),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoFavoriteView(this, null)))
                    .withIcon(VaadinIcons.STAR).withStyleName(WebThemes.BUTTON_SMALL_PADDING, WebThemes.BUTTON_LINK);
            footer.addComponents(favoriteBtn);
        }

        MHorizontalLayout topPanel = new MHorizontalLayout().withMargin(false);
        this.with(headerLayout, topPanel).expand(headerLayout).withAlign(topPanel, Alignment.TOP_RIGHT);

        if (project.isProjectArchived()) {
            MButton activeProjectBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_ACTIVE_PROJECT), clickEvent -> {
                ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
                project.setProjectstatus(StatusI18nEnum.Open.name());
                projectService.updateSelectiveWithSession(project, UserUIContext.getUsername());

                PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(CurrentProjectVariables.getProjectId()));
                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
            }).withStyleName(WebThemes.BUTTON_ACTION);
            topPanel.with(activeProjectBtn).withAlign(activeProjectBtn, Alignment.MIDDLE_RIGHT);
        } else {
            SearchTextField searchField = new SearchTextField() {
                public void doSearch(String value) {
                    ProjectView prjView = UIUtils.getRoot(this, ProjectView.class);
                    if (prjView != null) {
                        prjView.displaySearchResult(value);
                    }
                }

                @Override
                public void emptySearch() {

                }
            };

            final PopupButton controlsBtn = new PopupButton();
            controlsBtn.addStyleName(WebThemes.BOX);
            controlsBtn.setIcon(VaadinIcons.ELLIPSIS_H);

            OptionPopupContent popupButtonsControl = new OptionPopupContent();

            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.USERS)) {
                MButton inviteMemberBtn = new MButton(UserUIContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES), clickEvent -> {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ProjectMemberEvent.GotoInviteMembers(this, null));
                }).withIcon(VaadinIcons.PAPERPLANE);
                popupButtonsControl.addOption(inviteMemberBtn);
            }

            MButton settingBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS), clickEvent -> {
                controlsBtn.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ProjectNotificationEvent.GotoList(this, null));
            }).withIcon(VaadinIcons.COG);
            popupButtonsControl.addOption(settingBtn);

            popupButtonsControl.addSeparator();

            if (UserUIContext.canAccess(RolePermissionCollections.CREATE_NEW_PROJECT)) {
                final MButton markProjectTemplateBtn = new MButton().withIcon(VaadinIcons.ANCHOR);
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
                popupButtonsControl.addOption(markProjectTemplateBtn);
            }

            if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PROJECT)) {
                MButton editProjectBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.EDIT), clickEvent -> {
                    controlsBtn.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoEdit(ProjectInfoComponent.this, project));
                }).withIcon(VaadinIcons.EDIT);
                popupButtonsControl.addOption(editProjectBtn);
            }

            if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PROJECT)) {
                MButton archiveProjectBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_ARCHIVE_PROJECT), clickEvent -> {
                    controlsBtn.setPopupVisible(false);
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE, AppUI.getSiteName()),
                            UserUIContext.getMessage(ProjectCommonI18nEnum.DIALOG_CONFIRM_PROJECT_ARCHIVE_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                            UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
                                    project.setProjectstatus(StatusI18nEnum.Archived.name());
                                    projectService.updateSelectiveWithSession(project, UserUIContext.getUsername());

                                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(CurrentProjectVariables.getProjectId()));
                                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                                }
                            });
                }).withIcon(VaadinIcons.ARCHIVE);
                popupButtonsControl.addOption(archiveProjectBtn);
            }

            if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.PROJECT)) {
                popupButtonsControl.addSeparator();
                MButton deleteProjectBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.BUTTON_DELETE_PROJECT), clickEvent -> {
                    controlsBtn.setPopupVisible(false);
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
                }).withIcon(VaadinIcons.TRASH);
                popupButtonsControl.addDangerOption(deleteProjectBtn);
            }

            controlsBtn.setContent(popupButtonsControl);
            controlsBtn.setWidthUndefined();

            topPanel.with(searchField, controlsBtn).withAlign(searchField, Alignment.TOP_RIGHT).withAlign(controlsBtn,
                    Alignment.TOP_RIGHT);
        }
    }
}
