package com.mycollab.module.crm.view.account;

import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountSelectionField extends CustomField<Integer> implements FieldSelection<Account> {
    private static final long serialVersionUID = 1L;

    private TextField accountName = new TextField();
    private Account account = null;

    private void clearValue() {
        accountName.setValue("");
        this.account = null;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof Integer) {
            setAccountByVal((Integer) value);
            super.setPropertyDataSource(newDataSource);
        } else {
            super.setPropertyDataSource(newDataSource);
        }
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        if (account != null) {
            setInternalValue(account.getId());
        } else {
            setInternalValue(null);
        }
        super.commit();
    }

    private void setAccountByVal(Integer accountId) {
        AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
        SimpleAccount account = accountService.findById(accountId, AppUI.getAccountId());
        if (account != null) {
            this.account = account;
            accountName.setValue(account.getAccountname());
        }
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public void fireValueChange(Account data) {
        account = data;
        if (account != null) {
            accountName.setValue(account.getAccountname());
            setInternalValue(account.getId());
        }
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth()
                .withDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        accountName.setNullRepresentation("");
        accountName.setEnabled(true);
        accountName.setWidth("100%");

        MButton browseBtn = new MButton("", clickEvent -> {
            AccountSelectionWindow accountWindow = new AccountSelectionWindow(AccountSelectionField.this);
            UI.getCurrent().addWindow(accountWindow);
            accountWindow.show();
        }).withIcon(FontAwesome.ELLIPSIS_H).withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);

        MButton clearBtn = new MButton("", clickEvent -> clearValue()).withIcon(FontAwesome.TRASH_O)
                .withStyleName(WebThemes.BUTTON_OPTION, WebThemes.BUTTON_SMALL_PADDING);

        layout.with(accountName, browseBtn, clearBtn).expand(accountName);
        return layout;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
