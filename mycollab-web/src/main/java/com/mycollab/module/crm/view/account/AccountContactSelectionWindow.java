/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.fielddef.ContactTableFieldDef;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.contact.ContactSearchPanel;
import com.mycollab.module.crm.view.contact.ContactTableDisplay;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.Button;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class AccountContactSelectionWindow extends RelatedItemSelectionWindow<SimpleContact, ContactSearchCriteria> {

    public AccountContactSelectionWindow(AccountContactListComp associateContactList) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(AccountI18nEnum.LIST)), associateContactList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        this.tableItem = new ContactTableDisplay(ContactTableFieldDef.selected, Arrays.asList(ContactTableFieldDef.name,
                ContactTableFieldDef.title, ContactTableFieldDef.account, ContactTableFieldDef.phoneOffice));

//        gridItem.addGeneratedColumn("contactName", (source, itemId, columnId) -> {
//            final SimpleContact contact = gridItem.getBeanByIndex(itemId);
//
//            return new ELabel(contact.getContactName()).withStyleName(WebThemes.BUTTON_LINK)
//                    .withDescription(CrmTooltipGenerator.generateToolTipContact
//                            (UserUIContext.getUserLocale(), AppUI.getDateFormat(),
//                                    contact, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
//        });

        Button selectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close());
        selectBtn.setStyleName(WebThemes.BUTTON_ACTION);

        ContactSearchPanel contactSimpleSearchPanel = new ContactSearchPanel();
        contactSimpleSearchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        this.bodyContent.with(contactSimpleSearchPanel, selectBtn, tableItem);
    }
}