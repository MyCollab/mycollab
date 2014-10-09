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
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MassUpdateWindow;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class MassUpdateOpportunityWindow extends MassUpdateWindow<Opportunity> {
	private static final long serialVersionUID = 1L;

	public MassUpdateOpportunityWindow(final String title,
			final OpportunityListPresenter presenter) {
		super(title, MyCollabResource
				.newResource("icons/18/crm/opportunity.png"),
				new Opportunity(), presenter);
	}

	@Override
	protected IFormLayoutFactory buildFormLayoutFactory() {
		return new MassUpdateOpportunityFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<Opportunity> buildBeanFormFieldFactory() {
		return new OpportunityEditFormFieldFactory<Opportunity>(updateForm,
				false);
	}

	private class MassUpdateOpportunityFormLayoutFactory implements
			IFormLayoutFactory {
		private static final long serialVersionUID = 1L;

		private GridFormLayoutHelper informationLayout;

		@Override
		public ComponentContainer getLayout() {
			final VerticalLayout formLayout = new VerticalLayout();
			formLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

			final Label organizationHeader = new Label(
					AppContext
							.getMessage(OpportunityI18nEnum.SECTION_OPPORTUNITY_INFORMATION));
			organizationHeader.setStyleName(UIConstants.H2_STYLE2);
			formLayout.addComponent(organizationHeader);

			this.informationLayout = new GridFormLayoutHelper(2, 6, "100%",
					"167px", Alignment.TOP_LEFT);

			this.informationLayout.getLayout().setWidth("100%");
			this.informationLayout.getLayout().setMargin(false);
			this.informationLayout.getLayout().setSpacing(false);
			this.informationLayout.getLayout().addStyleName(
					"colored-gridlayout");
			formLayout.addComponent(this.informationLayout.getLayout());

			formLayout.addComponent(buildButtonControls());

			return formLayout;
		}

		@Override
		public void attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("opportunityname")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(OpportunityI18nEnum.FORM_NAME),
						0, 0);
			} else if (propertyId.equals("currencyid")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(OpportunityI18nEnum.FORM_CURRENCY), 0, 1);
			} else if (propertyId.equals("amount")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(OpportunityI18nEnum.FORM_AMOUNT),
						0, 2);
			} else if (propertyId.equals("salesstage")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE), 0, 3);
			} else if (propertyId.equals("probability")) {
				this.informationLayout
						.addComponent(
								field,
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_PROBABILITY),
								0, 4);
			} else if (propertyId.equals("nextstep")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(OpportunityI18nEnum.FORM_NEXT_STEP), 0, 5);
			} else if (propertyId.equals("accountid")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(OpportunityI18nEnum.FORM_ACCOUNT_NAME), 1,
						0);
			} else if (propertyId.equals("expectedcloseddate")) {
				this.informationLayout
						.addComponent(
								field,
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE),
								1, 1);
			} else if (propertyId.equals("opportunitytype")) {
				this.informationLayout.addComponent(field,
						AppContext.getMessage(OpportunityI18nEnum.FORM_TYPE),
						1, 2);
			} else if (propertyId.equals("source")) {
				this.informationLayout
						.addComponent(
								field,
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_LEAD_SOURCE),
								1, 3);
			} else if (propertyId.equals("campaignid")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(OpportunityI18nEnum.FORM_CAMPAIGN_NAME), 1,
						4);
			} else if (propertyId.equals("assignuser")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 5);
			}
		}
	}
}
