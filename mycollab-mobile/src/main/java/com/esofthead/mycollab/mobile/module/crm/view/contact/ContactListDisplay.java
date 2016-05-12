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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactListDisplay extends DefaultPagedBeanList<ContactService, ContactSearchCriteria, SimpleContact> {
    private static final long serialVersionUID = -2234454107835680053L;

    public ContactListDisplay() {
        super(AppContextUtil.getSpringBean(ContactService.class), new ContactRowDisplayHandler());
    }

    static public class ContactRowDisplayHandler implements RowDisplayHandler<SimpleContact> {

        @Override
        public Component generateRow(final SimpleContact contact, int rowIndex) {
            final Button b = new Button(contact.getContactName(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, contact.getId()));
                }
            });
            b.setWidth("100%");
            return b;
        }

    }
}
