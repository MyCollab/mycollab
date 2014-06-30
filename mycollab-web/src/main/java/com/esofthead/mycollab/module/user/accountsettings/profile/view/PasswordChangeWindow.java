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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.utils.InvalidPasswordException;
import com.esofthead.mycollab.core.utils.PasswordCheckerUtil;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class PasswordChangeWindow extends Window {

	private PasswordField txtNewPassword;
	private PasswordField txtConfirmPassword;

	private final User user;

	public PasswordChangeWindow(final User user) {
		this.setWidth("500px");
		this.initUI();
		this.center();
		this.setResizable(false);
		this.setModal(true);
		this.user = user;
		this.setCaption(AppContext
				.getMessage(UserI18nEnum.WINDOW_CHANGE_PASSWORD_TITLE));
	}

	private void initUI() {

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		final Label lbInstruct1 = new Label(
				AppContext.getMessage(UserI18nEnum.MSG_PASSWORD_INSTRUCT_LABEL_1));
		mainLayout.addComponent(lbInstruct1);
		mainLayout.setComponentAlignment(lbInstruct1, Alignment.MIDDLE_LEFT);

		final Label lbInstruct2 = new Label(
				AppContext.getMessage(UserI18nEnum.MSG_PASSWORD_INSTRUCT_LABEL_2));
		mainLayout.addComponent(lbInstruct2);
		mainLayout.setComponentAlignment(lbInstruct2, Alignment.MIDDLE_LEFT);

		final GridFormLayoutHelper passInfo = new GridFormLayoutHelper(1, 3,
				UIConstants.DEFAULT_CONTROL_WIDTH_1024_RESOLUTION, "150px");

		this.txtNewPassword = (PasswordField) passInfo.addComponent(
				new PasswordField(), "New Password", 0, 0);
		this.txtConfirmPassword = (PasswordField) passInfo.addComponent(
				new PasswordField(), "Confirmed New Password", 0, 1);

		passInfo.getLayout().setSpacing(true);
		mainLayout.addComponent(passInfo.getLayout());
		mainLayout.setComponentAlignment(passInfo.getLayout(),
				Alignment.MIDDLE_CENTER);

		final Label lbSpace = new Label();
		mainLayout.addComponent(lbSpace);
		mainLayout.setExpandRatio(lbSpace, 1.0f);

		final HorizontalLayout hlayoutControls = new HorizontalLayout();

		final Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						PasswordChangeWindow.this.close();
					}
				});

		cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		hlayoutControls.addComponent(cancelBtn);
		hlayoutControls.setSpacing(true);
		hlayoutControls.setMargin(true);
		hlayoutControls.setComponentAlignment(cancelBtn,
				Alignment.MIDDLE_CENTER);

		final Button sendBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						PasswordChangeWindow.this.changePassword();
					}
				});
		sendBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		hlayoutControls.addComponent(sendBtn);
		hlayoutControls.setComponentAlignment(sendBtn, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(hlayoutControls);
		mainLayout.setComponentAlignment(hlayoutControls,
				Alignment.MIDDLE_RIGHT);

		this.setModal(true);
		this.setContent(mainLayout);
	}

	private void changePassword() {

		this.txtNewPassword.removeStyleName("errorField");
		this.txtConfirmPassword.removeStyleName("errorField");

		if (!this.txtNewPassword.getValue().equals(
				this.txtConfirmPassword.getValue())) {
			NotificationUtil.showErrorNotification(AppContext
					.getMessage(UserI18nEnum.ERROR_PASSWORDS_ARE_NOT_MATCH));
			this.txtNewPassword.addStyleName("errorField");
			this.txtConfirmPassword.addStyleName("errorField");
			return;
		}

		try {
			PasswordCheckerUtil.checkValidPassword(this.txtNewPassword
					.getValue());
		} catch (InvalidPasswordException e) {
			NotificationUtil.showErrorNotification(e.getMessage());
		}

		this.user.setPassword(PasswordEncryptHelper
				.encryptSaltPassword(this.txtNewPassword.getValue()));

		final UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		userService.updateWithSession(this.user, AppContext.getUsername());

		EventBus.getInstance().fireEvent(
				new ProfileEvent.GotoProfileView(PasswordChangeWindow.this,
						null));
		PasswordChangeWindow.this.close();
	}
}
