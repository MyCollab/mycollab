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

package com.esofthead.mycollab.module.crm.view.lead;

import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UrlLink;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LeadTableDisplay extends DefaultPagedBeanTable<LeadService, LeadSearchCriteria, SimpleLead> {

	public LeadTableDisplay(List<TableViewField> displayColumns) {
		this(null, displayColumns);
	}

	public LeadTableDisplay(TableViewField requiredColumn, List<TableViewField> displayColumns) {
		this(null, requiredColumn, displayColumns);

	}

	public LeadTableDisplay(String viewId, TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		super(ApplicationContextUtil.getSpringBean(LeadService.class),
				SimpleLead.class, viewId, requiredColumn, displayColumns);

		this.addGeneratedColumn("selected", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final SimpleLead lead = getBeanByIndex(itemId);
				final CheckBoxDecor cb = new CheckBoxDecor("", lead
						.isSelected());
				cb.setImmediate(true);
				cb.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						LeadTableDisplay.this.fireSelectItemEvent(lead);

						fireTableEvent(new TableClickEvent(
								LeadTableDisplay.this, lead, "selected"));

					}
				});

				lead.setExtraData(cb);
				return cb;
			}
		});

		this.addGeneratedColumn("leadName", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final SimpleLead lead = getBeanByIndex(itemId);

				LabelLink b = new LabelLink(lead.getLeadName(), CrmLinkBuilder
						.generateLeadPreviewLinkFull(lead.getId()));
				if ("Dead".equals(lead.getStatus())
						|| "Converted".equals(lead.getStatus())) {
					b.addStyleName(UIConstants.LINK_COMPLETED);
				}
				b.setDescription(CrmTooltipGenerator.generateTooltipLead(
						AppContext.getUserLocale(), lead,
						AppContext.getSiteUrl(), AppContext.getUserTimezone()));
				return b;
			}
		});

		this.addGeneratedColumn("assignUserFullName",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(Table source,
							final Object itemId, Object columnId) {
						final SimpleLead lead = getBeanByIndex(itemId);
						UserLink b = new UserLink(lead.getAssignuser(), lead
								.getAssignUserAvatarId(), lead
								.getAssignUserFullName());
						return b;

					}
				});

		this.addGeneratedColumn("email", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					Object itemId, Object columnId) {
				final SimpleLead lead = getBeanByIndex(itemId);
				Link l = new Link();
				l.setResource(new ExternalResource("mailto:" + lead.getEmail()));
				l.setCaption(lead.getEmail());
				return l;

			}
		});

		this.addGeneratedColumn("website", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					Object itemId, Object columnId) {
				final SimpleLead lead = getBeanByIndex(itemId);
				if (lead.getWebsite() != null) {
					return new UrlLink(lead.getWebsite());
				} else {
					return new Label("");
				}

			}
		});

		this.setWidth("100%");
	}
}
