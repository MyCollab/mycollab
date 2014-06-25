/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.mobile.UIConstants;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.EventService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Table;

public class ActivityListDisplay
		extends
		DefaultPagedBeanList<EventService, ActivitySearchCriteria, SimpleActivity> {
	private static final long serialVersionUID = -2050012123292483060L;

	public ActivityListDisplay(String displayColumnId) {
		super(ApplicationContextUtil.getSpringBean(EventService.class),
				SimpleActivity.class, displayColumnId);

		addGeneratedColumn("subject", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleActivity simpleEvent = ActivityListDisplay.this
						.getBeanByIndex(itemId);
				NavigationButton b = new NavigationButton(simpleEvent
						.getSubject());
				b.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent event) {
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
