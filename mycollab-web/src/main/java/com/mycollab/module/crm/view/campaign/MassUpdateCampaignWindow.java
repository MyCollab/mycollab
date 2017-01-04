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
package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
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
public class MassUpdateCampaignWindow extends MassUpdateWindow<CampaignWithBLOBs> {
    private static final long serialVersionUID = 1L;

    public MassUpdateCampaignWindow(final String title, final CampaignListPresenter presenter) {
        super(title, CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN), new CampaignWithBLOBs(), presenter);
    }

    @Override
    protected AbstractFormLayoutFactory buildFormLayoutFactory() {
        return new MassUpdateContactFormLayoutFactory();
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<CampaignWithBLOBs> buildBeanFormFieldFactory() {
        return new CampaignEditFormFieldFactory<>(updateForm, false);
    }

    private class MassUpdateContactFormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        private GridFormLayoutHelper informationLayout;
        private GridFormLayoutHelper campaignGoal;

        @Override
        public AbstractComponent getLayout() {
            final FormContainer formLayout = new FormContainer();

            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            formLayout.addSection(UserUIContext.getMessage(CampaignI18nEnum.SECTION_CAMPAIGN_INFORMATION), informationLayout.getLayout());

            campaignGoal = GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
            formLayout.addSection(UserUIContext.getMessage(CampaignI18nEnum.SECTION_GOAL), campaignGoal.getLayout());

            formLayout.addComponent(buildButtonControls());

            return formLayout;
        }

        @Override
        protected Component onAttachField(Object propertyId, final Field<?> field) {
            if (propertyId.equals("assignuser")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 0);
            } else if (propertyId.equals("status")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_STATUS), 1, 0);
            } else if (propertyId.equals("type")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_TYPE), 0, 1);
            } else if (propertyId.equals("currencyid")) {
                return campaignGoal.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_CURRENCY), 0, 0);
            }
            return null;
        }
    }
}
