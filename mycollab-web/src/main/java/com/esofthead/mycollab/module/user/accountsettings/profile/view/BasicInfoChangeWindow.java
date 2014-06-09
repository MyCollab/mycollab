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
package com.esofthead.mycollab.module.user.accountsettings.profile.view;

import static com.esofthead.mycollab.vaadin.MyCollabSession.USER_TIMEZONE;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.module.user.ui.components.LanguageComboBox;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.MyCollabSession;
import com.esofthead.mycollab.vaadin.ui.DateComboboxSelectionField;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.TimeZoneSelectionField;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class BasicInfoChangeWindow extends Window {

	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtEmail;
	private DateComboboxSelectionField birthdayField;
	private TimeZoneSelectionField timeZoneField;
	private LanguageComboBox languageBox;

	private final User user;

	public BasicInfoChangeWindow(final User user) {
		this.user = user;
		this.setWidth("450px");
		this.setResizable(false);
		this.setModal(true);
		this.initUI();
		this.center();
		this.setCaption("Change your basic information");
	}

	private void initUI() {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(new MarginInfo(false, false, true, false));
		mainLayout.setWidth("100%");
		mainLayout.setSpacing(true);

		final GridFormLayoutHelper passInfo = new GridFormLayoutHelper(1, 6,
				"100%", "150px", Alignment.TOP_LEFT);

		this.txtFirstName = (TextField) passInfo.addComponent(new TextField(),
				"First Name", 0, 0);
		this.txtLastName = (TextField) passInfo.addComponent(new TextField(),
				"Last Name", 0, 1);
		this.txtLastName.setRequired(true);
		this.txtEmail = (TextField) passInfo.addComponent(new TextField(),
				"Email", 0, 2);
		this.txtEmail.setRequired(true);
		this.birthdayField = (DateComboboxSelectionField) passInfo
				.addComponent(new DateComboboxSelectionField(), "Birthday", 0,
						3);
		this.birthdayField.setDate(this.user.getDateofbirth());

		this.timeZoneField = (TimeZoneSelectionField) passInfo.addComponent(
				new TimeZoneSelectionField(), "TimeZone", 0, 4);
		this.timeZoneField.setTimeZone(TimezoneMapper.getTimezone(this.user
				.getTimezone()));

		this.languageBox = (LanguageComboBox) passInfo.addComponent(
				new LanguageComboBox(), "Language", 0, 5);
		this.languageBox.setValue(this.user.getLanguage());

		this.txtFirstName.setValue(this.user.getFirstname() == null ? ""
				: this.user.getFirstname());
		this.txtLastName.setValue(this.user.getLastname() == null ? ""
				: this.user.getLastname());
		this.txtEmail.setValue(this.user.getEmail() == null ? "" : this.user
				.getEmail());
		this.birthdayField.setValue(this.user.getDateofbirth());

		passInfo.getLayout().setMargin(false);
		passInfo.getLayout().setWidth("100%");
		passInfo.getLayout().addStyleName("colored-gridlayout");
		mainLayout.addComponent(passInfo.getLayout());
		mainLayout.setComponentAlignment(passInfo.getLayout(),
				Alignment.TOP_LEFT);

		final HorizontalLayout hlayoutControls = new HorizontalLayout();
		hlayoutControls.setSpacing(true);
		hlayoutControls.setMargin(new MarginInfo(false, true, false, true));
		final Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						BasicInfoChangeWindow.this.close();
					}
				});

		cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		hlayoutControls.addComponent(cancelBtn);
		hlayoutControls.setComponentAlignment(cancelBtn,
				Alignment.MIDDLE_CENTER);

		final Button saveBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						BasicInfoChangeWindow.this.changeUserInfo();
					}
				});
		saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		saveBtn.setIcon(MyCollabResource.newResource("icons/16/save.png"));
		hlayoutControls.addComponent(saveBtn);
		hlayoutControls.setComponentAlignment(saveBtn, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(hlayoutControls);
		mainLayout.setComponentAlignment(hlayoutControls,
				Alignment.MIDDLE_RIGHT);

		this.setModal(true);
		this.setContent(mainLayout);
	}

	private void changeUserInfo() {

		this.txtLastName.removeStyleName("errorField");
		this.txtEmail.removeStyleName("errorField");

		if (this.txtLastName.getValue().equals("")) {
			NotificationUtil
					.showErrorNotification("The last name must be not null!");
			this.txtLastName.addStyleName("errorField");
			return;
		}

		if (this.txtEmail.getValue().equals("")) {
			NotificationUtil
					.showErrorNotification("The email must be not null!");
			this.txtLastName.addStyleName("errorField");
			return;
		}

		this.user.setFirstname(this.txtFirstName.getValue());
		this.user.setLastname(this.txtLastName.getValue());
		this.user.setEmail(this.txtEmail.getValue());
		this.user.setDateofbirth(this.birthdayField.getDate());
		this.user.setLanguage((String) this.languageBox.getValue());
		this.user.setTimezone(this.timeZoneField.getTimeZone().getId());

		MyCollabSession.removeVariable(USER_TIMEZONE);
		MyCollabSession.putVariable(USER_TIMEZONE, this.timeZoneField
				.getTimeZone().getTimezone());

		final UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		userService.updateWithSession(this.user, AppContext.getUsername());

		EventBus.getInstance().fireEvent(
				new ProfileEvent.GotoProfileView(BasicInfoChangeWindow.this,
						null));
		BasicInfoChangeWindow.this.close();
	}
}
