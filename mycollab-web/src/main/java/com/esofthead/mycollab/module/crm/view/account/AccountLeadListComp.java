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
package com.esofthead.mycollab.module.crm.view.account;

import java.util.Arrays;
import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.AccountLead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp;
import com.esofthead.mycollab.module.crm.view.lead.LeadTableDisplay;
import com.esofthead.mycollab.module.crm.view.lead.LeadTableFieldDef;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class AccountLeadListComp extends
		RelatedListComp<SimpleLead, LeadSearchCriteria> {

	private static final long serialVersionUID = 1L;
	private Account account;

	public AccountLeadListComp() {
		initUI();
	}

	public void displayLeads(final Account account) {
		this.account = account;
		loadLeads();
	}

	private void initUI() {

		final SplitButton controlsBtn = new SplitButton();
		controlsBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
		controlsBtn.setCaption("New Lead");
		controlsBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		controlsBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_LEAD));
		controlsBtn
				.addClickListener(new SplitButton.SplitButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void splitButtonClick(
							final SplitButton.SplitButtonClickEvent event) {
						fireNewRelatedItem("");
					}
				});
		final Button selectBtn = new Button("Select from existing leads",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						final AccountLeadSelectionWindow leadsWindow = new AccountLeadSelectionWindow(
								AccountLeadListComp.this);
						final LeadSearchCriteria criteria = new LeadSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						UI.getCurrent().addWindow(leadsWindow);
						leadsWindow.setSearchCriteria(criteria);
						controlsBtn.setPopupVisible(false);
					}
				});
		selectBtn.setIcon(MyCollabResource.newResource("icons/16/select.png"));
		selectBtn.setStyleName("link");

		VerticalLayout buttonControlsLayout = new VerticalLayout();
		buttonControlsLayout.addComponent(selectBtn);
		controlsBtn.setContent(buttonControlsLayout);

		this.addComponent(controlsBtn);

		tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name,
				LeadTableFieldDef.status, LeadTableFieldDef.phoneoffice,
				LeadTableFieldDef.email, LeadTableFieldDef.action));

		tableItem
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(final TableClickEvent event) {
						final SimpleLead lead = (SimpleLead) event.getData();
						if ("leadName".equals(event.getFieldName())) {
							EventBus.getInstance().fireEvent(
									new LeadEvent.GotoRead(this, lead.getId()));
						}
					}
				});

		tableItem.addGeneratedColumn("id", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					final Object columnId) {
				final SimpleLead lead = tableItem.getBeanByIndex(itemId);
				final HorizontalLayout controlLayout = new HorizontalLayout();
				controlLayout.setWidth("50px");
				final Button editBtn = new Button(null,
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								EventBus.getInstance().fireEvent(
										new LeadEvent.GotoRead(
												AccountLeadListComp.this, lead
														.getId()));
							}
						});
				editBtn.setStyleName("link");
				editBtn.setIcon(MyCollabResource
						.newResource("icons/16/edit.png"));
				controlLayout.addComponent(editBtn);

				final Button deleteBtn = new Button(null,
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								ConfirmDialogExt.show(
										UI.getCurrent(),
										LocalizationHelper
												.getMessage(
														GenericI18Enum.DELETE_DIALOG_TITLE,
														SiteConfiguration
																.getSiteName()),
										LocalizationHelper
												.getMessage(CrmCommonI18nEnum.DIALOG_DELETE_RELATIONSHIP_TITLE),
										LocalizationHelper
												.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
										LocalizationHelper
												.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
										new ConfirmDialog.Listener() {
											private static final long serialVersionUID = 1L;

											@Override
											public void onClose(
													final ConfirmDialog dialog) {
												if (dialog.isConfirmed()) {
													final AccountService accountService = ApplicationContextUtil
															.getSpringBean(AccountService.class);
													final AccountLead associateLead = new AccountLead();
													associateLead
															.setAccountid(account
																	.getId());
													associateLead
															.setLeadid(lead
																	.getId());
													accountService
															.removeAccountLeadRelationship(
																	associateLead,
																	AppContext
																			.getAccountId());
													AccountLeadListComp.this
															.refresh();
												}
											}
										});
							}
						});
				deleteBtn.setStyleName("link");
				deleteBtn.setIcon(MyCollabResource
						.newResource("icons/16/delete.png"));
				controlLayout.addComponent(deleteBtn);
				return controlLayout;
			}
		});

		this.addComponent(tableItem);
	}

	private void loadLeads() {
		final LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(SearchField.AND, account
				.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadLeads();
	}

	@Override
	public void setSelectedItems(final Set<SimpleLead> selectedItems) {
		fireSelectedRelatedItems(selectedItems);
	}
}
