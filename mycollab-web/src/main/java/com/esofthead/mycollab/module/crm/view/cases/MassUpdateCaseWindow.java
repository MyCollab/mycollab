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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.CaseWithBLOBs;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;
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
public class MassUpdateCaseWindow extends MassUpdateWindow<CaseWithBLOBs> {
	private static final long serialVersionUID = 1L;

	public MassUpdateCaseWindow(String title, CaseListPresenter presenter) {
		super(title, CrmAssetsManager.getAsset(CrmTypeConstants.CASE), new CaseWithBLOBs(), presenter);
	}

	@Override
	protected IFormLayoutFactory buildFormLayoutFactory() {
		return new MassUpdateContactFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<CaseWithBLOBs> buildBeanFormFieldFactory() {
		return new CaseEditFormFieldFactory<>(updateForm, false);
	}

	private class MassUpdateContactFormLayoutFactory implements IFormLayoutFactory {
		private static final long serialVersionUID = 1L;

		private GridFormLayoutHelper informationLayout;

		@Override
		public ComponentContainer getLayout() {
			VerticalLayout formLayout = new VerticalLayout();
			formLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

			Label organizationHeader = new Label(AppContext.getMessage(CaseI18nEnum.SECTION_CASE_INFORMATION));
			organizationHeader.setStyleName(UIConstants.H2_STYLE2);
			formLayout.addComponent(organizationHeader);

			informationLayout =  GridFormLayoutHelper.defaultFormLayoutHelper(2, 6);
			formLayout.addComponent(informationLayout.getLayout());

			formLayout.addComponent(buildButtonControls());

			return formLayout;
		}

		// priority, status, account name, origin, type, reason, assignuser
		@Override
		public void attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("priority")) {
				this.informationLayout.addComponent(field, "Priority", 0, 0);
			} else if (propertyId.equals("status")) {
				this.informationLayout.addComponent(field, "Status", 1, 0);
			} else if (propertyId.equals("accountid")) {
				this.informationLayout.addComponent(field, "Account Name", 0, 1);
			} else if (propertyId.equals("origin")) {
				this.informationLayout.addComponent(field, "Origin", 1, 1);
			} else if (propertyId.equals("type")) {
				this.informationLayout.addComponent(field, "Type", 0, 2);
			} else if (propertyId.equals("reason")) {
				this.informationLayout.addComponent(field, "Reason", 1, 2);
			} else if (propertyId.equals("assignuser")) {
				this.informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 3, 2, "297px");
			}
		}
	}
}