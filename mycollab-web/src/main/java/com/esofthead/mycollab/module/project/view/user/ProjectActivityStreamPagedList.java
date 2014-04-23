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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.localization.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.localization.ProjectLocalizationTypeMap;
import com.esofthead.mycollab.module.project.service.ProjectActivityStreamService;
import com.esofthead.mycollab.module.project.ui.components.ProjectActivityStreamGenerator;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectActivityStreamPagedList
		extends
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

			for (final SimpleActivityStream activityStream : currentListData) {
				final Date itemCreatedDate = activityStream.getCreatedtime();

				if (!DateUtils.isSameDay(currentDate, itemCreatedDate)) {
					currentFeedBlock = new CssLayout();
					currentFeedBlock.setStyleName("feed-block");
					feedBlocksPut(currentDate, itemCreatedDate,
							currentFeedBlock);
					currentDate = itemCreatedDate;
				}
				String content = "";

				// --------------Item hidden div tooltip----------------
				String randomStrId = UUID.randomUUID().toString();
				String idDivSeverData = "serverdata" + randomStrId + "";
				String idToopTipDiv = "tooltip" + randomStrId + "";
				String idStickyToolTipDiv = "mystickyTooltip" + randomStrId;
				String idtagA = "tagA" + randomStrId;
				// --------------User hidden div tooltip-----------------
				String idDivUserSeverData = "userserverdata" + randomStrId + "";
				String idUserToopTipDiv = "usertooltip" + randomStrId + "";
				String idUserStickyToolTipDiv = "usermystickyTooltip"
						+ randomStrId;
				String idUsertagA = "usertagA" + randomStrId;

				String arg0 = UserAvatarControlFactory.getAvatarLink(
						activityStream.getCreatedUserAvatarId(), 16);
				String arg1 = idUsertagA;
				String arg2 = ProjectLinkBuilder.generateProjectMemberFullLink(
						activityStream.getExtratypeid(),
						activityStream.getCreateduser());
				String arg3 = "'" + randomStrId + "'";
				String arg4 = "'" + activityStream.getCreateduser() + "'";
				String arg5 = "'" + AppContext.getSiteUrl() + "tooltip/'";
				String arg6 = "'" + AppContext.getSiteUrl() + "'";
				String arg7 = AppContext.getSession().getTimezone();
				String arg8 = "'" + activityStream.getSaccountid() + "'";
				String arg9 = activityStream.getCreatedUserFullName();
				String arg10 = idUserStickyToolTipDiv;
				String arg11 = idUserToopTipDiv;
				String arg12 = idDivUserSeverData;
				String arg13 = LocalizationHelper
						.getMessage(ProjectLocalizationTypeMap
								.getType(activityStream.getType()));
				String arg14 = ProjectResources.getResourceLink(activityStream
						.getType());
				String arg15 = idtagA;
				String arg16 = ProjectLinkBuilder.generateProjectItemLink(
						activityStream.getExtratypeid(),
						activityStream.getType(), activityStream.getTypeid());
				String arg17 = "'" + randomStrId + "'";
				String arg18 = "'" + activityStream.getType() + "'";
				String arg19 = "'" + activityStream.getTypeid() + "'";
				String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
				String arg21 = "'" + activityStream.getSaccountid() + "'";
				String arg22 = "'" + AppContext.getSiteUrl() + "'";
				String arg23 = AppContext.getSession().getTimezone();
				String arg24 = activityStream.getNamefield();
				String arg25 = idStickyToolTipDiv;
				String arg26 = idToopTipDiv;
				String arg27 = idDivSeverData;
				if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream
						.getAction())) {
					content = LocalizationHelper
							.getMessage(
									ProjectCommonI18nEnum.FEED_USER_ACTIVITY_CREATE_ACTION_TITLE,
									arg0, arg1, arg2, arg3, arg4, arg5, arg6,
									arg7, arg8, arg9, arg10, arg11, arg12,
									arg13, arg14, arg15, arg16, arg17, arg18,
									arg19, arg20, arg21, arg22, arg23, arg24,
									arg25, arg26, arg27);
				} else if (ActivityStreamConstants.ACTION_UPDATE
						.equals(activityStream.getAction())) {
					content = LocalizationHelper
							.getMessage(
									ProjectCommonI18nEnum.FEED_USER_ACTIVITY_UPDATE_ACTION_TITLE,
									arg0, arg1, arg2, arg3, arg4, arg5, arg6,
									arg7, arg8, arg9, arg10, arg11, arg12,
									arg13, arg14, arg15, arg16, arg17, arg18,
									arg19, arg20, arg21, arg22, arg23, arg24,
									arg25, arg26, arg27);
					if (activityStream.getAssoAuditLog() != null) {
						content += ProjectActivityStreamGenerator
								.generatorDetailChangeOfActivity(activityStream);
					}
				}
				final Label actionLbl = new Label(content, ContentMode.HTML);
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

	protected void feedBlocksPut(Date currentDate, Date nextDate,
			CssLayout currentBlock) {
		HorizontalLayout blockWrapper = new HorizontalLayout();
		blockWrapper.setStyleName("feed-block-wrap");
		blockWrapper.setWidth("100%");
		blockWrapper.setSpacing(true);

		blockWrapper.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(currentDate);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(nextDate);

		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
			int currentYear = cal2.get(Calendar.YEAR);
			Label yearLbl = new Label(String.valueOf(currentYear));
			yearLbl.setStyleName("year-lbl");
			yearLbl.setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
			yearLbl.setHeight("49px");
			this.listContainer.addComponent(yearLbl);
		} else {
			blockWrapper.setMargin(new MarginInfo(true, false, false, false));
		}
		Label dateLbl = new Label(DateFormatUtils.format(nextDate, "dd/MM"));
		dateLbl.setSizeUndefined();
		dateLbl.setStyleName("date-lbl");
		blockWrapper.addComponent(dateLbl);
		blockWrapper.addComponent(currentBlock);
		blockWrapper.setExpandRatio(currentBlock, 1.0f);

		this.listContainer.addComponent(blockWrapper);
	}

	@Override
	protected CssLayout createPageControls() {
		this.controlBarWrapper = new CssLayout();
		this.controlBarWrapper.setWidth("100%");
		this.controlBarWrapper.setStyleName("page-controls");
		ButtonGroup controlBtns = new ButtonGroup();
		controlBtns.setStyleName(UIConstants.THEME_GREEN_LINK);
		Button prevBtn = new Button("Newer", new Button.ClickListener() {
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

		Button nextBtn = new Button("Older", new Button.ClickListener() {
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

		this.controlBarWrapper.addComponent(controlBtns);

		return this.controlBarWrapper;
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
