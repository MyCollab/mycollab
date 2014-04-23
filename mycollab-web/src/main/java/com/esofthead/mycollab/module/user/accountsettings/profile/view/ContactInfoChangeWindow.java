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

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
public class ContactInfoChangeWindow extends Window {

	private TextField txtWorkPhone;
	private TextField txtHomePhone;
	private TextField txtFaceBook;
	private TextField txtTwitter;
	private TextField txtSkype;
	private final Validator validation;

	private final User user;

	public ContactInfoChangeWindow(final User user) {
		this.user = user;
		this.setWidth("450px");
		this.validation = ApplicationContextUtil
				.getSpringBean(LocalValidatorFactoryBean.class);
		this.initUI();
		this.center();
		this.setCaption(LocalizationHelper
				.getMessage(UserI18nEnum.CHANGE_CONTACT_INFO_WINDOW_TITLE));
	}

	private void initUI() {

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(new MarginInfo(false, false, true, false));
		mainLayout.setSpacing(true);

		final GridFormLayoutHelper passInfo = new GridFormLayoutHelper(1, 6,
				"100%", "150px", Alignment.TOP_LEFT);

		this.txtWorkPhone = (TextField) passInfo.addComponent(new TextField(),
				"Work phone", 0, 0);
		this.txtHomePhone = (TextField) passInfo.addComponent(new TextField(),
				"Home phone", 0, 1);
		this.txtFaceBook = (TextField) passInfo.addComponent(new TextField(),
				"Facebook", 0, 2);
		this.txtTwitter = (TextField) passInfo.addComponent(new TextField(),
				"Twitter", 0, 3);
		this.txtSkype = (TextField) passInfo.addComponent(new TextField(),
				"Skype", 0, 4);

		this.txtWorkPhone.setValue(this.user.getWorkphone() == null ? ""
				: this.user.getWorkphone());
		this.txtHomePhone.setValue(this.user.getHomephone() == null ? ""
				: this.user.getHomephone());
		this.txtFaceBook.setValue(this.user.getFacebookaccount() == null ? ""
				: this.user.getFacebookaccount());
		this.txtTwitter.setValue(this.user.getTwitteraccount() == null ? ""
				: this.user.getTwitteraccount());
		this.txtSkype.setValue(this.user.getSkypecontact() == null ? ""
				: this.user.getSkypecontact());

		passInfo.getLayout().setMargin(false);
		passInfo.getLayout().setWidth("100%");
		passInfo.getLayout().addStyleName("colored-gridlayout");
		mainLayout.addComponent(passInfo.getLayout());
		mainLayout.setComponentAlignment(passInfo.getLayout(),
				Alignment.TOP_LEFT);

		final Label lbSpace = new Label();
		mainLayout.addComponent(lbSpace);
		mainLayout.setExpandRatio(lbSpace, 1.0f);

		final HorizontalLayout hlayoutControls = new HorizontalLayout();
		hlayoutControls.setSpacing(true);
		hlayoutControls.setMargin(true);
		final Button cancelBtn = new Button(
				LocalizationHelper
						.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						ContactInfoChangeWindow.this.close();
					}
				});

		cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
		hlayoutControls.addComponent(cancelBtn);
		hlayoutControls.setComponentAlignment(cancelBtn,
				Alignment.MIDDLE_CENTER);

		final Button sendBtn = new Button(
				LocalizationHelper.getMessage(GenericI18Enum.BUTTON_SAVE_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						ContactInfoChangeWindow.this.changeUserInfo();
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

	public boolean validateForm(final Object data) {

		final Set<ConstraintViolation<Object>> violations = this.validation
				.validate(data);
		if (violations.size() > 0) {
			final StringBuilder errorMsg = new StringBuilder();

			for (@SuppressWarnings("rawtypes")
			final ConstraintViolation violation : violations) {
				errorMsg.append(violation.getMessage()).append("<br/>");

				if (violation.getPropertyPath() != null
						&& !violation.getPropertyPath().toString().equals("")) {
					if (violation.getPropertyPath().toString()
							.equals("workphone")) {
						this.txtWorkPhone.addStyleName("errorField");
					}

					if (violation.getPropertyPath().toString()
							.equals("homephone")) {
						this.txtHomePhone.addStyleName("errorField");
					}
				}

			}

			NotificationUtil.showErrorNotification(errorMsg.toString());

			return false;
		}

		return true;
	}

	private void changeUserInfo() {

		this.txtWorkPhone.removeStyleName("errorField");
		this.txtHomePhone.removeStyleName("errorField");

		this.user.setWorkphone(this.txtWorkPhone.getValue());
		this.user.setHomephone(this.txtHomePhone.getValue());
		this.user.setFacebookaccount(this.txtFaceBook.getValue());
		this.user.setTwitteraccount(this.txtTwitter.getValue());
		this.user.setSkypecontact(this.txtSkype.getValue());

		if (this.validateForm(this.user)) {
			final UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			userService.updateWithSession(this.user, AppContext.getUsername());

			EventBus.getInstance().fireEvent(
					new ProfileEvent.GotoProfileView(
							ContactInfoChangeWindow.this, null));
			ContactInfoChangeWindow.this.close();
		}

	}
}
