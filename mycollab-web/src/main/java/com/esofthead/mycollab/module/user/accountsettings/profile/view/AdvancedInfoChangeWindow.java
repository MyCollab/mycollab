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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.CountryComboBox;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
class AdvancedInfoChangeWindow extends Window {
    private TextField txtWebsite = new TextField();
    private TextField txtCompany = new TextField();
    private CountryComboBox cboCountry = new CountryComboBox();

    private final User user;

    AdvancedInfoChangeWindow(final User user) {
        this.user = user;
        this.setWidth("450px");
        this.setResizable(false);
        this.setModal(true);
        this.initUI();
        this.center();
        this.setCaption(AppContext.getMessage(UserI18nEnum.WINDOW_CHANGE_ADVANCED_INFO_TITLE));
    }

    private void initUI() {
        MVerticalLayout mainLayout = new MVerticalLayout().withMargin(new MarginInfo(false, false, true, false)).withWidth("100%");

        GridFormLayoutHelper passInfo = GridFormLayoutHelper.defaultFormLayoutHelper(1, 4);

        passInfo.addComponent(txtWebsite, AppContext.getMessage(UserI18nEnum.FORM_WEBSITE), 0, 0);
        passInfo.addComponent(txtCompany, AppContext.getMessage(UserI18nEnum.FORM_COMPANY), 0, 1);
        passInfo.addComponent(cboCountry, AppContext.getMessage(UserI18nEnum.FORM_COUNTRY), 0, 2);

        this.txtWebsite.setValue(this.user.getWebsite() == null ? ""
                : this.user.getWebsite());
        this.txtCompany.setValue(this.user.getCompany() == null ? ""
                : this.user.getCompany());
        this.cboCountry.setValue(this.user.getCountry() == null ? ""
                : this.user.getCountry());

        mainLayout.addComponent(passInfo.getLayout());
        mainLayout.setComponentAlignment(passInfo.getLayout(),
                Alignment.TOP_LEFT);

        MHorizontalLayout hlayoutControls = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true));

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                AdvancedInfoChangeWindow.this.close();
            }
        });
        cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);

        Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SAVE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                changeInfo();
            }
        });
        saveBtn.setStyleName(UIConstants.BUTTON_ACTION);
        saveBtn.setIcon(FontAwesome.SAVE);

        hlayoutControls.with(saveBtn, cancelBtn).alignAll(Alignment.MIDDLE_CENTER);

        mainLayout.with(hlayoutControls).withAlign(hlayoutControls, Alignment.MIDDLE_RIGHT);

        this.setModal(true);
        this.setContent(mainLayout);
    }

    private void changeInfo() {
        user.setWebsite(this.txtWebsite.getValue());
        user.setCompany(this.txtCompany.getValue());
        user.setCountry((String) this.cboCountry.getValue());

        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        userService.updateWithSession(this.user, AppContext.getUsername());

        EventBusFactory.getInstance().post(new ProfileEvent.GotoProfileView(AdvancedInfoChangeWindow.this, null));
        AdvancedInfoChangeWindow.this.close();

        Page.getCurrent().getJavaScript().execute("window.location.reload();");
    }
}
