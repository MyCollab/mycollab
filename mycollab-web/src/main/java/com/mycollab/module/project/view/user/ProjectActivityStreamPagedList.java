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
import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.ProjectActivityStreamService;
import com.mycollab.module.project.service.ProjectPageService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectLocalizationTypeMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.mycollab.vaadin.web.ui.AbstractBeanPagedList;
import com.mycollab.vaadin.web.ui.ButtonGroup;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.time.LocalDate;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectActivityStreamPagedList extends AbstractBeanPagedList<ProjectActivityStream> {
    private static final long serialVersionUID = 1L;

    protected ProjectActivityStreamService projectActivityStreamService;

    public ProjectActivityStreamPagedList() {
        super(null, 20);
        this.setStyleName("activity-list");
        projectActivityStreamService = AppContextUtil.getSpringBean(ProjectActivityStreamService.class);
    }

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
            this.addComponent(createPageControls());
        } else {
            if (getComponentCount() == 2) {
                removeComponent(getComponent(1));
            }
        }

        List<ProjectActivityStream> projectActivities = projectActivityStreamService.getProjectActivityStreams((BasicSearchRequest<ActivityStreamSearchCriteria>) searchRequest);
        this.removeAllComponents();
        LocalDate currentDate = LocalDate.of(2100, 1, 1);

        CssLayout currentFeedBlock = new CssLayout();
        AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);

        try {
            for (ProjectActivityStream activity : projectActivities) {
                if (ProjectTypeConstants.PAGE.equals(activity.getType())) {
                    ProjectPageService pageService = AppContextUtil.getSpringBean(ProjectPageService.class);
                    Page page = pageService.getPage(activity.getTypeid(), UserUIContext.getUsername());
                    if (page != null) {
                        activity.setNamefield(page.getSubject());
                    }
                }

                LocalDate itemCreatedDate = activity.getCreatedtime().toLocalDate();

                if (!currentDate.isEqual(itemCreatedDate)) {
                    currentFeedBlock = new CssLayout();
                    currentFeedBlock.setStyleName("feed-block");
                    feedBlocksPut(currentDate, itemCreatedDate, currentFeedBlock);
                    currentDate = itemCreatedDate;
                }
                StringBuilder content = new StringBuilder();
                String itemType = ProjectLocalizationTypeMap.getType(activity.getType());
                String assigneeParam = buildAssigneeValue(activity);
                String itemParam = buildItemValue(activity);

                if (ActivityStreamConstants.ACTION_CREATE.equals(activity.getAction())) {
                    content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
                            assigneeParam, itemType, itemParam));
                } else if (ActivityStreamConstants.ACTION_UPDATE.equals(activity.getAction())) {
                    content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                            assigneeParam, itemType, itemParam));
                    if (activity.getAssoAuditLog() != null) {
                        content.append(auditLogRegistry.generatorDetailChangeOfActivity(activity));
                    }
                } else if (ActivityStreamConstants.ACTION_COMMENT.equals(activity.getAction())) {
                    content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_COMMENT_ACTION_TITLE,
                            assigneeParam, itemType, itemParam));
                    if (activity.getAssoAuditLog() != null) {
                        content.append("<ul><li>\"").append(
                                StringUtils.trimHtmlTags(activity.getAssoAuditLog().getChangeset(), 200)).append("\"</li></ul>");
                    }
                } else if (ActivityStreamConstants.ACTION_DELETE.equals(activity.getAction())) {
                    content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_DELETE_ACTION_TITLE,
                            assigneeParam, itemType, itemParam));
                }
                ELabel actionLbl = ELabel.html(content.toString()).withFullWidth();
                MCssLayout streamWrapper = new MCssLayout(actionLbl).withFullWidth().withStyleName("stream-wrapper");
                currentFeedBlock.addComponent(streamWrapper);
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private String buildAssigneeValue(SimpleActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Img userAvatar = new Img("", StorageUtils.getAvatarPath(activityStream.getCreatedUserAvatarId(), 16))
                .setCSSClass(WebThemes.CIRCLE_BOX);
        A userLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID).setHref(ProjectLinkGenerator.generateProjectMemberLink(
                activityStream.getExtratypeid(), activityStream.getCreateduser()));

        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(activityStream.getCreateduser()));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        userLink.appendText(StringUtils.trim(activityStream.getCreatedUserFullName(), 30, true));

        div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE, userLink);

        return div.write();
    }

    private String buildItemValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Text image = new Text(ProjectAssetsManager.getAsset(activityStream.getType()).getHtml());
        A itemLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID);
        if (ProjectTypeConstants.TASK.equals(activityStream.getType())
                || ProjectTypeConstants.BUG.equals(activityStream.getType())) {
            itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(
                    activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(),
                    activityStream.getItemKey() + ""));
        } else {
            itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(
                    activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(),
                    activityStream.getTypeid()));
        }

        itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(activityStream.getType(),
                activityStream.getTypeid()));
        itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        itemLink.appendText(StringUtils.trim(activityStream.getNamefield(), 50, true));

        if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
            itemLink.setCSSClass(WebThemes.LINK_COMPLETED);
        }

        div.appendChild(image, DivLessFormatter.EMPTY_SPACE, itemLink);
        return div.write();
    }

    protected void feedBlocksPut(LocalDate currentDate, LocalDate nextDate, ComponentContainer currentBlock) {
        MHorizontalLayout blockWrapper = new MHorizontalLayout().withSpacing(false).withFullWidth().withStyleName
                ("feed-block-wrap");

        blockWrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        if (currentDate.getYear() != nextDate.getYear()) {
            int currentYear = nextDate.getYear();
            ELabel yearLbl = ELabel.html("<div>" + currentYear + "</div>").withStyleName("year-lbl").withUndefinedWidth();
            this.addComponent(yearLbl);
        } else {
            blockWrapper.setMargin(new MarginInfo(true, false, false, false));
        }
        ELabel dateLbl = new ELabel(UserUIContext.formatShortDate(nextDate)).withStyleName("date-lbl").withUndefinedWidth();
        blockWrapper.with(dateLbl, currentBlock).expand(currentBlock);

        this.addComponent(blockWrapper);
    }

    @Override
    protected MHorizontalLayout createPageControls() {
        controlBarWrapper = new MHorizontalLayout().withFullHeight().withStyleName("page-controls");

        MButton prevBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_NAV_NEWER), clickEvent -> pageChange(currentPage - 1))
                .withWidth("64px").withStyleName(WebThemes.BUTTON_ACTION);
        if (currentPage == 1) {
            prevBtn.setEnabled(false);
        }

        MButton nextBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_NAV_OLDER), clickEvent -> pageChange(currentPage + 1))
                .withWidth("64px").withStyleName(WebThemes.BUTTON_ACTION);
        if (currentPage == totalPage) {
            nextBtn.setEnabled(false);
        }

        ButtonGroup controlBtns = new ButtonGroup(prevBtn, nextBtn);
        controlBtns.setStyleName(WebThemes.BUTTON_ACTION);

        controlBarWrapper.addComponent(controlBtns);
        return controlBarWrapper;
    }

    @Override
    protected QueryHandler<ProjectActivityStream> buildQueryHandler() {
        return new QueryHandler<ProjectActivityStream>() {
        };
    }
}