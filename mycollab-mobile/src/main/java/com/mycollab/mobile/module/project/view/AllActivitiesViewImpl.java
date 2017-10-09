package com.mycollab.mobile.module.project.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.module.project.event.ProjectEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectLocalizationTypeMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class AllActivitiesViewImpl extends AbstractListPageView<ActivityStreamSearchCriteria, ProjectActivityStream> implements AllActivitiesView {
    private static final long serialVersionUID = -7722214412998470562L;

    public AllActivitiesViewImpl() {
        this.setCaption(UserUIContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES));
    }

    @Override
    protected AbstractPagedBeanList<ActivityStreamSearchCriteria, ProjectActivityStream> createBeanList() {
        ProjectActivitiesStreamListDisplay beanList = new ProjectActivitiesStreamListDisplay();
        beanList.setRowDisplayHandler(new ActivityStreamRowHandler());
        return beanList;
    }

    @Override
    protected SearchInputField<ActivityStreamSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected void buildNavigateMenu() {
        addSection("Views");

        // Buttons with styling (slightly smaller with left-aligned text)
        Button activityBtn = new Button("Activities", clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.GotoAllActivitiesView(this, null));
        });
        activityBtn.setIcon(FontAwesome.INBOX);
        addMenuItem(activityBtn);

        Button prjBtn = new Button(UserUIContext.getMessage(ProjectI18nEnum.LIST), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
        });
        prjBtn.setIcon(FontAwesome.BUILDING);
        addMenuItem(prjBtn);

        addSection("Modules");
        MButton crmModuleBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.MODULE_CRM), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
        }).withIcon(VaadinIcons.MONEY);
        addMenuItem(crmModuleBtn);

        addSection(UserUIContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));

        Button logoutBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), clickEvent -> {
            closeMenu();
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this));
        });
        logoutBtn.setIcon(FontAwesome.SIGN_OUT);
        addMenuItem(logoutBtn);
    }

    @Override
    protected Component buildRightComponent() {
        return null;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment("project/activities/", UserUIContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES));
    }

    private static class ActivityStreamRowHandler implements IBeanList.RowDisplayHandler<ProjectActivityStream> {

        @Override
        public Component generateRow(IBeanList<ProjectActivityStream> host, final ProjectActivityStream activityStream, int rowIndex) {
            CssLayout layout = new CssLayout();
            layout.addStyleName("activity-cell");
            StringBuilder content = new StringBuilder();
            String assigneeValue = buildAssigneeValue(activityStream);
            String itemLink = buildItemValue(activityStream);
            String projectLink = buildProjectValue(activityStream);
            String type = ProjectLocalizationTypeMap.getType(activityStream.getType());
            AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);

            if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream.getAction())) {
                if (ProjectTypeConstants.PROJECT.equals(activityStream.getType())) {
                    content.append(UserUIContext.getMessage(
                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
                            assigneeValue, type, projectLink));
                } else {
                    content.append(UserUIContext.getMessage(
                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
                            assigneeValue, type, itemLink, projectLink));
                }

            } else if (ActivityStreamConstants.ACTION_UPDATE.equals(activityStream.getAction())) {
                if (ProjectTypeConstants.PROJECT.equals(activityStream.getType())) {
                    content.append(UserUIContext.getMessage(
                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                            assigneeValue, type, projectLink));
                } else {
                    content.append(UserUIContext.getMessage(
                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                            assigneeValue, type, itemLink, projectLink));
                }
                if (activityStream.getAssoAuditLog() != null) {
                    content.append(auditLogRegistry.generatorDetailChangeOfActivity(activityStream));
                }
            } else if (ActivityStreamConstants.ACTION_COMMENT.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(
                        ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_COMMENT_ACTION_TITLE,
                        assigneeValue, type, itemLink, projectLink));

                if (activityStream.getAssoAuditLog() != null) {
                    content.append("<p><ul><li>\"").append(activityStream.getAssoAuditLog()
                            .getChangeset()).append("\"</li></ul></p>");
                }
            } else if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
                if (ProjectTypeConstants.PROJECT.equals(activityStream.getType())) {
                    content.append(UserUIContext.getMessage(
                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_DELETE_ACTION_TITLE,
                            assigneeValue, type, projectLink));
                } else {
                    content.append(UserUIContext.getMessage(
                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_DELETE_ACTION_TITLE,
                            assigneeValue, type, itemLink, projectLink));
                }
            }

            layout.addComponent(ELabel.html(content.toString()));
            return layout;
        }

    }

    private static String buildAssigneeValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Img userAvatar = new Img("", AppContextUtil.getSpringBean(AbstractStorageService.class)
                .getAvatarPath(activityStream.getCreatedUserAvatarId(), 16))
                .setCSSClass(UIConstants.CIRCLE_BOX);
        A userLink = new A().setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                activityStream.getExtratypeid(), activityStream.getCreateduser()));
        userLink.appendText(StringUtils.trim(activityStream.getCreatedUserFullName(), 30, true));

        div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, userLink);
        return div.write();
    }

    private static String buildItemValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Text itemImg = new Text(ProjectAssetsManager.getAsset(activityStream.getType()).getHtml());
        A itemLink = new A();

        if (ProjectTypeConstants.TASK.equals(activityStream.getType())
                || ProjectTypeConstants.BUG.equals(activityStream.getType())) {
            itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(), activityStream.getItemKey() + ""));
        } else {
            itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(), activityStream.getTypeid()));
        }
        itemLink.appendText(StringUtils.trim(activityStream.getNamefield(), 50, true));

        div.appendChild(itemImg, DivLessFormatter.EMPTY_SPACE, itemLink);
        return div.write();
    }

    private static String buildProjectValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Text prjImg = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT).getHtml());
        A prjLink = new A(ProjectLinkBuilder.generateProjectFullLink(activityStream.getProjectId())).appendText(activityStream.getProjectName());
        div.appendChild(prjImg, DivLessFormatter.EMPTY_SPACE, prjLink);
        return div.write();
    }
}
