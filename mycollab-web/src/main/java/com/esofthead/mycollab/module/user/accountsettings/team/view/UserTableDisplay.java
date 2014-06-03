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

package com.esofthead.mycollab.module.user.accountsettings.team.view;

import java.util.GregorianCalendar;
import java.util.List;

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.dao.UserAccountInvitationMapper;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserAccountInvitation;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.EmailLink;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class UserTableDisplay extends
		DefaultPagedBeanTable<UserService, UserSearchCriteria, SimpleUser> {
	private static final long serialVersionUID = 1L;

	public UserTableDisplay(TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		super(ApplicationContextUtil.getSpringBean(UserService.class),
				SimpleUser.class, requiredColumn, displayColumns);

		this.addGeneratedColumn("selected", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final SimpleUser user = UserTableDisplay.this
						.getBeanByIndex(itemId);
				final CheckBoxDecor cb = new CheckBoxDecor("", user
						.isSelected());
				cb.setImmediate(true);
				cb.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						UserTableDisplay.this.fireSelectItemEvent(user);
					}
				});

				user.setExtraData(cb);
				return cb;
			}
		});

		this.addGeneratedColumn("username", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleUser user = UserTableDisplay.this
						.getBeanByIndex(itemId);
				UserLink b = new UserLink(user.getUsername(), user
						.getAvatarid(), user.getDisplayName(), false);

				b.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireTableEvent(new TableClickEvent(
								UserTableDisplay.this, user.getUsername(),
								"username"));
					}
				});

				if (RegisterStatusConstants.ACTIVE.equals(user
						.getRegisterstatus())) {
					return b;
				} else {
					HorizontalLayout layout = new HorizontalLayout();
					layout.setWidth("100%");
					layout.setSpacing(true);

					if (RegisterStatusConstants.DELETE.equals(user
							.getRegisterstatus())) {
						layout.addComponent(b);
						Label statusLbl = new Label("(Removed)");
						layout.addComponent(statusLbl);
						layout.addComponent(statusLbl);
					} else {
						HorizontalLayout userLayout = new HorizontalLayout();
						userLayout.addComponent(b);
						userLayout.setWidth("100%");

						Label statusLbl = new Label("(Waiting for accept)");
						userLayout.addComponent(statusLbl);
						userLayout.setExpandRatio(statusLbl, 1.0f);

						layout.addComponent(userLayout);
						layout.setExpandRatio(userLayout, 1.0f);

						Button resendBtn = new Button("Resend invitation",
								new Button.ClickListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(ClickEvent event) {
										UserAccountInvitationMapper userAccountInvitationMapper = ApplicationContextUtil
												.getSpringBean(UserAccountInvitationMapper.class);
										UserAccountInvitation invitation = new UserAccountInvitation();
										invitation.setAccountid(user
												.getAccountId());
										invitation
												.setCreatedtime(new GregorianCalendar()
														.getTime());
										invitation.setUsername(user
												.getUsername());
										invitation.setInvitationstatus((user
												.getRegisterstatus() == null) ? RegisterStatusConstants.VERIFICATING
												: user.getRegisterstatus());
										userAccountInvitationMapper
												.insert(invitation);

									}
								});
						resendBtn.addStyleName("link");
						layout.addComponent(resendBtn);
					}
					return layout;
				}
			}
		});

		this.addGeneratedColumn("email", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					Object itemId, Object columnId) {
				SimpleUser user = UserTableDisplay.this.getBeanByIndex(itemId);
				return new EmailLink(user.getEmail());
			}
		});

		this.addGeneratedColumn("lastaccessedtime",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(Table source,
							final Object itemId, Object columnId) {
						final SimpleUser user = UserTableDisplay.this
								.getBeanByIndex(itemId);
						Label dateLbl = new Label(DateTimeUtils
								.getStringDateFromNow(user
										.getLastaccessedtime()));
						return dateLbl;
					}
				});
	}
}
