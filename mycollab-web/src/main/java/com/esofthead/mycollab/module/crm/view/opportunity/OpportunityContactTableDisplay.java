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

import java.util.List;

import com.esofthead.mycollab.common.ui.components.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.service.ContactOpportunityService;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.EmailLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class OpportunityContactTableDisplay
		extends
		DefaultPagedBeanTable<ContactOpportunityService, ContactSearchCriteria, SimpleContactOpportunityRel> {
	private static final long serialVersionUID = 1L;

	public OpportunityContactTableDisplay(List<TableViewField> displayColumns) {
		super(ApplicationContextUtil
				.getSpringBean(ContactOpportunityService.class),
				SimpleContactOpportunityRel.class, displayColumns);

		addGeneratedColumn("contactName", new ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					final Object columnId) {
				final SimpleContact contact = OpportunityContactTableDisplay.this
						.getBeanByIndex(itemId);

				LabelLink b = new LabelLink(contact.getContactName(),
						CrmLinkBuilder.generateContactPreviewLinkFull(contact
								.getId()));
				b.setDescription(CrmTooltipGenerator.generateToolTipContact(
						AppContext.getUserLocale(), contact,
						AppContext.getSiteUrl(), AppContext.getTimezoneId()));
				return b;
			}
		});

		addGeneratedColumn("email", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleContact contact = OpportunityContactTableDisplay.this
						.getBeanByIndex(itemId);
				return new EmailLink(contact.getEmail());
			}
		});

		addGeneratedColumn("accountName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleContact contact = OpportunityContactTableDisplay.this
						.getBeanByIndex(itemId);
				if (contact.getAccountName() != null) {

					LabelLink b = new LabelLink(contact.getAccountName(),
							CrmLinkBuilder
									.generateAccountPreviewLinkFull(contact
											.getAccountid()));
					return b;
				} else {
					return new Label();
				}
			}
		});

	}
}
