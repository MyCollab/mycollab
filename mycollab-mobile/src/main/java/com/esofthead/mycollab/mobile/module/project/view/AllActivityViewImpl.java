/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.SearchInputField;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectAuditLogStreamGenerator;
import com.esofthead.mycollab.module.project.view.ProjectLocalizationTypeMap;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class AllActivityViewImpl extends AbstractListPageView<ActivityStreamSearchCriteria, ProjectActivityStream> implements AllActivityView {
    private static final long serialVersionUID = -7722214412998470562L;

    public AllActivityViewImpl() {
        this.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES));
    }

    @Override
    protected AbstractPagedBeanList<ActivityStreamSearchCriteria, ProjectActivityStream> createBeanList() {
        ProjectActivityStreamListDisplay beanList = new ProjectActivityStreamListDisplay();
        beanList.setRowDisplayHandler(new ActivityStreamRowHandler());
        return beanList;
    }

    @Override
    protected SearchInputField<ActivityStreamSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected void buildNavigateMenu() {
        addSection("Views:");

        // Buttons with styling (slightly smaller with left-aligned text)
        Button activityBtn = new Button("Activities", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new ProjectEvent.AllActivities(this));
            }
        });
        activityBtn.setIcon(FontAwesome.INBOX);
        addMenuItem(activityBtn);

        Button prjBtn = new Button("Projects", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
            }
        });
        prjBtn.setIcon(FontAwesome.BUILDING);
        addMenuItem(prjBtn);

        addSection("Settings:");

        Button logoutBtn = new Button("Logout", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeMenu();
                EventBusFactory.getInstance().post(new ShellEvent.LogOut(this));
            }
        });
        logoutBtn.setIcon(FontAwesome.SIGN_OUT);
        addMenuItem(logoutBtn);
    }

    @Override
    protected Component buildRightComponent() {
        return null;
    }

    private static class ActivityStreamRowHandler implements RowDisplayHandler<ProjectActivityStream> {

        @Override
        public Component generateRow(final ProjectActivityStream activityStream, int rowIndex) {
            CssLayout layout = new CssLayout();
            layout.addStyleName("activity-cell");
            StringBuilder content = new StringBuilder();
            String assigneeValue = buildAssigneeValue(activityStream);
            String itemLink = buildItemValue(activityStream);
            String projectLink = buildProjectValue(activityStream);
            String type = AppContext.getMessage(ProjectLocalizationTypeMap.getType(activityStream.getType()));

            if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream.getAction())) {
                if (ProjectTypeConstants.PROJECT.equals(activityStream.getType())) {
                    content.append(AppContext.getMessage(
                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
                            assigneeValue, type, projectLink));
                } else {
                    content.append(AppContext.getMessage(
                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
                            assigneeValue, type, itemLink, projectLink));
                }

            } else if (ActivityStreamConstants.ACTION_UPDATE.equals(activityStream.getAction())) {
                if (ProjectTypeConstants.PROJECT.equals(activityStream.getType())) {
                    content.append(AppContext.getMessage(
                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                            assigneeValue, type, projectLink));
                } else {
                    content.append(AppContext.getMessage(
                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                            assigneeValue, type, itemLink, projectLink));
                }
                if (activityStream.getAssoAuditLog() != null) {
                    content.append(ProjectAuditLogStreamGenerator
                            .generatorDetailChangeOfActivity(activityStream));
                }
            } else if (ActivityStreamConstants.ACTION_COMMENT.equals(activityStream.getAction())) {
                content.append(AppContext.getMessage(
                        ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_COMMENT_ACTION_TITLE,
                        assigneeValue, type, itemLink, projectLink));

                if (activityStream.getAssoAuditLog() != null) {
                    content.append("<p><ul><li>\"").append(activityStream.getAssoAuditLog()
                            .getChangeset()).append("\"</li></ul></p>");
                }
            } else if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
                if (ProjectTypeConstants.PROJECT.equals(activityStream.getType())) {
                    content.append(AppContext.getMessage(
                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_DELETE_ACTION_TITLE,
                            assigneeValue, type, projectLink));
                } else {
                    content.append(AppContext.getMessage(
                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_DELETE_ACTION_TITLE,
                            assigneeValue, type, itemLink, projectLink));
                }
            }

            Label actionLbl = new Label(content.toString(), ContentMode.HTML);
            layout.addComponent(actionLbl);
            return layout;
        }

    }

    private static String buildAssigneeValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Img userAvatar = new Img("", StorageFactory.getInstance().getAvatarPath(activityStream.getCreatedUserAvatarId(), 16));
        A userLink = new A().setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                activityStream.getExtratypeid(), activityStream.getCreateduser()));
        userLink.appendText(StringUtils.trim(activityStream.getCreatedUserFullName(), 30, true));

        div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink);
        return div.write();
    }

    private static String buildItemValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Text itemImg = new Text(ProjectAssetsManager.getAsset(activityStream.getType()).getHtml());
        A itemLink = new A();

        if (ProjectTypeConstants.TASK.equals(activityStream.getType())
                || ProjectTypeConstants.BUG.equals(activityStream.getType())) {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(), activityStream.getItemKey() + ""));
        } else {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(), activityStream.getTypeid()));
        }
        itemLink.appendText(StringUtils.trim(activityStream.getNamefield(), 50, true));

        div.appendChild(itemImg, DivLessFormatter.EMPTY_SPACE(), itemLink);
        return div.write();
    }

    private static String buildProjectValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Text prjImg = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT).getHtml());
        A prjLink = new A(ProjectLinkBuilder.generateProjectFullLink(activityStream.getProjectId())).appendText(activityStream.getProjectName());
        div.appendChild(prjImg, DivLessFormatter.EMPTY_SPACE(), prjLink);
        return div.write();
    }
}
