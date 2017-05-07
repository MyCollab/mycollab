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
package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.IBeanList;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ContactSelectionView extends AbstractSelectionView<SimpleContact> {
    private static final long serialVersionUID = 7742786524816492321L;
    private DefaultPagedBeanList<ContactService, ContactSearchCriteria, SimpleContact> itemList;

    private ContactRowDisplayHandler rowHandler = new ContactRowDisplayHandler();

    public ContactSelectionView() {
        createUI();
        this.setCaption(UserUIContext.getMessage(ContactI18nEnum.M_VIEW_CONTACT_NAME_LOOKUP));
    }

    private void createUI() {
        itemList = new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(ContactService.class), new ContactRowDisplayHandler());
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        itemList.search(searchCriteria);

        SimpleContact clearContact = new SimpleContact();
        itemList.addComponentAtTop(rowHandler.generateRow(itemList, clearContact, 0));
    }

    private class ContactRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleContact> {

        @Override
        public Component generateRow(IBeanList<SimpleContact> host, final SimpleContact contact, int rowIndex) {
            return new Button(contact.getContactName(), clickEvent -> {
                selectionField.fireValueChange(contact);
                ContactSelectionView.this.getNavigationManager().navigateBack();
            });
        }
    }
}
