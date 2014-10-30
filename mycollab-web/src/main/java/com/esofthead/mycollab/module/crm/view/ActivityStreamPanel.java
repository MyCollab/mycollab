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

import static com.esofthead.mycollab.html.DivLessFormatter.EMPTY_SPACE;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.peter.buttongroup.ButtonGroup;

import com.esofthead.mycollab.common.ActivityStreamConstants;
import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.ActivityStreamService;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivityStreamPanel extends CssLayout {
	private static final long serialVersionUID = 1L;
	private static final int MAX_NUMBER_DISPLAY = 20;

	private final CrmActivityStreamPagedList activityStreamList;

	public ActivityStreamPanel() {
		this.activityStreamList = new CrmActivityStreamPagedList();

		this.activityStreamList.addStyleName("stream-list");
		this.addComponent(this.activityStreamList);
		this.setStyleName("crm-activity-list");
	}

	public void display() {
		final ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
		searchCriteria.setModuleSet(new SetSearchField<String>(SearchField.AND,
				new String[] { ModuleNameConstants.CRM }));
		searchCriteria.setSaccountid(new NumberSearchField(AppContext
				.getAccountId()));
		searchCriteria.setOrderByField("createdTime");
		searchCriteria.setSortDirection(SearchCriteria.DESC);
		this.activityStreamList.setSearchCriteria(searchCriteria);
	}

	static class CrmActivityStreamPagedList extends VerticalLayout {

		private static final long serialVersionUID = 1L;

		private final CssLayout listContainer = new CssLayout();
		private CssLayout controlBarWrapper = new CssLayout();
		private CssLayout currentFeedBlock;

		private ActivityStreamService activityStreamService;
		private ActivityStreamSearchCriteria searchCriteria;

		private int firstIndex = 0, lastIndex = 0;
		private Date currentDate;

		public CrmActivityStreamPagedList() {
			listContainer.setStyleName("beanlist-content");
			listContainer.setWidth("100%");
			this.addComponent(listContainer);
			activityStreamService = ApplicationContextUtil
					.getSpringBean(ActivityStreamService.class);
		}

		public void setSearchCriteria(
				final ActivityStreamSearchCriteria searchCriteria) {
			this.listContainer.removeAllComponents();
			this.searchCriteria = searchCriteria;
			navigateToNext();
		}

		@SuppressWarnings("unchecked")
		private void doSearch(boolean isMoveForward) {
			this.listContainer.removeAllComponents();

			currentDate = new GregorianCalendar(2100, 1, 1).getTime();
			currentFeedBlock = new CssLayout();

			Integer currentItemsDisplay = 0;

			while (currentItemsDisplay < MAX_NUMBER_DISPLAY) {
				final List<SimpleActivityStream> currentListData = this.activityStreamService
						.findAbsoluteListByCriteria(searchCriteria, firstIndex,
								MAX_NUMBER_DISPLAY);

				if (currentListData.size() == 0) {
					break;
				}

				for (SimpleActivityStream item : currentListData) {
					if (checkReadPermisson(item.getType())) {
						currentItemsDisplay++;
						showItem(item);
					}
					if (currentItemsDisplay == MAX_NUMBER_DISPLAY) {
						break;
					}
				}

				if (isMoveForward) {
					firstIndex = lastIndex;
					lastIndex = lastIndex + MAX_NUMBER_DISPLAY;
				} else {
					lastIndex = firstIndex;
					firstIndex = firstIndex - MAX_NUMBER_DISPLAY;
				}
			}

			if (hasPrevious() || hasNext()) {
				this.addComponent(createPageControls());
			}
		}

		private void navigateToPrevious() {
			lastIndex = firstIndex;
			firstIndex = firstIndex - MAX_NUMBER_DISPLAY;
			doSearch(false);
		}

		private void navigateToNext() {
			firstIndex = lastIndex;
			lastIndex = lastIndex + MAX_NUMBER_DISPLAY;
			doSearch(true);
		}

		private boolean checkReadPermisson(String type) {
			if (CrmTypeConstants.ACCOUNT.equals(type)
					&& !AppContext
							.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
				return false;
			} else if (CrmTypeConstants.CONTACT.equals(type)
					&& !AppContext
							.canRead(RolePermissionCollections.CRM_CONTACT)) {
				return false;
			} else if (CrmTypeConstants.CAMPAIGN.equals(type)
					&& !AppContext
							.canRead(RolePermissionCollections.CRM_CAMPAIGN)) {
				return false;
			} else if (CrmTypeConstants.LEAD.equals(type)
					&& !AppContext.canRead(RolePermissionCollections.CRM_LEAD)) {
				return false;
			} else if (CrmTypeConstants.OPPORTUNITY.equals(type)
					&& !AppContext
							.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
				return false;
			} else if (CrmTypeConstants.CASE.equals(type)
					&& !AppContext.canRead(RolePermissionCollections.CRM_CASE)) {
				return false;
			} else if (CrmTypeConstants.TASK.equals(type)
					&& !AppContext.canRead(RolePermissionCollections.CRM_TASK)) {
				return false;
			} else if (CrmTypeConstants.MEETING.equals(type)
					&& !AppContext
							.canRead(RolePermissionCollections.CRM_MEETING)) {
				return false;
			} else if (CrmTypeConstants.CALL.equals(type)
					&& !AppContext.canRead(RolePermissionCollections.CRM_CALL)) {
				return false;
			}
			return true;
		}

		private void showItem(final SimpleActivityStream activityStream) {

			final Date itemCreatedDate = activityStream.getCreatedtime();

			if (!DateUtils.isSameDay(currentDate, itemCreatedDate)) {
				currentFeedBlock = new CssLayout();
				currentFeedBlock.setStyleName("feed-block");
				feedBlocksPut(currentDate, itemCreatedDate, currentFeedBlock);
				currentDate = itemCreatedDate;
			}

			CrmCommonI18nEnum action = null;

			if (ActivityStreamConstants.ACTION_CREATE.equals(activityStream
					.getAction())) {
				action = CrmCommonI18nEnum.WIDGET_ACTIVITY_CREATE_ACTION;
			} else if (ActivityStreamConstants.ACTION_UPDATE
					.equals(activityStream.getAction())) {
				action = CrmCommonI18nEnum.WIDGET_ACTIVITY_UPDATE_ACTION;
			}
			// --------------Item hidden div tooltip----------------
			String uid = UUID.randomUUID().toString();
			String itemType = AppContext.getMessage(CrmLocalizationTypeMap
					.getType(activityStream.getType()));
			String assigneeValue = buildAssigneeValue(activityStream, uid);
			String itemValue = buildItemValue(activityStream, uid);

			StringBuffer content = new StringBuffer(AppContext.getMessage(
					action, assigneeValue, itemType, itemValue));
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
			currentFeedBlock.addComponent(streamWrapper);
		}

		private CssLayout createPageControls() {
			this.removeComponent(controlBarWrapper);
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
							navigateToPrevious();
						}
					});

			prevBtn.setEnabled(hasPrevious());
			prevBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			prevBtn.setWidth("64px");

			Button nextBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_NAV_OLDER),
					new Button.ClickListener() {
						private static final long serialVersionUID = 3095522916508256018L;

						@Override
						public void buttonClick(ClickEvent event) {
							navigateToNext();
						}
					});

			nextBtn.setEnabled(hasNext());
			nextBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			nextBtn.setWidth("64px");

			controlBtns.addButton(prevBtn);
			controlBtns.addButton(nextBtn);

			this.controlBarWrapper.addComponent(controlBtns);

			return this.controlBarWrapper;
		}

		private boolean hasNext() {
			return !this.activityStreamService.findAbsoluteListByCriteria(
					this.searchCriteria, lastIndex, 1).isEmpty();
		}

		private boolean hasPrevious() {
			return (this.firstIndex > 0);
		}

		private String buildAssigneeValue(SimpleActivityStream activityStream,
				String uid) {
			DivLessFormatter div = new DivLessFormatter();
			Img userAvatar = new Img("", StorageManager.getAvatarLink(
					activityStream.getCreatedUserAvatarId(), 16));
			A userLink = new A();
			userLink.setId("crmusertagA" + uid);
			userLink.setHref(AccountLinkGenerator.generatePreviewFullUserLink(
					AppContext.getSiteUrl(), activityStream.getCreateduser()));

			String arg3 = "'" + uid + "'";
			String arg4 = "'" + activityStream.getCreateduser() + "'";
			String arg5 = "'" + AppContext.getSiteUrl() + "tooltip/'";
			String arg6 = "'" + AppContext.getSiteUrl() + "'";
			String arg7 = AppContext.getSession().getTimezone();
			String arg8 = "'" + activityStream.getSaccountid() + "'";
			String arg9 = "'" + AppContext.getUserLocale().toString() + "'";
			String onMouseOverFunc = String.format(
					"return crmuseroverIt(%s,%s,%s,%s,%s,%s,%s);", arg3, arg4,
					arg5, arg6, arg7, arg8, arg9);
			userLink.setAttribute("onmouseover", onMouseOverFunc);
			userLink.appendText(activityStream.getCreatedUserFullName());

			Div div1 = new Div();
			div1.setId("crmusermystickyTooltip" + uid);
			div1.setAttribute("class", "stickytooltip");

			Div div12 = new Div();
			div12.setAttribute("style", "padding:5px");
			div1.appendChild(div12);

			Div div13 = new Div();
			div13.setId("crmusertooltip" + uid);
			div13.setAttribute("class", "atip");
			div13.setAttribute("style", "width:400px");
			div12.appendChild(div13);

			Div div14 = new Div();
			div14.setId("crmuserserverdata" + uid);
			div13.appendChild(div14);

			div.appendChild(userAvatar, EMPTY_SPACE, userLink, EMPTY_SPACE,
					div1);

			return div.write();
		}

		private String buildItemValue(SimpleActivityStream activityStream,
				String uid) {
			DivLessFormatter div = new DivLessFormatter();
			Img itemImg = new Img("",
					CrmResources.getResourceLink(activityStream.getType()));
			A itemLink = new A();
			itemLink.setId("crmActivitytagA" + uid);
			itemLink.setHref(CrmLinkGenerator.generateCrmItemLink(
					activityStream.getType(), activityStream.getTypeid()));

			String arg17 = "'" + uid + "'";
			String arg18 = "'" + activityStream.getType() + "'";
			String arg19 = "'" + activityStream.getTypeid() + "'";
			String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
			String arg21 = "'" + activityStream.getSaccountid() + "'";
			String arg22 = "'" + AppContext.getSiteUrl() + "'";
			String arg23 = AppContext.getSession().getTimezone();
			String arg24 = "'" + AppContext.getUserLocale().toString() + "'";
			String onMouseOverFunc = String.format(
					"return crmActivityOverIt(%s,%s,%s,%s,%s,%s,%s,%s);",
					arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
			itemLink.setAttribute("onmouseover", onMouseOverFunc);
			itemLink.appendText(activityStream.getNamefield());

			Div div1 = new Div();
			div1.setId("crmActivitymystickyTooltip" + uid);
			div1.setAttribute("class", "stickytooltip");

			Div div12 = new Div();
			div12.setAttribute("style", "padding:5px");
			div1.appendChild(div12);

			Div div13 = new Div();
			div13.setId("crmActivitytooltip" + uid);
			div13.setAttribute("class", "atip");
			div13.setAttribute("style", "width:550px");
			div12.appendChild(div13);

			Div div14 = new Div();
			div14.setId("crmActivityserverdata" + uid);
			div13.appendChild(div14);

			div.appendChild(itemImg, EMPTY_SPACE, itemLink, EMPTY_SPACE, div1);

			return div.write();
		}

		private void feedBlocksPut(Date currentDate, Date nextDate,
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
				yearLbl.setWidthUndefined();
				yearLbl.setHeight("49px");
				this.listContainer.addComponent(yearLbl);
			} else {
				blockWrapper
						.setMargin(new MarginInfo(true, false, false, false));
			}
			Label dateLbl = new Label(DateFormatUtils.format(nextDate, "dd/MM"));
			dateLbl.setSizeUndefined();
			dateLbl.setStyleName("date-lbl");
			blockWrapper.addComponent(dateLbl);
			blockWrapper.addComponent(currentBlock);
			blockWrapper.setExpandRatio(currentBlock, 1.0f);

			this.listContainer.addComponent(blockWrapper);
		}
	}
}
