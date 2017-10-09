package com.mycollab.mobile.module.crm.view.account;

import com.hp.gagawa.java.elements.A;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountListDisplay extends DefaultPagedBeanList<AccountService, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = 1491890029721763281L;

    public AccountListDisplay() {
        super(AppContextUtil.getSpringBean(AccountService.class), new AccountRowDisplayHandler());
    }

    private static class AccountRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleAccount> {

        @Override
        public Component generateRow(IBeanList<SimpleAccount> host, final SimpleAccount account, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
            A accountLink = new A(CrmLinkBuilder.generateAccountPreviewLinkFull(account.getId())).appendText(account.getAccountname());
            MCssLayout accountWrap = new MCssLayout(ELabel.html(accountLink.write()));
            rowLayout.addComponent(new MHorizontalLayout(ELabel.fontIcon(CrmAssetsManager.getAsset
                    (CrmTypeConstants.ACCOUNT)), accountWrap).expand(accountWrap).withFullWidth());
            return rowLayout;
        }
    }
}
