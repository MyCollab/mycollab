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

import java.util.GregorianCalendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class OpportunityTableDisplay
		extends
		DefaultPagedBeanTable<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
	private static Logger log = LoggerFactory
			.getLogger(OpportunityTableDisplay.class);

	public OpportunityTableDisplay(List<TableViewField> displayColumns) {
		this(null, displayColumns);
	}

	public OpportunityTableDisplay(TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		this(null, requiredColumn, displayColumns);

	}

	public OpportunityTableDisplay(String viewId,
			TableViewField requiredColumn, List<TableViewField> displayColumns) {
		super(ApplicationContextUtil.getSpringBean(OpportunityService.class),
				SimpleOpportunity.class, viewId, requiredColumn, displayColumns);

		this.addGeneratedColumn("selected", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final CheckBoxDecor cb = new CheckBoxDecor("", false);
				cb.setImmediate(true);
				cb.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						SimpleOpportunity opportunity = OpportunityTableDisplay.this
								.getBeanByIndex(itemId);
						OpportunityTableDisplay.this
								.fireSelectItemEvent(opportunity);
						fireTableEvent(new TableClickEvent(
								OpportunityTableDisplay.this, opportunity,
								"selected"));
					}
				});

				SimpleOpportunity opportunity = OpportunityTableDisplay.this
						.getBeanByIndex(itemId);
				opportunity.setExtraData(cb);
				return cb;
			}
		});

		this.addGeneratedColumn("opportunityname", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final SimpleOpportunity opportunity = OpportunityTableDisplay.this
						.getBeanByIndex(itemId);
				
				LabelLink b = new LabelLink(opportunity.getOpportunityname(),
						CrmLinkBuilder.generateOpportunityPreviewLinkFull(opportunity
								.getId()));	
				if ("Closed Won".equals(opportunity.getSalesstage())
						|| "Closed Lost".equals(opportunity.getSalesstage())) {
					b.addStyleName(UIConstants.LINK_COMPLETED);
				} else {
					if (opportunity.getExpectedcloseddate() != null
							&& (opportunity.getExpectedcloseddate()
									.before(new GregorianCalendar().getTime()))) {
						b.addStyleName(UIConstants.LINK_OVERDUE);
					}
				}
				b.setDescription(generateTooltip(opportunity));

				return b;
			}
		});

		this.addGeneratedColumn("amount", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final SimpleOpportunity opportunity = OpportunityTableDisplay.this
						.getBeanByIndex(itemId);

				String amount = "";
				if (opportunity.getAmount() != null) {
					amount = opportunity.getAmount() + "";

					if (opportunity.getCurrency() != null) {
						amount += " " + opportunity.getCurrency().getSymbol();
					}
				}

				return new Label(amount);
			}
		});

		this.addGeneratedColumn("assignUserFullName",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(Table source,
							final Object itemId, Object columnId) {
						final SimpleOpportunity opportunity = OpportunityTableDisplay.this
								.getBeanByIndex(itemId);
						UserLink b = new UserLink(opportunity.getAssignuser(),
								opportunity.getAssignUserAvatarId(),
								opportunity.getAssignUserFullName());
						return b;

					}
				});

		this.addGeneratedColumn("accountName", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final SimpleOpportunity opportunity = OpportunityTableDisplay.this
						.getBeanByIndex(itemId);
				
				LabelLink b = new LabelLink(opportunity.getAccountName(),
						CrmLinkBuilder.generateAccountPreviewLinkFull(opportunity
								.getAccountid()));	
				return b;
			}
		});

		this.addGeneratedColumn("campaignName", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId,
					Object columnId) {
				final SimpleOpportunity opportunity = OpportunityTableDisplay.this
						.getBeanByIndex(itemId);
				
				LabelLink b = new LabelLink(opportunity.getCampaignName(),
						CrmLinkBuilder.generateCampaignPreviewLinkFull(opportunity
								.getCampaignid()));	
				return b;
			}
		});

		this.addGeneratedColumn("expectedcloseddate",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(Table source,
							Object itemId, Object columnId) {
						final SimpleOpportunity opportunity = OpportunityTableDisplay.this
								.getBeanByIndex(itemId);
						Label l = new Label();
						l.setValue(AppContext.formatDate(opportunity
								.getExpectedcloseddate()));
						return l;
					}
				});

		this.setWidth("100%");
	}

	private String generateTooltip(SimpleOpportunity opportunity) {
		try {
			Div div = new Div();
			H3 opportunityName = new H3();
			opportunityName.appendText(Jsoup.parse(
					opportunity.getOpportunityname()).html());
			div.appendChild(opportunityName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Currency:"))
					.appendChild(
							new Td().setStyle(
									"vertical-align: top; text-align: left;")
									.appendText(
											StringUtils
													.getStringFieldValue((opportunity
															.getCurrency() != null) ? opportunity
															.getCurrency()
															.getSymbol() : "")));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Account Name:"))
					.appendChild(
							new Td().appendChild(new A()
									.setHref(
											(opportunity.getAccountid() != null) ? AppContext
													.getSiteUrl()
													+ "#"
													+ CrmLinkGenerator
															.generateAccountPreviewLink(opportunity
																	.getAccountid())
													: "")
									.appendText(
											StringUtils
													.getStringFieldValue(opportunity
															.getAccountName()))));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Amount:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(opportunity
															.getAmount())));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 140px; vertical-align: top; text-align: right;")
							.appendText("Expected Close Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											AppContext.formatDate(opportunity
													.getExpectedcloseddate())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Sales Stage:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(opportunity
															.getSalesstage())));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Lead Source:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(opportunity
															.getSource())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Probability (%):"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(opportunity
															.getProbability())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Campaign:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(opportunity
															.getCampaignid() != null) ? AppContext
															.getSiteUrl()
															+ "#"
															+ CrmLinkGenerator
																	.generateCampaignPreviewLink(opportunity
																			.getCampaignid())
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(opportunity
																			.getCampaignName()))));

			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Next Step:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(opportunity
															.getNextstep())));
			trRow5.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Actual Cost:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(opportunity
															.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	AppContext
																			.getSiteUrl(),
																	opportunity
																			.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					opportunity
																							.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(opportunity
																			.getAssignUserFullName()))));
			Tr trRow6 = new Tr();

			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(opportunity
									.getDescription()));
			trRow6_value.setAttribute("colspan", "3");

			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow6_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			table.appendChild(trRow6);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate Opportunity tooltip", e);
			return "";
		}
	}
}
