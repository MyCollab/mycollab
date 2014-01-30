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

package com.esofthead.mycollab.module.crm.view;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.common.service.ActivityStreamService;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.localization.CrmLocalizationTypeMap;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivityStreamPanel extends Depot {
	private static final long serialVersionUID = 1L;

	private final CrmActivityStreamPagedList activityStreamList;

	public ActivityStreamPanel() {
		super("Activity Channels", new VerticalLayout(), "100%");
		this.activityStreamList = new CrmActivityStreamPagedList();

		this.activityStreamList.addStyleName("stream-list");
		this.bodyContent.addComponent(this.activityStreamList);
		this.addStyleName("activity-panel");
		((VerticalLayout) this.bodyContent).setMargin(false);
	}

	public void display() {
		final ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
		searchCriteria.setModuleSet(new SetSearchField<String>(SearchField.AND,
				new String[] { ModuleNameConstants.CRM }));
		searchCriteria.setSaccountid(new NumberSearchField(AppContext
				.getAccountId()));
		this.activityStreamList.setSearchCriteria(searchCriteria);
	}

	static class CrmActivityStreamPagedList
			extends
			AbstractBeanPagedList<ActivityStreamSearchCriteria, SimpleActivityStream> {
		private static final long serialVersionUID = 1L;

		private final ActivityStreamService activityStreamService;

		public CrmActivityStreamPagedList() {
			super(null, 20);
			this.activityStreamService = ApplicationContextUtil
					.getSpringBean(ActivityStreamService.class);

		}

		@Override
		protected void doSearch() {
			this.totalCount = this.activityStreamService
					.getTotalCount(this.searchRequest.getSearchCriteria());
			this.totalPage = (this.totalCount - 1)
					/ this.searchRequest.getNumberOfItems() + 1;
			if (this.searchRequest.getCurrentPage() > this.totalPage) {
				this.searchRequest.setCurrentPage(this.totalPage);
			}

			if (this.totalPage > 1) {
				// Define button layout
				if (this.controlBarWrapper != null) {
					this.removeComponent(this.controlBarWrapper);
				}
				this.addComponent(this.createPageControls());
			} else {
				if (this.getComponentCount() == 2) {
					this.removeComponent(this.getComponent(1));
				}
			}

			this.setCurrentPage(this.currentPage);
			this.setTotalPage(this.totalPage);

			final List<SimpleActivityStream> currentListData = this.activityStreamService
					.findPagableListByCriteria(this.searchRequest);
			this.listContainer.removeAllComponents();

			Date currentDate = new GregorianCalendar(2100, 1, 1).getTime();

			try {
				for (final SimpleActivityStream activityStream : currentListData) {

					if (CrmTypeConstants.ACCOUNT.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
						continue;
					} else if (CrmTypeConstants.CONTACT.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_CONTACT)) {
						continue;
					} else if (CrmTypeConstants.CAMPAIGN.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {
						continue;
					} else if (CrmTypeConstants.LEAD.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_LEAD)) {
						continue;
					} else if (CrmTypeConstants.OPPORTUNITY
							.equals(activityStream.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
						continue;
					} else if (CrmTypeConstants.CASE.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_CASE)) {
						continue;
					} else if (CrmTypeConstants.TASK.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_TASK)) {
						continue;
					} else if (CrmTypeConstants.MEETING.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_MEETING)) {
						continue;
					} else if (CrmTypeConstants.CALL.equals(activityStream
							.getType())
							&& !AppContext
									.canRead(RolePermissionCollections.CRM_CALL)) {
						continue;
					}

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

					CrmCommonI18nEnum action = null;

					if (ActivityStreamConstants.ACTION_CREATE
							.equals(activityStream.getAction())) {
						action = CrmCommonI18nEnum.WIDGET_ACTIVITY_CREATE_ACTION;
					} else if (ActivityStreamConstants.ACTION_UPDATE
							.equals(activityStream.getAction())) {
						action = CrmCommonI18nEnum.WIDGET_ACTIVITY_UPDATE_ACTION;
					}
					// --------------Item hidden div tooltip----------------
					String randomStrId = UUID.randomUUID().toString();
					String idDivSeverData = "crmActivityserverdata"
							+ randomStrId + "";
					String idToopTipDiv = "crmActivitytooltip" + randomStrId
							+ "";
					String idStickyToolTipDiv = "crmActivitymystickyTooltip"
							+ randomStrId;
					String idtagA = "crmActivitytagA" + randomStrId;
					// --------------User hidden div tooltip-----------------
					String idDivUserSeverData = "crmuserserverdata"
							+ randomStrId + "";
					String idUserToopTipDiv = "crmusertooltip" + randomStrId
							+ "";
					String idUserStickyToolTipDiv = "crmusermystickyTooltip"
							+ randomStrId;
					String idUsertagA = "crmusertagA" + randomStrId;

					String arg0 = UserAvatarControlFactory.getAvatarLink(
							activityStream.getCreatedUserAvatarId(), 16);
					String arg1 = idUsertagA;
					String arg2 = UserLinkUtils.generatePreviewFullUserLink(
							SiteConfiguration.getSiteUrl(AppContext
									.getSession().getSubdomain()),
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
							.getMessage(CrmLocalizationTypeMap
									.getType(activityStream.getType()));
					String arg14 = CrmResources.getResourceLink(activityStream
							.getType());
					String arg15 = idtagA;
					String arg16 = CrmLinkGenerator.generateCrmItemLink(
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
					String arg25 = idStickyToolTipDiv;
					String arg26 = idToopTipDiv;
					String arg27 = idDivSeverData;
					StringBuffer content = new StringBuffer(
							LocalizationHelper.getMessage(action, arg0, arg1,
									arg2, arg3, arg4, arg5, arg6, arg7, arg8,
									arg9, arg10, arg11, arg12, arg13, arg14,
									arg15, arg16, arg17, arg18, arg19, arg20,
									arg21, arg22, arg23, arg24, arg25, arg26,
									arg27));
					if (activityStream.getAssoAuditLog() != null) {
						content.append(CrmActivityStreamGenerator
								.generatorDetailChangeOfActivity(activityStream));
					}

					final Label activityLink = new Label(content.toString(),
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
		protected List<SimpleActivityStream> queryCurrentData() {
			return null;
		}
	}
}
