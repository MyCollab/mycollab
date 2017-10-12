/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.ProjectActivityStreamService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectLocalizationTypeMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
class ProjectActivitiesStreamListDisplay extends AbstractPagedBeanList<ActivityStreamSearchCriteria, ProjectActivityStream> {
    private static final long serialVersionUID = 9189667863722393067L;

    private final ProjectActivityStreamService projectActivityStreamService;

    ProjectActivitiesStreamListDisplay() {
        super(new ActivityStreamRowHandler(), 20);
        projectActivityStreamService = AppContextUtil.getSpringBean(ProjectActivityStreamService.class);
    }

    @Override
    protected int queryTotalCount() {
        return projectActivityStreamService.getTotalActivityStream(searchRequest.getSearchCriteria());
    }

    @Override
    protected List<ProjectActivityStream> queryCurrentData() {
        return projectActivityStreamService.getProjectActivityStreams(searchRequest);
    }

    @Override
    protected void renderRows() {
        int i = 0;
        Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();
        for (final ProjectActivityStream item : currentListData) {
            if (!DateUtils.isSameDay(item.getCreatedtime(), currentDate)) {
                listContainer.addComponent(FormSectionBuilder.build(UserUIContext.formatDate(item.getCreatedtime())));
                currentDate = item.getCreatedtime();
            }
            final Component row = getRowDisplayHandler().generateRow(this, item, i);
            if (row != null) {
                listContainer.addComponent(row);
            }
            i++;
        }
    }

    private static class ActivityStreamRowHandler implements IBeanList.RowDisplayHandler<ProjectActivityStream> {

        @Override
        public Component generateRow(IBeanList<ProjectActivityStream> host, final ProjectActivityStream activityStream, int rowIndex) {
            AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);
            CssLayout layout = new CssLayout();
            layout.addStyleName("activity-cell");
            String itemType = ProjectLocalizationTypeMap.getType(activityStream.getType());
            String assigneeParam = buildAssigneeValue(activityStream);
            String itemParam = buildItemValue(activityStream);
            StringBuilder content = new StringBuilder();
            if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
                        assigneeParam, itemType, itemParam));
            } else if (ActivityStreamConstants.ACTION_UPDATE.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                        assigneeParam, itemType, itemParam));
                if (activityStream.getAssoAuditLog() != null) {
                    content.append(auditLogRegistry.generatorDetailChangeOfActivity(activityStream));
                }
            } else if (ActivityStreamConstants.ACTION_COMMENT.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_COMMENT_ACTION_TITLE,
                        assigneeParam, itemType, itemParam));
                if (activityStream.getAssoAuditLog() != null) {
                    content.append("<p><ul><li>\"").append(activityStream.getAssoAuditLog().getChangeset()).append("\"</li></ul></p>");
                }
            } else if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
                content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_DELETE_ACTION_TITLE,
                        assigneeParam, itemType, itemParam));
            }
            Label actionLbl = new Label(content.toString(), ContentMode.HTML);
            layout.addComponent(actionLbl);
            return layout;
        }

    }

    private static String buildAssigneeValue(ProjectActivityStream activity) {
        Img userAvatar = new Img("", AppContextUtil.getSpringBean(AbstractStorageService.class)
                .getAvatarPath(activity.getCreatedUserAvatarId(), 16))
                .setCSSClass(UIConstants.CIRCLE_BOX);
        A userLink = new A(ProjectLinkBuilder.generateProjectMemberFullLink(activity.getExtratypeid(), activity
                .getCreateduser())).appendText(StringUtils.trim(activity.getCreatedUserFullName(), 30, true));
        return new DivLessFormatter().appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, userLink).write();
    }

    private static String buildItemValue(ProjectActivityStream activity) {
        Text image = new Text(ProjectAssetsManager.getAsset(activity.getType()).getHtml());
        A itemLink = new A();
        if (ProjectTypeConstants.TASK.equals(activity.getType()) || ProjectTypeConstants.BUG.equals(activity.getType())) {
            itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(
                    activity.getProjectShortName(), activity.getExtratypeid(),
                    activity.getType(), activity.getItemKey() + ""));
        } else {
            itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(
                    activity.getProjectShortName(), activity.getExtratypeid(),
                    activity.getType(), activity.getTypeid()));
        }
        itemLink.appendText(StringUtils.trim(activity.getNamefield(), 30, true));
        return new DivLessFormatter().appendChild(image, DivLessFormatter.EMPTY_SPACE, itemLink).write();
    }

}
