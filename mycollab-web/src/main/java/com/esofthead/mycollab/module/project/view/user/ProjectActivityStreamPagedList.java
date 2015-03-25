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
import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.module.project.service.ProjectPageService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectAuditLogStreamGenerator;
import com.esofthead.mycollab.module.project.view.ProjectLocalizationTypeMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.peter.buttongroup.ButtonGroup;

import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectActivityStreamPagedList extends
        AbstractBeanPagedList<ActivityStreamSearchCriteria, ProjectActivityStream> {
    private static final long serialVersionUID = 1L;

    protected final ProjectActivityStreamService projectActivityStreamService;

    public ProjectActivityStreamPagedList() {
        super(null, 20);
        this.projectActivityStreamService = ApplicationContextUtil
                .getSpringBean(ProjectActivityStreamService.class);

    }

    @Override
    public void doSearch() {
        this.totalCount = this.projectActivityStreamService
                .getTotalActivityStream(this.searchRequest.getSearchCriteria());
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
                if (ProjectTypeConstants.PAGE.equals(activityStream.getType())) {
                    ProjectPageService pageService = ApplicationContextUtil
                            .getSpringBean(ProjectPageService.class);
                    Page page = pageService.getPage(activityStream.getTypeid(),
                            AppContext.getUsername());
                    if (page != null) {
                        activityStream.setNamefield(page.getSubject());
                    }
                }

                final Date itemCreatedDate = activityStream.getCreatedtime();

                if (!DateUtils.isSameDay(currentDate, itemCreatedDate)) {
                    currentFeedBlock = new CssLayout();
                    currentFeedBlock.setStyleName("feed-block");
                    feedBlocksPut(currentDate, itemCreatedDate,
                            currentFeedBlock);
                    currentDate = itemCreatedDate;
                }
                StringBuffer content = new StringBuffer();
                String itemType = AppContext
                        .getMessage(ProjectLocalizationTypeMap
                                .getType(activityStream.getType()));
                String assigneeParam = buildAssigneeValue(activityStream);
                String itemParam = buildItemValue(activityStream);

                if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream
                        .getAction())) {
                    content.append(AppContext
                            .getMessage(
                                    ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
                                    assigneeParam, itemType, itemParam));
                } else if (ActivityStreamConstants.ACTION_UPDATE
                        .equals(activityStream.getAction())) {
                    content.append(AppContext
                            .getMessage(
                                    ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
                                    assigneeParam, itemType, itemParam));
                    if (activityStream.getAssoAuditLog() != null) {
                        content.append(ProjectAuditLogStreamGenerator
                                .generatorDetailChangeOfActivity(activityStream));
                    }
                } else if (ActivityStreamConstants.ACTION_COMMENT
                        .equals(activityStream.getAction())) {
                    content.append(AppContext
                            .getMessage(
                                    ProjectCommonI18nEnum.FEED_USER_ACTIVITY_COMMENT_ACTION_TITLE,
                                    assigneeParam, itemType, itemParam));
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

    private String buildAssigneeValue(SimpleActivityStream activityStream) {
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
        userLink.appendText(activityStream.getCreatedUserFullName());

        div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink, DivLessFormatter.EMPTY_SPACE(),
                TooltipHelper.buildDivTooltipEnable(uid));

        return div.write();
    }

    private String buildItemValue(ProjectActivityStream activityStream) {
        String uid = UUID.randomUUID().toString();
        DivLessFormatter div = new DivLessFormatter();
        Text image = new Text(ProjectAssetsManager.getAsset(activityStream
                .getType()).getHtml());
        A itemLink = new A();
        itemLink.setId("tag" + uid);
        if (ProjectTypeConstants.TASK.equals(activityStream.getType())
                || ProjectTypeConstants.BUG.equals(activityStream.getType())) {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                    activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(),
                    activityStream.getItemKey() + ""));
        } else {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                    activityStream.getProjectShortName(),
                    activityStream.getExtratypeid(), activityStream.getType(),
                    activityStream.getTypeid()));
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
                "return overIt(%s,%s,%s,%s,%s,%s,%s,%s);", arg17, arg18, arg19,
                arg20, arg21, arg22, arg23, arg24);
        itemLink.setAttribute("onmouseover", mouseOverFunc);
        itemLink.appendText(activityStream.getNamefield());

        div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(),
                TooltipHelper.buildDivTooltipEnable(uid));
        return div.write();
    }

    protected void feedBlocksPut(Date currentDate, Date nextDate,
                                 CssLayout currentBlock) {
        MHorizontalLayout blockWrapper = new MHorizontalLayout().withWidth("100%").withStyleName("feed-block-wrap");

        blockWrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(nextDate);

        if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
            int currentYear = cal2.get(Calendar.YEAR);
            Label yearLbl = new Label(String.valueOf(currentYear));
            yearLbl.setStyleName("year-lbl");
            yearLbl.setWidthUndefined();
            yearLbl.setHeight("49px");
            listContainer.addComponent(yearLbl);
        } else {
            blockWrapper.setMargin(new MarginInfo(true, false, false, false));
        }
        Label dateLbl = new Label(DateFormatUtils.format(nextDate,
                AppContext.getUserDayMonthFormat()));
        dateLbl.setSizeUndefined();
        dateLbl.setStyleName("date-lbl");
        blockWrapper.with(dateLbl, currentBlock).expand(currentBlock);

        this.listContainer.addComponent(blockWrapper);
    }

    @Override
    protected CssLayout createPageControls() {
        this.controlBarWrapper = new CssLayout();
        this.controlBarWrapper.setWidth("100%");
        this.controlBarWrapper.setStyleName("page-controls");
        ButtonGroup controlBtns = new ButtonGroup();
        controlBtns.setStyleName(UIConstants.THEME_GREEN_LINK);
        Button prevBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_NAV_NEWER),
                new Button.ClickListener() {
                    private static final long serialVersionUID = -94021599166105307L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        ProjectActivityStreamPagedList.this
                                .pageChange(ProjectActivityStreamPagedList.this.currentPage - 1);
                    }
                });
        if (currentPage == 1) {
            prevBtn.setEnabled(false);
        }
        prevBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        prevBtn.setWidth("64px");

        Button nextBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_NAV_OLDER),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 3095522916508256018L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        ProjectActivityStreamPagedList.this
                                .pageChange(ProjectActivityStreamPagedList.this.currentPage + 1);
                    }
                });
        if (currentPage == totalPage) {
            nextBtn.setEnabled(false);
        }
        nextBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        nextBtn.setWidth("64px");

        controlBtns.addButton(prevBtn);
        controlBtns.addButton(nextBtn);

        controlBarWrapper.addComponent(controlBtns);

        return controlBarWrapper;
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