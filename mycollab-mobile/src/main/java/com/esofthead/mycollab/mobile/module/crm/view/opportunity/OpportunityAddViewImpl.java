package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.mobile.form.view.DynaFormLayout;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractEditItemComp;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;

@ViewComponent
public class OpportunityAddViewImpl extends
		AbstractEditItemComp<SimpleOpportunity> implements OpportunityAddView {
	private static final long serialVersionUID = -7666059081043542816L;

	@Override
	protected String initFormTitle() {
		return beanItem.getOpportunityname() != null ? beanItem
				.getOpportunityname() : "Add Opportunity";
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.OPPORTUNITY,
				OpportunityDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<SimpleOpportunity> initBeanFormFieldFactory() {
		return new OpportunityEditFormFieldFactory<SimpleOpportunity>(
				this.editForm);
	}

}
