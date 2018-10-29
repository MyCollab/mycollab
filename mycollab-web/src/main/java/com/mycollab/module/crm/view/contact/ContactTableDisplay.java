/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.contact;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class ContactTableDisplay extends DefaultPagedGrid<ContactService, ContactSearchCriteria, SimpleContact> {
    private static final long serialVersionUID = 1L;

    public ContactTableDisplay(List<GridFieldMeta> displayColumns) {
        this(null, displayColumns);
    }

    public ContactTableDisplay(GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        this(null, requiredColumn, displayColumns);

    }

    public ContactTableDisplay(String viewId, GridFieldMeta requiredColumn, List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(ContactService.class), SimpleContact.class, viewId, requiredColumn, displayColumns);

//        addGeneratedColumn("selected", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//            final CheckBoxDecor cb = new CheckBoxDecor("", contact.isSelected());
//            cb.addValueChangeListener(valueChangeEvent -> {
//                fireSelectItemEvent(contact);
//                fireTableEvent(new TableClickEvent(ContactTableDisplay.this, contact, "selected"));
//            });
//            contact.setExtraData(cb);
//            return cb;
//        });
//
//        addGeneratedColumn("contactName", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//
//            LabelLink b = new LabelLink(contact.getContactName(), CrmLinkGenerator.generateContactPreviewLink(contact.getId()));
//            b.setDescription(CrmTooltipGenerator.generateToolTipContact(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
//                    contact, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
//            return b;
//        });
//
//        addGeneratedColumn("createdtime", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//            return new Label(UserUIContext.formatDateTime(contact.getCreatedtime()));
//        });
//
//        addGeneratedColumn("email", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//            return ELabel.email(contact.getEmail());
//        });
//
//        addGeneratedColumn("leadsource", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//            return ELabel.i18n(contact.getLeadsource(), OpportunityLeadSource.class);
//        });
//
//        addGeneratedColumn("accountName", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//            if (contact.getAccountName() != null) {
//                return new LabelLink(contact.getAccountName(), CrmLinkGenerator.generateAccountPreviewLink(contact.getAccountid()));
//            } else {
//                return new Label();
//            }
//        });
//
//        addGeneratedColumn("birthday", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//            return new Label(UserUIContext.formatDate(contact.getBirthday()));
//        });
//
//        addGeneratedColumn("assignUserFullName", (source, itemId, columnId) -> {
//            final SimpleContact contact = getBeanByIndex(itemId);
//            return new UserLink(contact.getAssignuser(), contact.getAssignUserAvatarId(), contact.getAssignUserFullName());
//        });

        this.setWidth("100%");
    }
}
