package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.mobile.UIConstants;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CaseListDisplay extends DefaultPagedBeanList<CaseService, CaseSearchCriteria, SimpleCase> {
	private static final long serialVersionUID = -5865353122197825948L;

	public CaseListDisplay(	String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(CaseService.class), SimpleCase.class, displayColumnId);

		addGeneratedColumn("subject", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final SimpleCase cases = CaseListDisplay.this
						.getBeanByIndex(itemId);
				Button b = new Button(cases.getSubject(),
						new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireTableEvent(new TableClickEvent(
								CaseListDisplay.this, cases, "subject"));
					}
				});

				if ("Closed".equals(cases.getStatus())
						|| "Rejected".equals(cases.getStatus())) {
					b.addStyleName(UIConstants.LINK_COMPLETED);
				}
				b.setWidth("100%");
				return b;
			}
		});
	}

}
