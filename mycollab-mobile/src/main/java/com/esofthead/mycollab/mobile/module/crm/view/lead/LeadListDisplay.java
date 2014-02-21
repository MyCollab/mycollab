package com.esofthead.mycollab.mobile.module.crm.view.lead;

import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class LeadListDisplay extends DefaultPagedBeanList<LeadService, LeadSearchCriteria, SimpleLead> {
	private static final long serialVersionUID = -2350731660593521985L;

	public LeadListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(LeadService.class),
				SimpleLead.class, displayColumnId);

		addGeneratedColumn("leadName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleLead lead = LeadListDisplay.this
						.getBeanByIndex(itemId);

				final Button b = new Button(lead.getLeadName(),
						new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							final Button.ClickEvent event) {
						fireTableEvent(new TableClickEvent(
								LeadListDisplay.this, lead,
								"leadName"));
					}
				});
				b.setWidth("100%");
				return b;
			}
		});
	}

}
