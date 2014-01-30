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
import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.domain.ProjectActivityStream;
import com.esofthead.mycollab.module.project.localization.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.localization.ProjectLocalizationTypeMap;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.components.ProjectActivityStreamGenerator;
import com.esofthead.mycollab.module.project.view.ProjectLinkBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.web.MyCollabResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivityStreamComponent extends Depot {

	private static final long serialVersionUID = 1L;

	private final ProjectActivityStreamPagedList activityStreamList;

	public ActivityStreamComponent() {
		super(LocalizationHelper.getMessage(ProjectCommonI18nEnum.FEEDS_TITLE),
				new VerticalLayout());
		this.activityStreamList = new ProjectActivityStreamPagedList();

		this.addStyleName("activity-panel");
		this.addStyleName("project-activity-panel");
		((VerticalLayout) this.bodyContent).setMargin(false);
	}

	public void showFeeds(final List<Integer> prjKeys) {
		this.bodyContent.removeAllComponents();
		this.bodyContent.addComponent(this.activityStreamList);

		final ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
		searchCriteria.setModuleSet(new SetSearchField<String>(SearchField.AND,
				new String[] { ModuleNameConstants.PRJ }));
		searchCriteria.setExtraTypeIds(new SetSearchField<Integer>(prjKeys
				.toArray(new Integer[0])));
		searchCriteria.setSaccountid(new NumberSearchField(AppContext
				.getAccountId()));
		this.activityStreamList.setSearchCriteria(searchCriteria);

	}

	static class ProjectActivityStreamPagedList
			extends
			AbstractBeanPagedList<ActivityStreamSearchCriteria, ProjectActivityStream> {
		private static final long serialVersionUID = 1L;
		private final ProjectService projectService;

		public ProjectActivityStreamPagedList() {
			super(null, 20);

			this.projectService = ApplicationContextUtil
					.getSpringBean(ProjectService.class);
		}

		@Override
		protected void doSearch() {
			this.totalCount = this.projectService
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

			final List<ProjectActivityStream> currentListData = this.projectService
					.getProjectActivityStreams(this.searchRequest);
			this.listContainer.removeAllComponents();

			Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();

			try {
				for (final ProjectActivityStream activityStream : currentListData) {
					final Date itemCreatedDate = activityStream
							.getCreatedtime();
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
					String idDivSeverData = "projectOverViewserverdata"
							+ randomStrId + "";
					String idToopTipDiv = "projectOverViewtooltip"
							+ randomStrId + "";
					String idStickyToolTipDiv = "projectOverViewmystickyTooltip"
							+ randomStrId;
					String idtagA = "projectOverViewtagA" + randomStrId;
					// --------------User hidden div tooltip-----------------
					String idDivUserSeverData = "projectuserserverdata"
							+ randomStrId + "";
					String idUserToopTipDiv = "projectusertooltip"
							+ randomStrId + "";
					String idUserStickyToolTipDiv = "projectusermystickyTooltip"
							+ randomStrId;
					String idUsertagA = "projectusertagA" + randomStrId;

					String arg0 = UserAvatarControlFactory.getAvatarLink(
							activityStream.getCreatedUserAvatarId(), 16);
					String arg1 = idUsertagA;
					String arg2 = ProjectLinkBuilder
							.generateProjectMemberFullLink(
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
					String arg14 = ProjectResources
							.getResourceLink(activityStream.getType());
					String arg15 = idtagA;
					String arg16 = ProjectLinkBuilder.generateProjectItemLink(
							activityStream.getExtratypeid(),
							activityStream.getType(),
							activityStream.getTypeid());
					String arg17 = "'" + randomStrId + "'";
					String arg18 = "'" + activityStream.getType() + "'";
					String arg19 = "'" + activityStream.getTypeid() + "'";
					String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
					String arg21 = "'" + activityStream.getSaccountid() + "'";
					String arg22 = "'" + AppContext.getSiteUrl() + "'";
					String arg23 = AppContext.getSession().getTimezone();
					String arg24 = activityStream.getNamefield();
					String arg25 = MyCollabResource
							.newResourceLink("icons/16/project/project.png");
					String arg26 = ProjectLinkBuilder.generateProjectFullLink(
							activityStream.getProjectId(),
							GenericLinkUtils.URL_PREFIX_PARAM);
					String arg27 = activityStream.getProjectName();
					String arg28 = idStickyToolTipDiv;
					String arg29 = idToopTipDiv;
					String arg30 = idDivSeverData;

					if (ActivityStreamConstants.ACTION_CREATE
							.equals(activityStream.getAction())) {
						content = LocalizationHelper
								.getMessage(
										ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_CREATE_ACTION_TITLE,
										arg0, arg1, arg2, arg3, arg4, arg5,
										arg6, arg7, arg8, arg9, arg10, arg11,
										arg12, arg13, arg14, arg15, arg16,
										arg17, arg18, arg19, arg20, arg21,
										arg22, arg23, arg24, arg25, arg26,
										arg27, arg28, arg29, arg30);
					} else if (ActivityStreamConstants.ACTION_UPDATE
							.equals(activityStream.getAction())) {
						content = LocalizationHelper
								.getMessage(
										ProjectCommonI18nEnum.FEED_PROJECT_USER_ACTIVITY_UPDATE_ACTION_TITLE,
										arg0, arg1, arg2, arg3, arg4, arg5,
										arg6, arg7, arg8, arg9, arg10, arg11,
										arg12, arg13, arg14, arg15, arg16,
										arg17, arg18, arg19, arg20, arg21,
										arg22, arg23, arg24, arg25, arg26,
										arg27, arg28, arg29, arg30);
						if (activityStream.getAssoAuditLog() != null) {
							content += ProjectActivityStreamGenerator
									.generatorDetailChangeOfActivity(activityStream);
						}
					}

					final Label activityLink = new Label(content,
							ContentMode.HTML);
					final CssLayout streamWrapper = new CssLayout();
					streamWrapper.setWidth("100%");
					streamWrapper.addStyleName("stream-wrapper");
					streamWrapper.addComponent(activityLink);
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
}
