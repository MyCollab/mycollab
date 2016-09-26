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
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.iexporter.CSVObjectEntityConverter.FieldMapperDef;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.event.AccountEvent;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.ui.components.EntityImportWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountImportWindow extends EntityImportWindow<Account> {
    private static final long serialVersionUID = 1L;

    public AccountImportWindow() {
        super(false, "Import Accounts", AppContextUtil.getSpringBean(AccountService.class), Account.class);
    }

    @Override
    protected List<FieldMapperDef> constructCSVFieldMapper() {
        FieldMapperDef[] fields = {
                new FieldMapperDef("accountname", "Account Name"),
                new FieldMapperDef("website", "Website"),
                new FieldMapperDef("phoneoffice", UserUIContext.getMessage(AccountI18nEnum.FORM_OFFICE_PHONE)),
                new FieldMapperDef("fax", "Fax"),
                new FieldMapperDef("alternatephone", "Alternate Phone"),
                new FieldMapperDef("annualrevenue", "Annual Revenue"),
                new FieldMapperDef("billingaddress", "Billing Address"),
                new FieldMapperDef("city", "City"),
                new FieldMapperDef("postalcode", "Postal Code"),
                new FieldMapperDef("state", "State"),
                new FieldMapperDef("email", "Email"),
                new FieldMapperDef("ownership", "Ownership"),
                new FieldMapperDef("shippingaddress", "Shipping Address"),
                new FieldMapperDef("shippingcity", "Shipping City"),
                new FieldMapperDef("shippingpostalcode", "Shipping Postal Code"),
                new FieldMapperDef("shippingstate", "Shipping State"),
                new FieldMapperDef("numemployees", "Number Employees"),
                new FieldMapperDef("assignuser", UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)),
                new FieldMapperDef("type", "Type"),
                new FieldMapperDef("industry", "Industry"),
                new FieldMapperDef("billingcountry", "Billing Country"),
                new FieldMapperDef("shippingcountry", "Shipping Country"),
                new FieldMapperDef("description", "Description")};
        return Arrays.asList(fields);
    }

    @Override
    protected void reloadWhenBackToListView() {
        EventBusFactory.getInstance().post(new AccountEvent.GotoList(AccountListView.class, new AccountSearchCriteria()));
    }

}