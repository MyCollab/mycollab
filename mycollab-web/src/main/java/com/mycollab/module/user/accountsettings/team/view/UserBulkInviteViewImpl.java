package com.mycollab.module.user.accountsettings.team.view;

import com.jarektoro.responsivelayout.ResponsiveColumn;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.Tuple2;
import com.mycollab.core.utils.RandomPasswordGenerator;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.event.UserEvent;
import com.mycollab.module.user.service.UserService;
import com.mycollab.module.user.view.component.RoleComboBox;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PasswordField;
import org.apache.commons.collections4.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.EmailField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 7.0.2
 */
@ViewComponent
public class UserBulkInviteViewImpl extends AbstractVerticalPageView implements UserBulkInviteView {

    private RolePermissionContainer rolePermissionDisplayLayout = new RolePermissionContainer();
    private GridLayout bulkInviteUsersLayout;
    private RoleComboBox roleComboBox;

    public UserBulkInviteViewImpl() {
        this.withMargin(true).withSpacing(true);
    }

    @Override
    public void display() {
        removeAllComponents();
        ELabel titleLbl = ELabel.h2(UserUIContext.getMessage(UserI18nEnum.BULK_INVITE));
        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), (Button.ClickListener) clickEvent -> {
            EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
        }).withStyleName(WebThemes.BUTTON_OPTION);
        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), (Button.ClickListener) clickEvent -> {
            sendInviteBulkUsers();
        }).withStyleName(WebThemes.BUTTON_ACTION);

        this.with(new MHorizontalLayout(titleLbl, cancelBtn, saveBtn).withExpand(titleLbl, 1.0f).withFullWidth());

        roleComboBox = new RoleComboBox();
        roleComboBox.addValueChangeListener((HasValue.ValueChangeListener<SimpleRole>) valueChangeEvent -> {
            displayRolePermission(valueChangeEvent.getSource().getValue());
        });
        this.with(new MHorizontalLayout(ELabel.html(UserUIContext.getMessage(RoleI18nEnum.SINGLE)).withStyleName(WebThemes.META_COLOR), roleComboBox).alignAll(Alignment.TOP_LEFT));
        ResponsiveLayout mainLayout = new ResponsiveLayout().withStyleName(WebThemes.MARGIN_TOP);

        ResponsiveRow mainRow = mainLayout.addRow();
        bulkInviteUsersLayout = new GridLayout(3, 1);
        bulkInviteUsersLayout.setSpacing(true);
        ResponsiveColumn column1 = new ResponsiveColumn(12, 12, 6, 6).withComponent(bulkInviteUsersLayout);
        ResponsiveColumn column2 = new ResponsiveColumn(12, 12, 6, 6).withVisibilityRules(false, false, true, true)
                .withComponent(rolePermissionDisplayLayout);

        mainRow.addColumn(column1);
        mainRow.addColumn(column2);

        bulkInviteUsersLayout.addComponent(ELabel.h3(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL)).withWidth("220px"), 0, 0);
        bulkInviteUsersLayout.addComponent(ELabel.h3(UserUIContext.getMessage(ShellI18nEnum.FORM_PASSWORD)), 1, 0);

        this.with(mainLayout);
        displayRolePermission(roleComboBox.getValue());

        buildInviteUserBlock();
    }

    private void buildInviteUserBlock() {
        EmailField emailField = new EmailField().withPlaceholder(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL)).withWidth("220px");
        PasswordField passwordField = new PasswordField();
        passwordField.setPlaceholder(UserUIContext.getMessage(UserI18nEnum.FORM_PASSWORD_HINT));
        passwordField.setDescription(UserUIContext.getMessage(UserI18nEnum.FORM_PASSWORD_HINT));

        MHorizontalLayout actionLayout = new MHorizontalLayout();
        MButton addBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD), (Button.ClickListener) event -> {
            buildInviteUserBlock();
        }).withStyleName(WebThemes.BUTTON_ACTION);
        actionLayout.with(addBtn);

        if (bulkInviteUsersLayout.getRows() >= 2) {
            MButton removeBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), (Button.ClickListener) event -> {
                GridLayout.Area area = bulkInviteUsersLayout.getComponentArea(actionLayout);
                int row1 = area.getRow1();
                bulkInviteUsersLayout.removeRow(row1);
            }).withStyleName(WebThemes.BUTTON_OPTION);
            actionLayout.with(removeBtn);
        }

        int rows = bulkInviteUsersLayout.getRows();
        bulkInviteUsersLayout.insertRow(rows);

        bulkInviteUsersLayout.addComponent(emailField, 0, rows);
        bulkInviteUsersLayout.addComponent(passwordField, 1, rows);
        bulkInviteUsersLayout.addComponent(actionLayout, 2, rows);
    }

    private void displayRolePermission(SimpleRole role) {
        rolePermissionDisplayLayout.displayRolePermission(role);
    }

    private void sendInviteBulkUsers() {
        int rows = bulkInviteUsersLayout.getRows();
        List<Tuple2<String, String>> tuples = new ArrayList<>();
        for (int i = 1; i < rows; i++) {
            EmailField emailField = (EmailField) bulkInviteUsersLayout.getComponent(0, i);
            PasswordField passwordField = (PasswordField) bulkInviteUsersLayout.getComponent(1, i);

            String email = emailField.getValue();
            String password = passwordField.getValue();
            if (StringUtils.isBlank(email)) {
                continue;
            }
            if (StringUtils.isBlank(password)) {
                password = RandomPasswordGenerator.generateRandomPassword();
            }
            tuples.add(new Tuple2<>(email, password));
        }

        if (CollectionUtils.isNotEmpty(tuples)) {
            UserService userService = AppContextUtil.getSpringBean(UserService.class);
            SimpleRole role = roleComboBox.getValue();
            userService.bulkInviteUsers(tuples, role.getId(), AppUI.getSubDomain(), AppUI.getAccountId(), UserUIContext.getUsername(), true);
        }
        EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
    }
}
