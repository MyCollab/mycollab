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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.FormContainer;
import com.esofthead.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.esofthead.mycollab.vaadin.web.ui.MassUpdateWindow;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class MassUpdateOpportunityWindow extends MassUpdateWindow<Opportunity> {
    private static final long serialVersionUID = 1L;

    public MassUpdateOpportunityWindow(final String title, final OpportunityListPresenter presenter) {
        super(title, CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY), new Opportunity(), presenter);
    }

    @Override
    protected AbstractFormLayoutFactory buildFormLayoutFactory() {
        return new MassUpdateOpportunityFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Opportunity> buildBeanFormFieldFactory() {
        return new OpportunityEditFormFieldFactory<>(updateForm, false);
    }

    private class MassUpdateOpportunityFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private GridFormLayoutHelper informationLayout;

        @Override
        public ComponentContainer getLayout() {
            final FormContainer formLayout = new FormContainer();
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            formLayout.addSection(AppContext.getMessage(OpportunityI18nEnum.SECTION_OPPORTUNITY_INFORMATION), informationLayout.getLayout());
            formLayout.addComponent(buildButtonControls());
            return formLayout;
        }

        @Override
        protected Component onAttachField(Object propertyId, final Field<?> field) {
            if (propertyId.equals("opportunityname")) {
                return informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0);
            } else if (propertyId.equals("currencyid")) {
                return informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_CURRENCY), 0, 1);
            } else if (propertyId.equals("amount")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_AMOUNT), 0, 2);
            } else if (propertyId.equals("salesstage")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE), 0, 3);
            } else if (propertyId.equals("probability")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_PROBABILITY), 0, 4);
            } else if (propertyId.equals("nextstep")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_NEXT_STEP), 0, 5);
            } else if (propertyId.equals("accountid")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_ACCOUNT_NAME), 1, 0);
            } else if (propertyId.equals("expectedcloseddate")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE), 1, 1);
            } else if (propertyId.equals("opportunitytype")) {
                return informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_TYPE), 1, 2);
            } else if (propertyId.equals("source")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_LEAD_SOURCE), 1, 3);
            } else if (propertyId.equals("campaignid")) {
                return informationLayout.addComponent(field, AppContext.getMessage(OpportunityI18nEnum.FORM_CAMPAIGN_NAME), 1, 4);
            } else if (propertyId.equals("assignuser")) {
                return informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 5);
            }
            return null;
        }
    }
}
