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

import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.CountryComboBox;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
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
public class AdvancedInfoChangeWindow extends Window {

	private TextField txtWebsite;
	private TextField txtCompany;
	private CountryComboBox cboCountry;

	private final User user;

	public AdvancedInfoChangeWindow(final User user) {
		this.user = user;
		this.setWidth("450px");
		this.initUI();
		this.center();
		this.setCaption("Change your advanced information");
	}

	private void initUI() {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(new MarginInfo(false, false, true, false));
		mainLayout.setSpacing(true);

		final GridFormLayoutHelper passInfo = new GridFormLayoutHelper(1, 4,
				"100%", "150px", Alignment.TOP_LEFT);

		this.txtWebsite = (TextField) passInfo.addComponent(new TextField(),
				"Website", 0, 0);
		this.txtCompany = (TextField) passInfo.addComponent(new TextField(),
				"Company", 0, 1);
		this.cboCountry = (CountryComboBox) passInfo.addComponent(
				new CountryComboBox(), "Country", 0, 2);

		this.txtWebsite.setValue(this.user.getWebsite() == null ? ""
				: this.user.getWebsite());
		this.txtCompany.setValue(this.user.getCompany() == null ? ""
				: this.user.getCompany());
		this.cboCountry.setValue(this.user.getCountry() == null ? ""
				: this.user.getCountry());

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
		final Button cancelBtn = new Button("Cancel",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						AdvancedInfoChangeWindow.this.close();
					}
				});

		cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
		hlayoutControls.addComponent(cancelBtn);
		hlayoutControls.setComponentAlignment(cancelBtn,
				Alignment.MIDDLE_CENTER);

		final Button sendBtn = new Button("Save", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				AdvancedInfoChangeWindow.this.changePassword();
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
		this.user.setWebsite(this.txtWebsite.getValue());
		this.user.setCompany(this.txtCompany.getValue());
		this.user.setCountry((String) this.cboCountry.getValue());

		final UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		userService.updateWithSession(this.user, AppContext.getUsername());

		EventBus.getInstance().fireEvent(
				new ProfileEvent.GotoProfileView(AdvancedInfoChangeWindow.this,
						null));
		AdvancedInfoChangeWindow.this.close();
	}
}
