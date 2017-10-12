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
package com.mycollab.mobile.module.crm.view.contact;

import com.hp.gagawa.java.elements.A;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.service.ContactService;
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
public class ContactListDisplay extends DefaultPagedBeanList<ContactService, ContactSearchCriteria, SimpleContact> {
    private static final long serialVersionUID = -2234454107835680053L;

    public ContactListDisplay() {
        super(AppContextUtil.getSpringBean(ContactService.class), new ContactRowDisplayHandler());
    }

    private static class ContactRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleContact> {

        @Override
        public Component generateRow(IBeanList<SimpleContact> host, final SimpleContact contact, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withSpacing(false).withFullWidth();
            A itemLink = new A(CrmLinkBuilder.generateContactPreviewLinkFull(contact.getId())).appendText(contact.getContactName());
            MCssLayout itemWrap = new MCssLayout(ELabel.html(itemLink.write()));
            rowLayout.addComponent(new MHorizontalLayout(ELabel.fontIcon(CrmAssetsManager.getAsset
                    (CrmTypeConstants.CONTACT)), itemWrap).expand(itemWrap).withFullWidth());
            return rowLayout;
        }

    }
}
