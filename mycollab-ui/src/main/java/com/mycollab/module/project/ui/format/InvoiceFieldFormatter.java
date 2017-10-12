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
package com.mycollab.module.project.ui.format;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.domain.Invoice;
import com.mycollab.module.project.i18n.InvoiceI18nEnum;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.formatter.LocalizationHistoryFieldFormat;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class InvoiceFieldFormatter extends FieldGroupFormatter {
    private static InvoiceFieldFormatter _instance = new InvoiceFieldFormatter();

    private InvoiceFieldFormatter() {
        this.generateFieldDisplayHandler(Invoice.Field.noid.name(), InvoiceI18nEnum.FORM_NOID_FIELD);
        this.generateFieldDisplayHandler(Invoice.Field.issuedate.name(), InvoiceI18nEnum.FORM_ISSUE_DATE_FIELD, DATE_FIELD);
        this.generateFieldDisplayHandler(Invoice.Field.currentid.name(), GenericI18Enum.FORM_CURRENCY, CURRENCY_FIELD);
        this.generateFieldDisplayHandler(Invoice.Field.assignuser.name(), GenericI18Enum.FORM_ASSIGNEE, new
                ProjectMemberHistoryFieldFormat());
        this.generateFieldDisplayHandler(Invoice.Field.status.name(), GenericI18Enum.FORM_STATUS);
        this.generateFieldDisplayHandler(Invoice.Field.contactuserfullname.name(), InvoiceI18nEnum.FORM_CONTACT_PERSON);
        this.generateFieldDisplayHandler(Invoice.Field.type.name(), InvoiceI18nEnum.FORM_TYPE, new
                LocalizationHistoryFieldFormat(InvoiceI18nEnum.class));
        this.generateFieldDisplayHandler(Invoice.Field.amount.name(), InvoiceI18nEnum.FORM_AMOUNT);
        this.generateFieldDisplayHandler(Invoice.Field.note.name(), InvoiceI18nEnum.FORM_NOTE);
        this.generateFieldDisplayHandler(Invoice.Field.description.name(), GenericI18Enum.FORM_DESCRIPTION);
    }

    public static InvoiceFieldFormatter instance() {
        return _instance;
    }
}
