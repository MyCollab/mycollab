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

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.ui.components.DynaFormLayout;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class OpportunityAddViewImpl extends
		AbstractEditItemComp<SimpleOpportunity> implements OpportunityAddView {
	private static final long serialVersionUID = 1L;

	@Override
	protected String initFormTitle() {
		return (beanItem.getId() == null) ? AppContext
				.getMessage(OpportunityI18nEnum.VIEW_NEW_TITLE) : beanItem
				.getOpportunityname();
	}

	@Override
	protected Resource initFormIconResource() {
		return MyCollabResource.newResource(WebResourceIds._22_crm_opportunity);
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new EditFormControlsGenerator<>(editForm)
				.createButtonControls();
	}

	@Override
	protected AdvancedEditBeanForm<SimpleOpportunity> initPreviewForm() {
		return new AdvancedEditBeanForm<>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.OPPORTUNITY,
				OpportunityDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<SimpleOpportunity> initBeanFormFieldFactory() {
		return new OpportunityEditFormFieldFactory<>(editForm);
	}
}
