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

import java.util.List;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.EmailLink;
import com.esofthead.mycollab.vaadin.ui.UrlLink;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AccountTableDisplay
		extends
		DefaultPagedBeanTable<AccountService, AccountSearchCriteria, SimpleAccount> {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(AccountTableDisplay.class);

	public AccountTableDisplay(List<TableViewField> displayColumns) {
		this(null, displayColumns);
	}

	public AccountTableDisplay(TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		this(null, requiredColumn, displayColumns);

	}

	public AccountTableDisplay(String viewId, TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		super(ApplicationContextUtil.getSpringBean(AccountService.class),
				SimpleAccount.class, viewId, requiredColumn, displayColumns);

		addGeneratedColumn("selected", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					final Object columnId) {
				final CheckBoxDecor cb = new CheckBoxDecor("", false);
				cb.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						final SimpleAccount account = AccountTableDisplay.this
								.getBeanByIndex(itemId);
						AccountTableDisplay.this.fireSelectItemEvent(account);
						fireTableEvent(new TableClickEvent(
								AccountTableDisplay.this, account, "selected"));

					}
				});

				final SimpleAccount account = AccountTableDisplay.this
						.getBeanByIndex(itemId);
				account.setExtraData(cb);
				return cb;
			}
		});

		addGeneratedColumn("email", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleAccount account = AccountTableDisplay.this
						.getBeanByIndex(itemId);
				return new EmailLink(account.getEmail());
			}
		});

		addGeneratedColumn("accountname", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleAccount account = AccountTableDisplay.this
						.getBeanByIndex(itemId);

				LabelLink b = new LabelLink(account.getAccountname(),
						CrmLinkBuilder.generateAccountPreviewLinkFull(account
								.getId()));
				b.setDescription(generateAccountToolTip(account));
				return b;
			}
		});

		addGeneratedColumn("assignUserFullName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleAccount account = AccountTableDisplay.this
						.getBeanByIndex(itemId);
				final UserLink b = new UserLink(account.getAssignuser(),
						account.getAssignUserAvatarId(), account
								.getAssignUserFullName());
				return b;

			}
		});

		addGeneratedColumn("website", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleAccount account = AccountTableDisplay.this
						.getBeanByIndex(itemId);
				if (account.getWebsite() != null) {
					return new UrlLink(account.getWebsite());
				} else {
					return new Label("");
				}

			}
		});

		this.setWidth("100%");
	}

	private String generateAccountToolTip(SimpleAccount account) {
		try {
			Div div = new Div();
			H3 accountName = new H3();
			accountName
					.appendText(Jsoup.parse(account.getAccountname()).html());
			div.appendChild(accountName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Website:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(account.getWebsite() != null) ? account
															.getWebsite() : "")
													.appendText(
															StringUtils
																	.getStringFieldValue(account
																			.getWebsite()))));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 150px; vertical-align: top; text-align: right;")
							.appendText("Office Phone:"))
					.appendChild(
							new Td().setStyle(
									"width:200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(account
															.getPhoneoffice())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Employees:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											(account.getNumemployees() != null) ? account
													.getNumemployees()
													.toString() : ""));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Email:"))
					.appendChild(
							new Td().setStyle(
									"width:200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(account.getEmail() != null) ? "mailto:"
															+ account
																	.getEmail()
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(account
																			.getEmail()))));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(account.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	AppContext
																			.getSiteUrl(),
																	account.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					account.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(account
																			.getAssignUserFullName()))));

			trRow3.appendChild(
					new Td().setStyle(
							"width: 150px; vertical-align: top; text-align: right;")
							.appendText("Annual Revenue:"))
					.appendChild(
							new Td().setStyle(
									"width: 180px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(account
															.getAnnualrevenue())));

			Tr trRow4 = new Tr();
			Td trRow4_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(account
									.getDescription()));
			trRow4_value.setAttribute("colspan", "3");
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow4_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate Account tooltip", e);
			return "";
		}
	}
}
