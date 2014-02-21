package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class OpportunityListDisplay extends DefaultPagedBeanList<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
	private static final long serialVersionUID = -2350731660593521985L;

	public OpportunityListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(OpportunityService.class),
				SimpleOpportunity.class, displayColumnId);

		addGeneratedColumn("opportunityname", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleOpportunity opportunity = OpportunityListDisplay.this
						.getBeanByIndex(itemId);

				final Button b = new Button(opportunity.getOpportunityname(),
						new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							final Button.ClickEvent event) {
						fireTableEvent(new TableClickEvent(
								OpportunityListDisplay.this, opportunity,
								"opportunityname"));
					}
				});
				b.setWidth("100%");
				return b;
			}
		});
	}

}
