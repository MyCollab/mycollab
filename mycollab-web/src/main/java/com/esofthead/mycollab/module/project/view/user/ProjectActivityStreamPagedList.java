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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.common.service.ActivityStreamService;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.ProjectContants;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.localization.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.ProjectActivityStreamGenerator;
import com.esofthead.mycollab.module.project.view.ProjectLinkBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
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

	private final ActivityStreamService activityStreamService;

	public ProjectActivityStreamPagedList() {
		super(null, 20);
		this.activityStreamService = ApplicationContextUtil
				.getSpringBean(ActivityStreamService.class);

	}

	@Override
	public void doSearch() {
		this.totalCount = this.activityStreamService
				.getTotalCount(this.searchRequest.getSearchCriteria());
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

		final List<SimpleActivityStream> currentListData = this.activityStreamService
				.findPagableListByCriteria(this.searchRequest);
		this.listContainer.removeAllComponents();

		Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();

		try {
			for (final SimpleActivityStream activityStream : currentListData) {
				final Date itemCreatedDate = activityStream.getCreatedtime();

				if (!DateUtils.isSameDay(currentDate, itemCreatedDate)) {
					final CssLayout dateWrapper = new CssLayout();
					dateWrapper.setWidth("100%");
					dateWrapper.addStyleName("date-wrapper");
					dateWrapper.addComponent(new Label(AppContext
							.formatDate(itemCreatedDate)));
					this.listContainer.addComponent(dateWrapper);
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
				String arg13 = activityStream.getType().toLowerCase();
				if (arg13.equalsIgnoreCase(ProjectContants.TASK_LIST)) {
					arg13 = "task group";
				} else if (arg13.equalsIgnoreCase(ProjectContants.STANDUP)) {
					arg13 = "standup report";
				}
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
					// tooltip id is = tooltip + dateTime + typeId
					// serverData id is = serverdata + dateTime + typeId
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
				this.listContainer.addComponent(streamWrapper);
			}
		} catch (final Exception e) {
			throw new MyCollabException(e);
		}
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
