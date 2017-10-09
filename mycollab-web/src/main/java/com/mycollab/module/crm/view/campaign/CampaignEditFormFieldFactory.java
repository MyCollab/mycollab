package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.CurrencyComboBoxField;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.DoubleField;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.fields.MTextField;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 3.0
 */
class CampaignEditFormFieldFactory<B extends CampaignWithBLOBs> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    CampaignEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    CampaignEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
        super(form, isValidateForm);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        if ("type".equals(propertyId)) {
            return new CampaignTypeComboBox();
        } else if ("status".equals(propertyId)) {
            return new CampaignStatusComboBox();
        } else if ("campaignname".equals(propertyId)) {
            MTextField tf = new MTextField();
            if (isValidateForm) {
                tf.withNullRepresentation("").withRequired(true)
                        .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                                UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
            }

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
