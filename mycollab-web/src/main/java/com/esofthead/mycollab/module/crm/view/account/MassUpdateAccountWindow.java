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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.*;

/**
 *
 * @author MyCollab Ltd.
 * @since 2.0
 *
 */
public class MassUpdateAccountWindow extends MassUpdateWindow<Account> {
    private static final long serialVersionUID = 1L;

    public MassUpdateAccountWindow(String title, AccountListPresenter presenter) {
        super(title, CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT), new Account(), presenter);
    }

    @Override
    protected IFormLayoutFactory buildFormLayoutFactory() {
        return new MassUpdateAccountFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Account> buildBeanFormFieldFactory() {
        return new AccountEditFormFieldFactory<>(updateForm, false);
    }

    private class MassUpdateAccountFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private GridFormLayoutHelper informationLayout;
        private GridFormLayoutHelper addressLayout;

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout formLayout = new VerticalLayout();
            formLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

            Label informationHeader = new Label(AppContext
                    .getMessage(AccountI18nEnum.SECTION_ACCOUNT_INFORMATION));
            informationHeader.setStyleName(UIConstants.H2_STYLE2);
            formLayout.addComponent(informationHeader);

            this.informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            formLayout.addComponent(this.informationLayout.getLayout());

            this.addressLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            Label addressHeader = new Label(AppContext
                    .getMessage(AccountI18nEnum.SECTION_ADDRESS_INFORMATION));
            addressHeader.setStyleName(UIConstants.H2_STYLE2);
            formLayout.addComponent(addressHeader);
            formLayout.addComponent(this.addressLayout.getLayout());

            formLayout.addComponent(buildButtonControls());

            return formLayout;
        }

        @Override
        protected void onAttachField(final Object propertyId, final Field<?> field) {
            if (propertyId.equals("industry")) {
                this.informationLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_INDUSTRY), 0, 0);
            } else if (propertyId.equals("type")) {
                this.informationLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_TYPE), 1, 0);
            } else if (propertyId.equals("ownership")) {
                this.informationLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_OWNERSHIP), 0, 1);
            } else if (propertyId.equals("assignuser")) {
                this.informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 1);
            } else if (propertyId.equals("city")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_BILLING_CITY), 0, 0);
            } else if (propertyId.equals("shippingcity")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_CITY), 1, 0);
            } else if (propertyId.equals("state")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_BILLING_STATE), 0, 1);
            } else if (propertyId.equals("postalcode")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_BILLING_POSTAL_CODE), 1, 1);
            } else if (propertyId.equals("billingcountry")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_BILLING_COUNTRY), 0, 2);
            } else if (propertyId.equals("shippingcountry")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_COUNTRY), 1, 2);
            } else if (propertyId.equals("shippingstate")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_STATE), 0, 3);
            } else if (propertyId.equals("shippingpostalcode")) {
                this.addressLayout.addComponent(field, AppContext.getMessage(AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE), 1, 3);
            }

        }
    }
}
