package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.service.UserService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.EmailField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 7.0.2
 */
@ViewComponent
public class UserBulkInviteViewImpl extends AbstractVerticalPageView implements UserBulkInviteView {

    private UserService userService;

    public UserBulkInviteViewImpl() {
        this.setMargin(true);
        userService = AppContextUtil.getSpringBean(UserService.class);
    }

    @Override
    public void display() {
        removeAllComponents();
        ELabel titleLbl = ELabel.h2(UserUIContext.getMessage(UserI18nEnum.BULK_INVITE));
        this.with(titleLbl);

        MVerticalLayout inviteUsersLayout = new MVerticalLayout();
        inviteUsersLayout.with(buildInviteUserBlock());
        this.with(inviteUsersLayout);
    }

    private MHorizontalLayout buildInviteUserBlock() {
        EmailField emailField = new EmailField().withPlaceholder(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL));
        MButton addBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), (Button.ClickListener) event -> {

        }).withStyleName(WebThemes.BUTTON_ACTION);
        return new MHorizontalLayout(emailField, addBtn);
    }
}
