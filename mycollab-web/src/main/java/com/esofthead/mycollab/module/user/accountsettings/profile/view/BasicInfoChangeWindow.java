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

import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.module.user.ui.components.LanguageComboBox;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.USER_TIMEZONE;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
class BasicInfoChangeWindow extends Window {

	private TextField txtFirstName = new TextField();
	private TextField txtLastName = new TextField();
	private TextField txtEmail = new TextField();
	private DateComboboxSelectionField birthdayField = new DateComboboxSelectionField();
	private TimeZoneSelectionField timeZoneField = new TimeZoneSelectionField(true);
	private LanguageComboBox languageBox = new LanguageComboBox();

	private final User user;

	public BasicInfoChangeWindow(final User user) {
		this.user = user;
		this.setWidth("450px");
		this.setResizable(false);
		this.setModal(true);
		this.initUI();
		this.center();
		this.setCaption(AppContext
				.getMessage(UserI18nEnum.WINDOW_CHANGE_BASIC_INFO_TITLE));
	}

	private void initUI() {
		final MVerticalLayout mainLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false)).withWidth("100%");

		final GridFormLayoutHelper passInfo = new GridFormLayoutHelper(1, 6,
				"100%", "150px", Alignment.TOP_LEFT);

		passInfo.addComponent(txtFirstName,
				AppContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0, 0);
		passInfo.addComponent(txtLastName,
				AppContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0, 1);
		this.txtLastName.setRequired(true);
		passInfo.addComponent(txtEmail,
				AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 0, 2);
		this.txtEmail.setRequired(true);
		passInfo.addComponent(birthdayField,
				AppContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 0, 3);
		this.birthdayField.setDate(this.user.getDateofbirth());

		passInfo.addComponent(timeZoneField,
				AppContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 4);
		this.timeZoneField.setTimeZone(TimezoneMapper.getTimezoneExt(this.user
				.getTimezone()));

		passInfo.addComponent(languageBox,
				AppContext.getMessage(UserI18nEnum.FORM_LANGUAGE), 0, 5);
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

		final MHorizontalLayout hlayoutControls = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true));
		final Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						BasicInfoChangeWindow.this.close();
					}
				});
		cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

		final Button saveBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SAVE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						changeUserInfo();
					}
				});
		saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		saveBtn.setIcon(FontAwesome.SAVE);

        hlayoutControls.with(saveBtn, cancelBtn).alignAll(Alignment.MIDDLE_CENTER);
		mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

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

		EventBusFactory.getInstance().post(
				new ProfileEvent.GotoProfileView(BasicInfoChangeWindow.this,
						null));
		BasicInfoChangeWindow.this.close();
		Page.getCurrent().getJavaScript().execute("window.location.reload();");

	}
}
