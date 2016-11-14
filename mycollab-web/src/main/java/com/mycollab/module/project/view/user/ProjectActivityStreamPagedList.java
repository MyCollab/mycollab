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
package com.mycollab.module.project.view.user;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.ActivityStreamConstants;
import com.mycollab.common.domain.SimpleActivityStream;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.ProjectActivityStreamService;
import com.mycollab.module.project.service.ProjectPageService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.ProjectLocalizationTypeMap;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import com.mycollab.vaadin.web.ui.AbstractBeanPagedList;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

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
        listContainer.removeAllComponents();
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

        List<ProjectActivityStream> currentListData = projectActivityStreamService.getProjectActivityStreams((BasicSearchRequest<ActivityStreamSearchCriteria>) searchRequest);
        this.listContainer.removeAllComponents();
        Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();

        CssLayout currentFeedBlock = new CssLayout();
        AuditLogRegistry auditLogRegistry = AppContextUtil.getSpringBean(AuditLogRegistry.class);

        try {
            for (ProjectActivityStream activityStream : currentListData) {
                if (ProjectTypeConstants.PAGE.equals(activityStream.getType())) {
                    ProjectPageService pageService = AppContextUtil.getSpringBean(ProjectPageService.class);
                    Page page = pageService.getPage(activityStream.getTypeid(), UserUIContext.getUsername());
                    if (page != null) {
                        activityStream.setNamefield(page.getSubject());
                    }
                }

                Date itemCreatedDate = activityStream.getCreatedtime();

                if (!DateUtils.isSameDay(currentDate, itemCreatedDate)) {
                    currentFeedBlock = new CssLayout();
                    currentFeedBlock.setStyleName("feed-block");
                    feedBlocksPut(currentDate, itemCreatedDate, currentFeedBlock);
                    currentDate = itemCreatedDate;
                }
                StringBuilder content = new StringBuilder();
                String itemType = ProjectLocalizationTypeMap.getType(activityStream.getType());
                String assigneeParam = buildAssigneeValue(activityStream);
                String itemParam = buildItemValue(activityStream);

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
                        content.append("<p><ul><li>\"").append(
                                StringUtils.trimHtmlTags(activityStream.getAssoAuditLog().getChangeset(), 200)).append("\"</li></ul></p>");
                    }
                } else if (ActivityStreamConstants.ACTION_DELETE.equals(activityStream.getAction())) {
                    content.append(UserUIContext.getMessage(ProjectCommonI18nEnum.FEED_USER_ACTIVITY_DELETE_ACTION_TITLE,
                            assigneeParam, itemType, itemParam));
                }
                Label actionLbl = new Label(content.toString(), ContentMode.HTML);
                CssLayout streamWrapper = new CssLayout();
                streamWrapper.setWidth("100%");
                streamWrapper.addStyleName("stream-wrapper");
                streamWrapper.addComponent(actionLbl);
                currentFeedBlock.addComponent(streamWrapper);
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    private String buildAssigneeValue(SimpleActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Img userAvatar = new Img("", StorageFactory.getAvatarPath(activityStream.getCreatedUserAvatarId(), 16))
                .setCSSClass(UIConstants.CIRCLE_BOX);
        A userLink = new A().setId("tag" + TOOLTIP_ID).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                activityStream.getExtratypeid(), activityStream.getCreateduser()));

        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(activityStream.getCreateduser()));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        userLink.appendText(StringUtils.trim(activityStream.getCreatedUserFullName(), 30, true));

        div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink);

        return div.write();
    }

    private String buildItemValue(ProjectActivityStream activityStream) {
        DivLessFormatter div = new DivLessFormatter();
        Text image = new Text(ProjectAssetsManager.getAsset(activityStream.getType()).getHtml());
        A itemLink = new A().setId("tag" + TOOLTIP_ID);
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

        div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink);
        return div.write();
    }

    protected void feedBlocksPut(Date currentDate, Date nextDate, ComponentContainer currentBlock) {
        MHorizontalLayout blockWrapper = new MHorizontalLayout().withSpacing(false).withFullWidth().withStyleName
                ("feed-block-wrap");

        blockWrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(nextDate);

        if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
            int currentYear = cal2.get(Calendar.YEAR);
            Label yearLbl = new Label("<div>" + String.valueOf(currentYear) + "</div>", ContentMode.HTML);
            yearLbl.setStyleName("year-lbl");
            yearLbl.setWidthUndefined();
            listContainer.addComponent(yearLbl);
        } else {
            blockWrapper.setMargin(new MarginInfo(true, false, false, false));
        }
        Label dateLbl = new Label(UserUIContext.formatShortDate(nextDate));
        dateLbl.setStyleName("date-lbl");
        dateLbl.setWidthUndefined();
        blockWrapper.with(dateLbl, currentBlock).expand(currentBlock);

        this.listContainer.addComponent(blockWrapper);
    }

    @Override
    protected MHorizontalLayout createPageControls() {
        this.controlBarWrapper = new MHorizontalLayout().withFullHeight().withStyleName("page-controls");
        ButtonGroup controlBtns = new ButtonGroup();
        controlBtns.setStyleName(WebThemes.BUTTON_ACTION);
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

        controlBtns.addButton(prevBtn);
        controlBtns.addButton(nextBtn);

        controlBarWrapper.addComponent(controlBtns);
        return controlBarWrapper;
    }

    @Override
    protected QueryHandler<ProjectActivityStream> buildQueryHandler() {
        return new QueryHandler<ProjectActivityStream>() {
            @Override
            public int queryTotalCount() {
                return 0;
            }

            @Override
            public List<ProjectActivityStream> queryCurrentData() {
                return null;
            }
        };
    }
}