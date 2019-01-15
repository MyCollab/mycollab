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

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.ProjectPageService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectLocalizationTypeMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.CssLayout;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MCssLayout;

import java.time.LocalDate;
import java.util.List;

import static com.mycollab.common.ActivityStreamConstants.*;
import static com.mycollab.html.DivLessFormatter.EMPTY_SPACE;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivityStreamComponent extends CssLayout {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(ActivityStreamComponent.class);

    private final ProjectActivityStreamPagedList2 activityStreamList;

    public ActivityStreamComponent() {
        this.activityStreamList = new ProjectActivityStreamPagedList2();
        setWidth("100%");
    }

    public void showFeeds(List<Integer> prjKeys) {
        this.removeAllComponents();
        if (CollectionUtils.isNotEmpty(prjKeys)) {
            addComponent(activityStreamList);
            ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
            searchCriteria.setModuleSet(new SetSearchField<>(ModuleNameConstants.PRJ));
            searchCriteria.setExtraTypeIds(new SetSearchField<>(prjKeys.toArray(new Integer[prjKeys.size()])));
            searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            activityStreamList.setSearchCriteria(searchCriteria);
        }
    }

    static class ProjectActivityStreamPagedList2 extends ProjectActivityStreamPagedList {
        private static final long serialVersionUID = 1L;

        public int setSearchCriteria(final ActivityStreamSearchCriteria searchCriteria) {
            this.removeAllComponents();
            searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, defaultNumberSearchItems);
            doSearch();
            return totalCount;
        }

        @Override
        protected void doSearch() {
            totalCount = projectActivityStreamService.getTotalActivityStream(((BasicSearchRequest<ActivityStreamSearchCriteria>) searchRequest).getSearchCriteria());
            totalPage = (totalCount - 1) / searchRequest.getNumberOfItems() + 1;
            if (searchRequest.getCurrentPage() > totalPage) {
                searchRequest.setCurrentPage(totalPage);
            }

            if (totalPage > 1) {
                if (controlBarWrapper != null) {
                    removeComponent(controlBarWrapper);
                }
                addComponent(createPageControls());
            } else {
                if (getComponentCount() == 2) {
                    removeComponent(getComponent(1));
                }
            }

            List<ProjectActivityStream> projectActivities = projectActivityStreamService.getProjectActivityStreams(
                    (BasicSearchRequest<ActivityStreamSearchCriteria>) searchRequest);
            this.removeAllComponents();

            LocalDate currentDate = LocalDate.of(2100, 1, 1);
            CssLayout currentFeedBlock = new CssLayout();
            AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);

            try {
                for (ProjectActivityStream projectActivity : projectActivities) {
                    if (ProjectTypeConstants.PAGE.equals(projectActivity.getType())) {
                        ProjectPageService pageService = AppContextUtil.getSpringBean(ProjectPageService.class);
                        Page page = pageService.getPage(projectActivity.getTypeid(), UserUIContext.getUsername());
                        if (page != null) {
                            projectActivity.setNamefield(page.getSubject());
                        }
                    }

                    LocalDate itemCreatedDate = projectActivity.getCreatedtime().toLocalDate();
                    if (!(currentDate.getYear() == itemCreatedDate.getYear())) {
                        currentFeedBlock = new CssLayout();
                        currentFeedBlock.setStyleName("feed-block");
                        feedBlocksPut(currentDate, itemCreatedDate, currentFeedBlock);
                        currentDate = itemCreatedDate;
                    }

                    StringBuilder content = new StringBuilder("");

                    // --------------Item hidden div tooltip----------------
                    String type = ProjectLocalizationTypeMap.getType(projectActivity.getType());
                    String assigneeValue = buildAssigneeValue(projectActivity);
                    String itemLink = buildItemValue(projectActivity);
                    String projectLink = buildProjectValue(projectActivity);

                    if (ACTION_CREATE.equals(projectActivity.getAction())) {
                        if (ProjectTypeConstants.PROJECT.equals(projectActivity.getType())) {
                            content.append(UserUIContext.getMessage(
                                    ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
                                    assigneeValue, type, projectLink));
                        } else {
                            content.append(UserUIContext.getMessage(
                                    ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
                                    assigneeValue, type, itemLink, projectLink));
                        }
                    } else if (ACTION_UPDATE.equals(projectActivity.getAction())) {
                        if (ProjectTypeConstants.PROJECT.equals(projectActivity.getType())) {
                            content.append(UserUIContext.getMessage(
                                    ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                                    assigneeValue, type, projectLink));
                        } else {
                            content.append(UserUIContext.getMessage(
                                    ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                                    assigneeValue, type, itemLink, projectLink));
                        }
                        if (projectActivity.getAssoAuditLog() != null) {
                            content.append(auditLogRegistry.generatorDetailChangeOfActivity(projectActivity));
                        }
                    } else if (ACTION_COMMENT.equals(projectActivity.getAction())) {
                        content.append(UserUIContext.getMessage(
                                ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_COMMENT_ACTION_TITLE,
                                assigneeValue, type, itemLink, projectLink));

                        if (projectActivity.getAssoAuditLog() != null) {
                            content.append("<ul><li>\"").append(StringUtils.trimHtmlTags(projectActivity.getAssoAuditLog().getChangeset(),
                                    200)).append("\"</li></ul>");
                        }
                    } else if (ActivityStreamConstants.ACTION_DELETE.equals(projectActivity.getAction())) {
                        if (ProjectTypeConstants.PROJECT.equals(projectActivity.getType())) {
                            content.append(UserUIContext.getMessage(
                                    ProjectCommonI18nEnum.FEED_USER_ACTIVITY_DELETE_ACTION_TITLE,
                                    assigneeValue, type, projectLink));
                        } else {
                            content.append(UserUIContext.getMessage(
                                    ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_DELETE_ACTION_TITLE,
                                    assigneeValue, type, itemLink, projectLink));
                        }
                    }

                    ELabel actionLbl = ELabel.html(content.toString()).withFullWidth();
                    MCssLayout streamWrapper = new MCssLayout(actionLbl).withFullWidth().withStyleName("stream-wrapper");
                    streamWrapper.addComponent(actionLbl);
                    currentFeedBlock.addComponent(streamWrapper);
                }
            } catch (final Exception e) {
                LOG.error("Error in tooltip", e);
            }
        }

        private String buildAssigneeValue(ProjectActivityStream activityStream) {
            DivLessFormatter div = new DivLessFormatter();
            Img userAvatar = new Img("", StorageUtils.getAvatarPath(activityStream.getCreatedUserAvatarId(), 16))
                    .setCSSClass(UIConstants.CIRCLE_BOX);
            A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).
                    setHref(ProjectLinkGenerator.generateProjectMemberLink(
                            activityStream.getExtratypeid(), activityStream.getCreateduser()));

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(activityStream.getCreateduser()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            userLink.appendText(StringUtils.trim(activityStream.getCreatedUserFullName(), 30, true));

            div.appendChild(userAvatar, EMPTY_SPACE, userLink);
            return div.write();
        }

        private String buildItemValue(ProjectActivityStream activityStream) {
            DivLessFormatter div = new DivLessFormatter();
            Text itemImg = new Text(ProjectAssetsManager.getAsset(activityStream.getType()).getHtml());
            A itemLink = new A();
            itemLink.setId("tag" + TooltipHelper.TOOLTIP_ID);

            if (ProjectTypeConstants.TASK.equals(activityStream.getType())
                    || ProjectTypeConstants.BUG.equals(activityStream.getType())) {
                itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(activityStream.getProjectShortName(),
                        activityStream.getExtratypeid(), activityStream.getType(), activityStream.getItemKey() + ""));
            } else {
                itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(activityStream.getProjectShortName(),
                        activityStream.getExtratypeid(), activityStream.getType(), activityStream.getTypeid()));
            }

            itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(activityStream.getType(), activityStream.getTypeid()));
            itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            itemLink.appendText(StringUtils.trim(activityStream.getNamefield(), 50, true));

            if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
                itemLink.setCSSClass(WebThemes.LINK_COMPLETED);
            }

            div.appendChild(itemImg, EMPTY_SPACE, itemLink);
            return div.write();
        }

        private String buildProjectValue(ProjectActivityStream activityStream) {
            DivLessFormatter div = new DivLessFormatter();
            Text prjImg = new Text(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT).getHtml());
            A prjLink = new A(ProjectLinkGenerator.generateProjectLink(activityStream.getProjectId())).setId("tag" + TooltipHelper.TOOLTIP_ID);
            prjLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.PROJECT,
                    activityStream.getProjectId() + ""));
            prjLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            prjLink.appendText(activityStream.getProjectName());

            div.appendChild(prjImg, EMPTY_SPACE, prjLink);

            return div.write();
        }

        @Override
        protected QueryHandler<ProjectActivityStream> buildQueryHandler() {
            return new QueryHandler<ProjectActivityStream>() {
            };
        }
    }
}
