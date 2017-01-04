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
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.web.ui.MassUpdateWindow;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class MassUpdateLeadWindow extends MassUpdateWindow<Lead> {
    private static final long serialVersionUID = 1L;

    public MassUpdateLeadWindow(final String title, final LeadListPresenter presenter) {
        super(title, CrmAssetsManager.getAsset(CrmTypeConstants.LEAD), new Lead(), presenter);
    }

    @Override
    protected AbstractFormLayoutFactory buildFormLayoutFactory() {
        return new MassUpdateLeadFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Lead> buildBeanFormFieldFactory() {
        return new LeadEditFormFieldFactory<>(updateForm, false);
    }

    private class MassUpdateLeadFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private GridFormLayoutHelper informationLayout;
        private GridFormLayoutHelper addressLayout;

        @Override
        public AbstractComponent getLayout() {
            final FormContainer formLayout = new FormContainer();

            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            formLayout.addSection(UserUIContext.getMessage(LeadI18nEnum.SECTION_LEAD_INFORMATION), informationLayout.getLayout());

            addressLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            formLayout.addSection(UserUIContext.getMessage(LeadI18nEnum.SECTION_ADDRESS), addressLayout.getLayout());

            formLayout.addComponent(buildButtonControls());

            return formLayout;
        }

        // Title, Account Name, Lead Source, Industry, Status, Assign User,
        // primary/other city, primary/other state, primary/other postal
        // code, primary/other country
        @Override
        protected Component onAttachField(Object propertyId, final Field<?> field) {
            if (propertyId.equals("title")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_TITLE), 0, 0);
            } else if (propertyId.equals("accountname")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_ACCOUNT_NAME), 1, 0);
            } else if (propertyId.equals("source")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_LEAD_SOURCE), 0, 1);
            } else if (propertyId.equals("industry")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_INDUSTRY), 1, 1);
            } else if (propertyId.equals("status")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_STATUS), 0, 2);
            } else if (propertyId.equals("assignuser")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 2);
            } else if (propertyId.equals("primcity")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_PRIMARY_CITY), 0, 0);
            } else if (propertyId.equals("primstate")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_PRIMARY_STATE), 0, 1);
            } else if (propertyId.equals("primpostalcode")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE), 0, 2);
            } else if (propertyId.equals("primcountry")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_PRIMARY_COUNTRY), 0, 3);
            } else if (propertyId.equals("othercity")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_OTHER_CITY), 1, 0);
            } else if (propertyId.equals("otherstate")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_OTHER_STATE), 1, 1);
            } else if (propertyId.equals("otherpostalcode")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_OTHER_POSTAL_CODE), 1, 2);
            } else if (propertyId.equals("othercountry")) {
                return addressLayout.addComponent(field, UserUIContext.getMessage(LeadI18nEnum.FORM_OTHER_COUNTRY), 1, 3);
            }
            return null;
        }
    }
}
