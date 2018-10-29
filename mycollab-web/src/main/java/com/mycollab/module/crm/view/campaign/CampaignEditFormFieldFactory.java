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
package com.mycollab.module.crm.view.campaign;

import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.CurrencyComboBoxField;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.DoubleField;
import com.vaadin.data.HasValue;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.fields.MTextField;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
// TODO
class CampaignEditFormFieldFactory<B extends CampaignWithBLOBs> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    CampaignEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    CampaignEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
        super(form, isValidateForm);
    }

    @Override
    protected HasValue<?> onCreateField(Object propertyId) {
        if ("type".equals(propertyId)) {
            return new CampaignTypeComboBox();
        } else if ("status".equals(propertyId)) {
            return new CampaignStatusComboBox();
        } else if ("campaignname".equals(propertyId)) {
            MTextField tf = new MTextField();
//            if (isValidateForm) {
//                tf.withNullRepresentation("").withRequired(true)
//                        .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
//                                UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
//            }

            return tf;
        } else if ("description".equals(propertyId)) {
            return new RichTextArea();
        } else if ("assignuser".equals(propertyId)) {
            return new ActiveUserComboBox();
        } else if (propertyId.equals("currencyid")) {
            return new CurrencyComboBoxField();
        } else if (CampaignWithBLOBs.Field.budget.equalTo(propertyId) || CampaignWithBLOBs.Field.actualcost.equalTo(propertyId) ||
                CampaignWithBLOBs.Field.expectedcost.equalTo(propertyId) || CampaignWithBLOBs.Field.expectedrevenue.equalTo(propertyId)) {
            return new DoubleField();
        }
        return null;
    }
}
