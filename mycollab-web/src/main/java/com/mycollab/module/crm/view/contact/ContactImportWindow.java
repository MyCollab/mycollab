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
package com.mycollab.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.iexporter.CSVObjectEntityConverter.FieldMapperDef;
import com.mycollab.iexporter.csv.CSVBooleanFormatter;
import com.mycollab.iexporter.csv.CSVDateFormatter;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.events.ContactEvent;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.ui.components.EntityImportWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactImportWindow extends EntityImportWindow<Contact> {
    private static final long serialVersionUID = 1L;

    public ContactImportWindow() {
        super(true, "Import Contacts", AppContextUtil.getSpringBean(ContactService.class), Contact.class);
    }

    @Override
    protected List<FieldMapperDef> constructCSVFieldMapper() {
        FieldMapperDef[] fields = {
                new FieldMapperDef("firstname", "First Name"),
                new FieldMapperDef("lastname", "Last Name"),
                new FieldMapperDef("account", "Account"),
                new FieldMapperDef("title", "Title"),
                new FieldMapperDef("department", "Department"),
                new FieldMapperDef("email", "Email"),
                new FieldMapperDef("assistant", "Assistant"),
                new FieldMapperDef("assistantphone", "Assistant Phone"),
                new FieldMapperDef("leadsource", "Leader Source"),
                new FieldMapperDef("officephone", UserUIContext
                        .getMessage(ContactI18nEnum.FORM_OFFICE_PHONE)),
                new FieldMapperDef("mobile", "Mobile"),
                new FieldMapperDef("homephone", "Home Phone"),
                new FieldMapperDef("otherphone", "Other Phone"),
                new FieldMapperDef("fax", "Fax"),
                new FieldMapperDef("birthday", "Birthday", new CSVDateFormatter()),
                new FieldMapperDef("iscallable", "Callable", new CSVBooleanFormatter()),
                new FieldMapperDef("assignuser", UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)),
                new FieldMapperDef("primaddress", "Address"),
                new FieldMapperDef("primcity", "City"),
                new FieldMapperDef("primstate", "State"),
                new FieldMapperDef("primpostalcode", "Postal Code"),
                new FieldMapperDef("primcountry", "Country"),
                new FieldMapperDef("otheraddress", "Other Address"),
                new FieldMapperDef("othercity", "Other City"),
                new FieldMapperDef("otherstate", "Other State"),
                new FieldMapperDef("otherpostalcode", "Other Postal Code"),
                new FieldMapperDef("othercountry", "Other Country"),
                new FieldMapperDef("description", "Description")};
        return Arrays.asList(fields);
    }

    @Override
    protected void reloadWhenBackToListView() {
        ContactSearchCriteria contactSearchCriteria = new ContactSearchCriteria();
        contactSearchCriteria.setContactName(StringSearchField.and(""));
        EventBusFactory.getInstance().post(new ContactEvent.GotoList(ContactListView.class, contactSearchCriteria));
    }
}
