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
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MassUpdateWindow;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class MassUpdateContactWindow extends MassUpdateWindow<Contact> {
	private static final long serialVersionUID = 1L;

	public MassUpdateContactWindow(final String title,
			final ContactListPresenter presenter) {
		super(title, MyCollabResource.newResource("icons/18/crm/contact.png"),
				new Contact(), presenter);
	}

	@Override
	protected IFormLayoutFactory buildFormLayoutFactory() {
		return new MassUpdateContactFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<Contact> buildBeanFormFieldFactory() {
		return new ContactEditFormFieldFactory<Contact>(updateForm, false);
	}

	private class MassUpdateContactFormLayoutFactory implements
			IFormLayoutFactory {
		private static final long serialVersionUID = 1L;

		private GridFormLayoutHelper informationLayout;
		private GridFormLayoutHelper addressLayout;

		@Override
		public Layout getLayout() {
			final VerticalLayout formLayout = new VerticalLayout();
			formLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

			final Label organizationHeader = new Label("Contact Information");
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

			this.addressLayout = new GridFormLayoutHelper(2, 6, "100%",
					"167px", Alignment.TOP_LEFT);
			final Label addressHeader = new Label("Address Information");
			addressHeader.setStyleName(UIConstants.H2_STYLE2);
			formLayout.addComponent(addressHeader);
			this.addressLayout.getLayout().setWidth("100%");
			this.addressLayout.getLayout().setMargin(false);
			this.addressLayout.getLayout().setSpacing(false);
			this.addressLayout.getLayout().addStyleName("colored-gridlayout");
			formLayout.addComponent(this.addressLayout.getLayout());

			formLayout.addComponent(buildButtonControls());

			return formLayout;
		}

		@Override
		public boolean attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("accountid")) {
				this.informationLayout.addComponent(field, "Account", 0, 0);
			} else if (propertyId.equals("title")) {
				this.informationLayout.addComponent(field, "Title", 1, 0);
			} else if (propertyId.equals("leadsource")) {
				this.informationLayout.addComponent(field, "Leader Source", 0,
						1);
			} else if (propertyId.equals("assignuser")) {
				this.informationLayout.addComponent(field, AppContext
						.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD), 1, 1);
			} else if (propertyId.equals("iscallable")) {
				this.informationLayout.addComponent(field, "Callable", 0, 2, 2,
						"100%", Alignment.TOP_LEFT);
			} else if (propertyId.equals("primcity")) {
				this.addressLayout.addComponent(field, "City", 0, 0);
			} else if (propertyId.equals("primstate")) {
				this.addressLayout.addComponent(field, "State", 1, 0);
			} else if (propertyId.equals("primpostalcode")) {
				this.addressLayout.addComponent(field, "Postal Code", 0, 1);
			} else if (propertyId.equals("primcountry")) {
				this.addressLayout.addComponent(field, "Country", 1, 1);
			} else if (propertyId.equals("othercity")) {
				this.addressLayout.addComponent(field, "Other City", 0, 2);
			} else if (propertyId.equals("otherstate")) {
				this.addressLayout.addComponent(field, "Other State", 1, 2);
			} else if (propertyId.equals("otherpostalcode")) {
				this.addressLayout.addComponent(field, "Other Postal Code", 0,
						3);
			} else if (propertyId.equals("othercountry")) {
				this.addressLayout.addComponent(field, "Other Country", 1, 3);
			} else {
				return false;
			}

			return true;
		}
	}
}
