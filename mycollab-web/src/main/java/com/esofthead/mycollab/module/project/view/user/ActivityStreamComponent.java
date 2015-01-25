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

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectPageService;
import com.esofthead.mycollab.module.project.ui.components.ProjectAuditLogStreamGenerator;
import com.esofthead.mycollab.module.project.view.ProjectLocalizationTypeMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivityStreamComponent extends CssLayout {

    private static final long serialVersionUID = 1L;

    private final ProjectActivityStreamPagedList2 activityStreamList;

    public ActivityStreamComponent() {
        this.setStyleName("project-activity-list");
        this.activityStreamList = new ProjectActivityStreamPagedList2();
    }

    public void showFeeds(final List<Integer> prjKeys) {
        this.removeAllComponents();
        this.addComponent(this.activityStreamList);

        final ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>(SearchField.AND,
                new String[]{ModuleNameConstants.PRJ}));
        searchCriteria.setExtraTypeIds(new SetSearchField<>(prjKeys
                .toArray(new Integer[prjKeys.size()])));
        searchCriteria.setSaccountid(new NumberSearchField(AppContext
                .getAccountId()));
        this.activityStreamList.setSearchCriteria(searchCriteria);

    }

    static class ProjectActivityStreamPagedList2 extends
            ProjectActivityStreamPagedList {
        private static final long serialVersionUID = 1L;

        @Override
        public void doSearch() {
            this.totalCount = this.projectActivityStreamService
                    .getTotalActivityStream(this.searchRequest
                            .getSearchCriteria());
            this.totalPage = (this.totalCount - 1)
                    / this.searchRequest.getNumberOfItems() + 1;
            if (this.searchRequest.getCurrentPage() > this.totalPage) {
                this.searchRequest.setCurrentPage(this.totalPage);
            }

            if (totalPage > 1) {
                if (this.controlBarWrapper != null) {
                    this.removeComponent(this.controlBarWrapper);
                }
                this.addComponent(this.createPageControls());
            } else {
                if (getComponentCount() == 2) {
                    removeComponent(getComponent(1));
                }
            }

            final List<ProjectActivityStream> currentListData = this.projectActivityStreamService
                    .getProjectActivityStreams(this.searchRequest);
            this.listContainer.removeAllComponents();

            Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();

            CssLayout currentFeedBlock = new CssLayout();

            try {
                for (final ProjectActivityStream activityStream : currentListData) {
                    if (ProjectTypeConstants.PAGE.equals(activityStream
                            .getType())) {
                        ProjectPageService pageService = ApplicationContextUtil
                                .getSpringBean(ProjectPageService.class);
                        Page page = pageService.getPage(
                                activityStream.getTypeid(),
                                AppContext.getUsername());
                        if (page != null) {
                            activityStream.setNamefield(page.getSubject());
                        }
                    }

                    final Date itemCreatedDate = activityStream
                            .getCreatedtime();
                    if (!DateUtils.isSameDay(currentDate, itemCreatedDate)) {
                        currentFeedBlock = new CssLayout();
                        currentFeedBlock.setStyleName("feed-block");
                        feedBlocksPut(currentDate, itemCreatedDate,
                                currentFeedBlock);
                        currentDate = itemCreatedDate;
                    }

                    StringBuffer content = new StringBuffer("");

                    // --------------Item hidden div tooltip----------------
                    String type = AppContext
                            .getMessage(ProjectLocalizationTypeMap
                                    .getType(activityStream.getType()));
                    String assigneeValue = buildAssigneeValue(activityStream);
                    String itemLink = buildItemValue(activityStream);
                    String projectLink = buildProjectValue(activityStream);

                    if (ActivityStreamConstants.ACTION_CREATE
                            .equals(activityStream.getAction())) {
                        if (ProjectTypeConstants.PROJECT.equals(activityStream
                                .getType())) {
                            content.append(AppContext
                                    .getMessage(
                                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
                                            assigneeValue, type, projectLink));
                        } else {
                            content.append(AppContext
                                    .getMessage(
                                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
                                            assigneeValue, type, itemLink,
                                            projectLink));
                        }

                    } else if (ActivityStreamConstants.ACTION_UPDATE
                            .equals(activityStream.getAction())) {
                        if (ProjectTypeConstants.PROJECT.equals(activityStream
                                .getType())) {
                            content.append(AppContext
                                    .getMessage(
                                            ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                                            assigneeValue, type, projectLink));
                        } else {
                            content.append(AppContext
                                    .getMessage(
                                            ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                                            assigneeValue, type, itemLink,
                                            projectLink));
                        }
                        if (activityStream.getAssoAuditLog() != null) {
                            content.append(ProjectAuditLogStreamGenerator
                                    .generatorDetailChangeOfActivity(activityStream));
                        }
                    } else if (ActivityStreamConstants.ACTION_COMMENT
                            .equals(activityStream.getAction())) {
                        content.append(AppContext
                                .getMessage(
                                        ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_COMMENT_ACTION_TITLE,
                                        assigneeValue, type, itemLink,
                                        projectLink));

                        if (activityStream.getAssoAuditLog() != null) {
                            content.append("<p><ul><li>\"")
                                    .append(activityStream.getAssoAuditLog()
                                            .getChangeset())
                                    .append("\"</li></ul></p>");
                        }
                    }

                    final Label actionLbl = new Label(content.toString(),
                            ContentMode.HTML);
                    final CssLayout streamWrapper = new CssLayout();
                    streamWrapper.setWidth("100%");
                    streamWrapper.addStyleName("stream-wrapper");
                    streamWrapper.addComponent(actionLbl);
                    currentFeedBlock.addComponent(streamWrapper);
                }
            } catch (final Exception e) {
                throw new MyCollabException(e);
            }
        }

        private String buildAssigneeValue(ProjectActivityStream activityStream) {
            String uid = UUID.randomUUID().toString();
            DivLessFormatter div = new DivLessFormatter();
            Img userAvatar = new Img("", StorageManager.getAvatarLink(
                    activityStream.getCreatedUserAvatarId(), 16));
            A userLink = new A();
            userLink.setId("tag" + uid);
            userLink.setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    activityStream.getExtratypeid(),
                    activityStream.getCreateduser()));

            userLink.setAttribute("onmouseover", TooltipHelper.buildUserHtmlTooltip(uid, activityStream.getCreateduser()));

            String arg10 = activityStream.getCreatedUserFullName();
            userLink.appendText(arg10);

            div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            return div.write();
        }

        private String buildItemValue(ProjectActivityStream activityStream) {
            String uid = UUID.randomUUID().toString();
            DivLessFormatter div = new DivLessFormatter();
            Img itemImg = new Img("",
                    ProjectResources.getResourceLink(activityStream.getType()));
            A itemLink = new A();
            itemLink.setId("tag" + uid);

            if (ProjectTypeConstants.TASK.equals(activityStream.getType())
                    || ProjectTypeConstants.BUG
                    .equals(activityStream.getType())) {
                itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                        activityStream.getProjectShortName(),
                        activityStream.getExtratypeid(),
                        activityStream.getType(), activityStream.getItemKey()
                                + ""));
            } else {
                itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                        activityStream.getProjectShortName(),
                        activityStream.getExtratypeid(),
                        activityStream.getType(), activityStream.getTypeid()));
            }

            String arg17 = "'" + uid + "'";
            String arg18 = "'" + activityStream.getType() + "'";
            String arg19 = "'" + activityStream.getTypeid() + "'";
            String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
            String arg21 = "'" + activityStream.getSaccountid() + "'";
            String arg22 = "'" + AppContext.getSiteUrl() + "'";
            String arg23 = AppContext.getSession().getTimezone();
            String arg24 = "'" + AppContext.getUserLocale().toString() + "'";

            String mouseOverFunc = String.format(
                    "return projectOverViewOverIt(%s,%s,%s,%s,%s,%s,%s,%s);",
                    arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
            itemLink.setAttribute("onmouseover", mouseOverFunc);
            itemLink.appendText(activityStream.getNamefield());

            div.appendChild(itemImg, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            return div.write();
        }

        private String buildProjectValue(ProjectActivityStream activityStream) {
            String uid = UUID.randomUUID().toString();
            DivLessFormatter div = new DivLessFormatter();
            Img prjImg = new Img("",
                    MyCollabResource
                            .newResourceLink(WebResourceIds._16_project_project));
            A prjLink = new A(
                    ProjectLinkBuilder.generateProjectFullLink(activityStream
                            .getProjectId()));
            prjLink.appendText(activityStream.getProjectName());

            div.appendChild(prjImg, DivLessFormatter.EMPTY_SPACE(), prjLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));

            return div.write();
        }

        @Override
        protected int queryTotalCount() {
            return 0;
        }

        @Override
        protected List<ProjectActivityStream> queryCurrentData() {
            return null;
        }
    }
}
