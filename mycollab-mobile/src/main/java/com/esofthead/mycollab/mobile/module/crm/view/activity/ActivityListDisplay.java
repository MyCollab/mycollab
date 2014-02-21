package com.esofthead.mycollab.mobile.module.crm.view.activity;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.mobile.UIConstants;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.EventService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public class ActivityListDisplay extends
DefaultPagedBeanList<EventService, ActivitySearchCriteria, SimpleActivity> {
	private static final long serialVersionUID = -2050012123292483060L;

	public ActivityListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(EventService.class), SimpleActivity.class, displayColumnId);

		addGeneratedColumn("subject", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleActivity simpleEvent = ActivityListDisplay.this
						.getBeanByIndex(itemId);
				Button b = new Button(simpleEvent.getSubject(),
						new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						fireTableEvent(new TableClickEvent(
								ActivityListDisplay.this, simpleEvent,
								"subject"));
					}
				});

				if ("Held".equals(simpleEvent.getStatus())) {
					b.addStyleName(UIConstants.LINK_COMPLETED);
				} else {
					if (simpleEvent.getEndDate() != null
							&& (simpleEvent.getEndDate()
									.before(new GregorianCalendar().getTime()))) {
						b.addStyleName(UIConstants.LINK_OVERDUE);
					}
				}
				b.setWidth("100%");
				return b;

			}
		});
	}

}
