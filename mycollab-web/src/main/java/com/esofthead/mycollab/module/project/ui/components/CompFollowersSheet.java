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
package com.esofthead.mycollab.module.project.ui.components;

import java.util.Arrays;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.domain.MonitorItem;
import com.esofthead.mycollab.common.domain.SimpleMonitorItem;
import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.MonitorItemService;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberMultiSelectComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author NyCollab Ltd.
 * @since 1.0
 * 
 * @param <V>
 */
public abstract class CompFollowersSheet<V extends ValuedBean> extends
		VerticalLayout {
	private static final long serialVersionUID = 1L;

	protected DefaultPagedBeanTable<MonitorItemService, MonitorSearchCriteria, SimpleMonitorItem> tableItem;
	protected MonitorItemService monitorItemService;
	protected V bean;
	protected Button btnSave;
	protected Button followBtn;

	protected CompFollowersSheet(V bean) {
		this.bean = bean;
		this.setMargin(true);
		this.setSpacing(true);
		this.setWidth("100%");

		monitorItemService = ApplicationContextUtil
				.getSpringBean(MonitorItemService.class);

		initUI();
	}

	public void setBean(V bean) {
		this.bean = bean;
	}

	private void initUI() {
		Label lbInstruct = new Label(
				"Add people from your team to follow this bug activity");
		this.addComponent(lbInstruct);

		HorizontalLayout layoutAdd = new HorizontalLayout();
		layoutAdd.setSpacing(true);

		final ProjectMemberMultiSelectComp memberSelection = new ProjectMemberMultiSelectComp();
		layoutAdd.addComponent(memberSelection);
		layoutAdd.setComponentAlignment(memberSelection, Alignment.MIDDLE_LEFT);

		btnSave = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_ADD_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {

						List<SimpleProjectMember> members = memberSelection
								.getSelectedItems();

						boolean canSendEmail = false;

						for (ProjectMember member : members) {

							ProjectMemberService memberService = ApplicationContextUtil
									.getSpringBean(ProjectMemberService.class);

							SimpleProjectMember projectMember = memberService
									.findMemberByUsername(AppContext
											.getUsername(),
											CurrentProjectVariables
													.getProjectId(), AppContext
													.getAccountId());

							boolean haveRightToSave = projectMember
									.getIsadmin()
									|| (AppContext.getUsername().equals(member
											.getUsername()));

							if (haveRightToSave && member != null
									&& member.getUsername() != null) {

								if (saveMonitorItem(member.getUsername())) {
									if (member.getUsername().equals(
											AppContext.getUsername())) {
										followBtn.setCaption("UnFollow");
									}
									canSendEmail = true;
								}
							}
						}

						if (canSendEmail) {
							saveRelayNotification();
						}

						memberSelection.resetComp();
						loadMonitorItems();
					}
				});

		btnSave.setEnabled(isEnableAdd());
		btnSave.setStyleName(UIConstants.THEME_GREEN_LINK);
		btnSave.setIcon(MyCollabResource.newResource("icons/16/addRecord.png"));

		layoutAdd.addComponent(btnSave);
		layoutAdd.setComponentAlignment(btnSave, Alignment.MIDDLE_LEFT);

		Label lbl = new Label("or");
		layoutAdd.addComponent(lbl);
		layoutAdd.setComponentAlignment(lbl, Alignment.MIDDLE_LEFT);

		followBtn = new Button();
		followBtn.addStyleName("link");
		followBtn.setCaption("Follow");
		followBtn.setDescription("Follow");
		followBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (followBtn.getDescription().equals("Follow")) {
					followBtn.setCaption("UnFollow by Me");
					followBtn.setDescription("UnFollow");
					saveMonitorItem(AppContext.getUsername());
					loadMonitorItems();
				} else if (followBtn.getDescription().equals("UnFollow")) {
					followBtn.setCaption("Follow");
					followBtn.setDescription("Follow");
					for (MonitorItem item : tableItem.getCurrentDataList()) {
						if (item.getUser().equals(AppContext.getUsername())) {
							monitorItemService.removeWithSession(item.getId(),
									AppContext.getUsername(),
									AppContext.getAccountId());
							break;
						}
					}
					loadMonitorItems();
				}
			}
		});
		layoutAdd.addComponent(followBtn);
		layoutAdd.setComponentAlignment(followBtn, Alignment.MIDDLE_LEFT);

		this.addComponent(layoutAdd);

		tableItem = new DefaultPagedBeanTable<MonitorItemService, MonitorSearchCriteria, SimpleMonitorItem>(
				ApplicationContextUtil.getSpringBean(MonitorItemService.class),
				SimpleMonitorItem.class, Arrays.asList(new TableViewField(
						FollowerI18nEnum.FOLLOWER_NAME, "user",
						UIConstants.TABLE_EX_LABEL_WIDTH), new TableViewField(
						FollowerI18nEnum.FOLLOWER_CREATE_DATE, "monitorDate",
						UIConstants.TABLE_DATE_WIDTH), new TableViewField(null,
						"id", UIConstants.TABLE_CONTROL_WIDTH)));

		tableItem.addGeneratedColumn("user", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleMonitorItem monitorItem = tableItem
						.getBeanByIndex(itemId);

				return new ProjectUserLink(monitorItem.getUser(), monitorItem
						.getUserAvatarId(), monitorItem.getUserFullname());

			}
		});

		tableItem.addGeneratedColumn("monitorDate", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					Object itemId, Object columnId) {
				final MonitorItem monitorItem = tableItem
						.getBeanByIndex(itemId);
				Label l = new Label();
				l.setValue(AppContext.formatDate(monitorItem.getMonitorDate()));
				return l;
			}
		});

		tableItem.addGeneratedColumn("id", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final MonitorItem monitorItem = tableItem
						.getBeanByIndex(itemId);

				Button deleteBtn = new Button(null, new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						MonitorItemService monitorItemService = ApplicationContextUtil
								.getSpringBean(MonitorItemService.class);
						monitorItemService.removeWithSession(
								monitorItem.getId(), AppContext.getUsername(),
								AppContext.getAccountId());
						CompFollowersSheet.this.displayMonitorItems();
					}
				});
				deleteBtn.setStyleName("link");
				deleteBtn.setIcon(MyCollabResource
						.newResource("icons/16/delete.png"));
				monitorItem.setExtraData(deleteBtn);

				ProjectMemberService memberService = ApplicationContextUtil
						.getSpringBean(ProjectMemberService.class);
				SimpleProjectMember member = memberService
						.findMemberByUsername(AppContext.getUsername(),
								CurrentProjectVariables.getProjectId(),
								AppContext.getAccountId());

				if (member != null) {
					deleteBtn.setEnabled(member.getIsadmin()
							|| (AppContext.getUsername().equals(monitorItem
									.getUser())));
				}
				return deleteBtn;
			}
		});

		tableItem.setWidth("100%");

		this.addComponent(tableItem);
	}

	public void displayMonitorItems() {
		loadMonitorItems();

		for (SimpleMonitorItem monitorItem : tableItem.getCurrentDataList()) {
			if (monitorItem.getUser().equals(AppContext.getUsername())) {
				followBtn.setCaption("UnFollow");
				break;
			}
		}
		if (tableItem.getCurrentDataList().size() == 0
				|| (followBtn.getDescription() != null && !followBtn
						.getDescription().equals("UnFollow"))) {
			followBtn.setCaption("Follow");
		}
	}

	protected abstract void loadMonitorItems();

	protected abstract boolean saveMonitorItem(String username);

	protected abstract void saveRelayNotification();

	protected abstract boolean isEnableAdd();

}
