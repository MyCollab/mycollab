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
package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CampaignStatus;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CampaignType;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
class CampaignReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleCampaign> {
    private static final long serialVersionUID = 1L;

    CampaignReadFormFieldFactory(GenericBeanForm<SimpleCampaign> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        SimpleCampaign campaign = attachForm.getBean();

        if (propertyId.equals("assignuser")) {
            return new DefaultViewField(campaign.getAssignUserFullName());
        } else if (propertyId.equals("startdate")) {
            return new DefaultViewField(UserUIContext.formatDate(campaign.getStartdate()));
        } else if (propertyId.equals("enddate")) {
            return new DefaultViewField(UserUIContext.formatDate(campaign.getEnddate()));
        } else if (propertyId.equals("currencyid")) {
            if (campaign.getCurrencyid() != null) {
                return new DefaultViewField(campaign.getCurrencyid());
            } else {
                return new DefaultViewField("");
            }
        } else if (CampaignWithBLOBs.Field.type.equalTo(propertyId)) {
            return new I18nFormViewField(campaign.getType(), CampaignType.class);
        } else if (CampaignWithBLOBs.Field.status.equalTo(propertyId)) {
            return new I18nFormViewField(campaign.getStatus(), CampaignStatus.class).withStyleName(UIConstants.FIELD_NOTE);
        }

        return null;
    }

}
