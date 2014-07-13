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
package com.esofthead.mycollab.module.crm.view.account;

import java.util.Arrays;
import java.util.List;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.iexporter.CSVObjectEntityConverter.FieldMapperDef;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.ui.components.EntityImportWindow;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class AccountImportWindow extends EntityImportWindow<Account> {
	private static final long serialVersionUID = 1L;

	public AccountImportWindow() {
		super(false, "Import Account Window", ApplicationContextUtil
				.getSpringBean(AccountService.class), Account.class);
	}

	@Override
	protected List<FieldMapperDef> constructCSVFieldMapper() {
		FieldMapperDef[] fields = {
				new FieldMapperDef("accountname", "Account Name"),
				new FieldMapperDef("website", "Website"),
				new FieldMapperDef("phoneoffice",
						AppContext
								.getMessage(AccountI18nEnum.FORM_OFFICE_PHONE)),
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
				new FieldMapperDef("assignuser",
						AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)),
				new FieldMapperDef("type", "Type"),
				new FieldMapperDef("industry", "Industry"),
				new FieldMapperDef("billingcountry", "Billing Country"),
				new FieldMapperDef("shippingcountry", "Shipping Country"),
				new FieldMapperDef("description", "Description") };
		return Arrays.asList(fields);
	}

	@Override
	protected void reloadWhenBackToListView() {
		EventBusFactory.getInstance().post(
				new AccountEvent.GotoList(AccountListView.class,
						new AccountSearchCriteria()));
	}

}