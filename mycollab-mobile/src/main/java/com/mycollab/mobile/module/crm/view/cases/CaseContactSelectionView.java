/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 *
 */
package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.mycollab.mobile.module.crm.view.contact.ContactListDisplay;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class CaseContactSelectionView extends AbstractRelatedItemSelectionView<SimpleContact, ContactSearchCriteria> {
    private static final long serialVersionUID = 6507450165757528468L;

    public CaseContactSelectionView(AbstractRelatedListView<SimpleContact, ContactSearchCriteria> relatedListView) {
        super(UserUIContext.getMessage(ContactI18nEnum.M_TITLE_SELECT_CONTACTS), relatedListView);
    }

    @Override
    protected void initUI() {
        this.itemList = new ContactListDisplay();
        this.itemList.setRowDisplayHandler((contact, rownIndex) -> {
            final SelectableButton b = new SelectableButton(contact.getContactName());
            if (selections.contains(contact))
                b.select();
            b.addClickListener(clickEvent -> {
                if (b.isSelected())
                    selections.add(contact);
                else
                    selections.remove(contact);
            });
            return b;
        });
    }

}
