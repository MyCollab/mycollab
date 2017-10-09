package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.fielddef.AccountTableFieldDef;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.account.AccountSearchPanel;
import com.mycollab.module.crm.view.account.AccountTableDisplay;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignAccountSelectionWindow extends RelatedItemSelectionWindow<SimpleAccount, AccountSearchCriteria> {
    private static final long serialVersionUID = 1L;

    public CampaignAccountSelectionWindow(CampaignAccountListComp associateAccountList) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(AccountI18nEnum.LIST)),
                associateAccountList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        tableItem = new AccountTableDisplay(AccountTableFieldDef.selected,
                Arrays.asList(AccountTableFieldDef.accountname, AccountTableFieldDef.phoneoffice,
                        AccountTableFieldDef.email, AccountTableFieldDef.city));

        tableItem.addGeneratedColumn("accountname", (source, itemId, columnId) -> {
            SimpleAccount account = tableItem.getBeanByIndex(itemId);
            return new ELabel(account.getAccountname()).withStyleName(WebThemes.BUTTON_LINK)
                    .withDescription(CrmTooltipGenerator.generateToolTipAccount(UserUIContext.getUserLocale(), account,
                            AppUI.getSiteUrl()));
        });

        MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_ACTION);

        AccountSearchPanel accountSimpleSearchPanel = new AccountSearchPanel(false);
        accountSimpleSearchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        bodyContent.with(accountSimpleSearchPanel, selectBtn, tableItem);
    }
}
