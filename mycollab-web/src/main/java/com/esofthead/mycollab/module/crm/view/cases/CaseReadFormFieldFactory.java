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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.field.EmailViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.LinkViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.RichTextViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class CaseReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCase> {
    private static final long serialVersionUID = 1L;

    public CaseReadFormFieldFactory(GenericBeanForm<SimpleCase> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        final SimpleCase cases = attachForm.getBean();
        if (propertyId.equals("accountid")) {
            return new LinkViewField(cases.getAccountName(),
                    CrmLinkBuilder.generateAccountPreviewLinkFull(cases.getAccountid()),
                    CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
        } else if (propertyId.equals("email")) {
            return new EmailViewField(cases.getEmail());
        } else if (propertyId.equals("assignuser")) {
            return new UserLinkViewField(cases.getAssignuser(),
                    cases.getAssignUserAvatarId(),
                    cases.getAssignUserFullName());
        } else if (propertyId.equals("description")) {
            return new RichTextViewField(cases.getDescription());
        }
        return null;
    }
}
