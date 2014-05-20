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

import java.util.Date;

import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.core.utils.TimezoneMapper.TimezoneExt;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfileFormLayoutFactory;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.module.user.view.component.RoleComboBox;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.CountryComboBox;
import com.esofthead.mycollab.vaadin.ui.DateComboboxSelectionField;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.TimeZoneSelectionField;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserAddViewImpl extends AbstractPageView implements UserAddView {

	private static final long serialVersionUID = 1L;
	private UserAddViewImpl.AdvancedEditUserForm advanceEditForm;
	private SimpleUser user;
	private DateComboboxSelectionField cboDateBirthday;
	private TimeZoneSelectionField cboTimezone;

	public UserAddViewImpl() {
		super();

		this.setMargin(new MarginInfo(false, true, false, true));

		this.advanceEditForm = new UserAddViewImpl.AdvancedEditUserForm(true);
		this.addComponent(this.advanceEditForm);
	}

	@Override
	public void editItem(final SimpleUser item) {
		this.user = item;
		this.removeAllComponents();
		this.addComponent(this.advanceEditForm);
		this.advanceEditForm.setIsLoadEdit(item.getIsLoadEdit());
		if (!item.getIsLoadEdit()) {
			this.user.setLastname(" ");
			this.user.setFirstname(" ");
		}
		this.advanceEditForm.setBean(this.user);
	}

	@Override
	public Date getBirthday() {
		return this.cboDateBirthday.getDate();
	}

	@Override
	public TimezoneExt getTimezone() {
		return this.cboTimezone.getTimeZone();
	}

	public class AdvancedEditUserForm extends AdvancedEditBeanForm<SimpleUser> {

		private static final long serialVersionUID = 1L;
		private Button moreInfoBtn;
		private Boolean isLoadEdit;

		public AdvancedEditUserForm(Boolean isLoadEdit) {
			this.isLoadEdit = isLoadEdit;
		}

		public void setIsLoadEdit(Boolean isLoadEdit) {
			this.isLoadEdit = isLoadEdit;
		}

		@Override
		public void setBean(final SimpleUser newDataSource) {
			this.setFormLayoutFactory(new UserAddViewImpl.AdvancedEditUserForm.FormLayoutFactory(
					isLoadEdit));
			this.setBeanFormFieldFactory(new EditFormFieldFactory(
					advanceEditForm));
			super.setBean(newDataSource);
		}

		private class FormLayoutFactory extends ProfileFormLayoutFactory {

			private static final long serialVersionUID = 1L;
			private Boolean isLoadEdit;

			public FormLayoutFactory(Boolean isLoadEdit) {
				super(
						(UserAddViewImpl.this.user.getUsername() == null) ? "New User"
								: (UserAddViewImpl.this.user.getFirstname()
										+ " " + UserAddViewImpl.this.user
										.getLastname()), isLoadEdit);
				this.isLoadEdit = isLoadEdit;
				this.setAvatarLink(user.getAvatarid());
			}

			@Override
			public Layout getLayout() {
				final AddViewLayout formAddLayout = new AddViewLayout(
						initFormHeader(),
						MyCollabResource
								.newResource("icons/24/project/user.png"));

				final ComponentContainer topLayout = createButtonControls();
				if (topLayout != null) {
					formAddLayout.addHeaderRight(topLayout);
				}

				formAddLayout.setTitle(initFormTitle());

				userInformationLayout = new UserInformationLayout();

				formAddLayout.addBody(userInformationLayout.getLayout());

				final ComponentContainer bottomPanel = createBottomPanel();
				if (bottomPanel != null) {
					formAddLayout.addBottomControls(bottomPanel);
				}

				return formAddLayout;
			}

			protected String initFormHeader() {
				return (user.getUsername() == null) ? "New User" : "Edit User";
			}

			protected String initFormTitle() {
				return (user.getUsername() == null) ? null : user
						.getFirstname() + " " + user.getLastname();
			}

			private Layout createButtonControls() {
				final HorizontalLayout controlPanel = new HorizontalLayout();
				final Layout controlButtons = (new EditFormControlsGenerator<SimpleUser>(
						UserAddViewImpl.AdvancedEditUserForm.this))
						.createButtonControls();
				controlButtons.setSizeUndefined();
				controlPanel.addComponent(controlButtons);
				controlPanel.setComponentAlignment(controlButtons,
						Alignment.MIDDLE_CENTER);
				return controlPanel;
			}

			@Override
			protected Layout createBottomPanel() {
				if (isLoadEdit == false) {
					final HorizontalLayout controlPanel = new HorizontalLayout();
					controlPanel.setMargin(true);
					controlPanel.setStyleName("more-info");
					controlPanel.setHeight("40px");
					controlPanel.setWidth("100%");
					moreInfoBtn = new Button("More information...",
							new Button.ClickListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void buttonClick(ClickEvent event) {
									UserAddViewImpl.this.user = new SimpleUser();
									UserAddViewImpl.this.advanceEditForm = new UserAddViewImpl.AdvancedEditUserForm(
											true);
									advanceEditForm
											.addFormHandler(new EditFormHandler<SimpleUser>() {
												private static final long serialVersionUID = 1L;

												@Override
												public void onSave(
														final SimpleUser item) {
													save(item);
													ViewState viewState = HistoryViewManager
															.back();
													if (viewState instanceof NullViewState) {
														EventBus.getInstance()
																.fireEvent(
																		new UserEvent.GotoList(
																				this,
																				null));
													}
												}

												@Override
												public void onCancel() {
													ViewState viewState = HistoryViewManager
															.back();
													if (viewState instanceof NullViewState) {
														EventBus.getInstance()
																.fireEvent(
																		new UserEvent.GotoList(
																				this,
																				null));
													}
												}

												@Override
												public void onSaveAndNew(
														final SimpleUser item) {
													save(item);
													EventBus.getInstance()
															.fireEvent(
																	new UserEvent.GotoAdd(
																			this,
																			null));
												}
											});
									UserAddViewImpl.this.advanceEditForm
											.setBean(UserAddViewImpl.this.user);
									UserAddViewImpl.this.removeAllComponents();
									UserAddViewImpl.this
											.addComponent(advanceEditForm);
								}
							});
					moreInfoBtn.addStyleName(UIConstants.THEME_LINK);
					controlPanel.addComponent(moreInfoBtn);
					controlPanel.setComponentAlignment(moreInfoBtn,
							Alignment.MIDDLE_LEFT);
					return controlPanel;
				} else {
					return null;
				}
			}
		}

		private class EditFormFieldFactory extends
				AbstractBeanFieldGroupEditFieldFactory<SimpleUser> {
			private static final long serialVersionUID = 1L;

			public EditFormFieldFactory(GenericBeanForm<SimpleUser> form) {
				super(form);
			}

			@Override
			protected Field<?> onCreateField(final Object propertyId) {

				if (propertyId.equals("roleid")) {
					AdminRoleSelectionField roleSelectionField = new AdminRoleSelectionField();
					return roleSelectionField;
				} else if (propertyId.equals("firstname")
						|| propertyId.equals("lastname")
						|| propertyId.equals("email")) {
					final TextField tf = new TextField();
					tf.setNullRepresentation("");
					tf.setRequired(true);
					tf.setRequiredError("This field must be not null");
					return tf;
				} else if (propertyId.equals("dateofbirth")) {
					UserAddViewImpl.this.cboDateBirthday = new DateComboboxSelectionField();
					return UserAddViewImpl.this.cboDateBirthday;
				} else if (propertyId.equals("timezone")) {
					UserAddViewImpl.this.cboTimezone = new TimeZoneSelectionField();
					if (UserAddViewImpl.this.user.getTimezone() != null) {
						UserAddViewImpl.this.cboTimezone
								.setTimeZone(TimezoneMapper
										.getTimezone(UserAddViewImpl.this.user
												.getTimezone()));
					} else {
						if (AppContext.getSession().getTimezone() != null) {
							UserAddViewImpl.this.cboTimezone
									.setTimeZone(TimezoneMapper
											.getTimezone(AppContext
													.getSession().getTimezone()));
						}
					}
					return UserAddViewImpl.this.cboTimezone;
				} else if (propertyId.equals("country")) {
					final CountryComboBox cboCountry = new CountryComboBox();
					cboCountry
							.addValueChangeListener(new Property.ValueChangeListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void valueChange(
										final Property.ValueChangeEvent event) {
									UserAddViewImpl.this.user
											.setCountry((String) cboCountry
													.getValue());
								}
							});
					return cboCountry;
				}
				return null;
			}
		}

		private class AdminRoleSelectionField extends CustomField<Integer> {
			private static final long serialVersionUID = 1L;

			private RoleComboBox roleBox;

			public AdminRoleSelectionField() {
				roleBox = new RoleComboBox();
				this.roleBox
						.addValueChangeListener(new Property.ValueChangeListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void valueChange(
									final Property.ValueChangeEvent event) {
								getValue();

							}
						});
			}

			@Override
			public void setPropertyDataSource(Property newDataSource) {
				Object value = newDataSource.getValue();
				if (value instanceof Integer) {
					roleBox.setValue(value);
				}
				super.setPropertyDataSource(newDataSource);
			}

			@Override
			public void commit() throws SourceException, InvalidValueException {
				Integer roleId = (Integer) roleBox.getValue();
				if (roleId == -1) {
					UserAddViewImpl.this.user.setIsAccountOwner(Boolean.TRUE);
				} else {
					UserAddViewImpl.this.user.setIsAccountOwner(Boolean.FALSE);
				}

				super.commit();
			}

			@Override
			public Class<Integer> getType() {
				return Integer.class;
			}

			@Override
			protected Component initContent() {
				return roleBox;
			}
		}
	}

	@Override
	public HasEditFormHandlers<SimpleUser> getEditFormHandlers() {
		return this.advanceEditForm;
	}

	public void save(SimpleUser item) {
		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);

		item.setAccountId(AppContext.getAccountId());

		item.setDateofbirth(UserAddViewImpl.this.getBirthday());
		item.setTimezone(UserAddViewImpl.this.getTimezone().getId());

		if (item.getUsername() == null) {
			userService.saveUserAccount(item, AppContext.getAccountId(),
					AppContext.getUsername());
		} else {
			userService.updateUserAccount(item, AppContext.getAccountId());
		}

	}
}
