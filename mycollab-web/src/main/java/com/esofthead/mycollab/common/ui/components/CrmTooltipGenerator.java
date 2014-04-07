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
package com.esofthead.mycollab.common.ui.components;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmTooltipGenerator {
	private static Logger log = LoggerFactory
			.getLogger(CrmTooltipGenerator.class);

	public static String generateTolltipNull(){
		Div div = new Div();
		com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
		table.setStyle("padding-left:10px;  color: #5a5a5a; font-size:11px;");
		
		Tr trRow1 = new Tr();
		trRow1.appendChild(
				new Td().setStyle(
						"vertical-align: top; text-align: left;")
						.appendText("The item is not existed"));
		
		table.appendChild(trRow1);
		div.appendChild(table);
		
		return div.write();
	}
	
	public static String generateToolTipAccount(SimpleAccount account,
			String siteURL, String timeZone) {
		try {
			if (account == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 accountName = new H3();
			accountName
					.appendText(Jsoup.parse(account.getAccountname()).html());
			div.appendChild(accountName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Website:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(account.getWebsite() != null) ? account
															.getWebsite() : "")
													.appendText(
															StringUtils
																	.getStringFieldValue(account
																			.getWebsite()))));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 150px; vertical-align: top; text-align: right;")
							.appendText("Office Phone:"))
					.appendChild(
							new Td().setStyle(
									"width:180px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(account
															.getPhoneoffice())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Employees:"))
					.appendChild(
							new Td().appendText((account.getNumemployees() != null) ? account
									.getNumemployees().toString() : ""));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Email:"))
					.appendChild(
							new Td().setStyle(
									"width:180px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(account.getEmail() != null) ? "mailto:"
															+ account
																	.getEmail()
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(account
																			.getEmail()))));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(account.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	account.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					account.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(account
																			.getAssignUserFullName()))));

			trRow3.appendChild(
					new Td().setStyle(
							"width: 150px; vertical-align: top; text-align: right;")
							.appendText("Annual Revenue:"))
					.appendChild(
							new Td().setStyle(
									"width: 180px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(account
															.getAnnualrevenue())));

			Tr trRow4 = new Tr();
			Td trRow4_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(account
									.getDescription()));
			trRow4_value.setAttribute("colspan", "3");
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow4_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate Account tooltip servlet", e);
			return null;
		}
	}

	public static String generateToolTipContact(SimpleContact contact,
			String siteURL, String userTimeZone) {
		try {
			if (contact == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 contactName = new H3();
			contactName
					.appendText(Jsoup.parse(contact.getContactName()).html());
			div.appendChild(contactName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("First Name:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(contact
															.getFirstname())));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Office Phone:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(contact
															.getOfficephone())));
			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Last Name:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(contact
															.getLastname())));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Mobile:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(contact
															.getMobile())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Email:"))
					.appendChild(
							new Td().setStyle(
									"width:200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(contact.getEmail() != null) ? "mailto:"
															+ contact
																	.getEmail()
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(contact
																			.getEmail()))));

			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Birthday:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															contact.getBirthday(),
															userTimeZone)));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Department:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(contact
															.getDepartment())));

			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(contact.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	contact.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					contact.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(contact
																			.getAssignUserFullName()))));
			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Address:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(contact
															.getPrimaddress())));

			trRow5.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Other Address:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(contact
															.getOtheraddress())));
			Tr trRow6 = new Tr();
			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(contact
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
			log.error("Error while generate Contact tooltip servlet", e);
			return null;
		}
	}

	public static String generateTooltipCampaign(SimpleCampaign campagin,
			String siteURl, String userTimeZone) {
		try {
			if (campagin == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 campaginName = new H3();
			campaginName.appendText(campagin.getCampaignname());
			div.appendChild(campaginName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Start Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															campagin.getStartdate(),
															userTimeZone)));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(campagin
															.getStatus())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("End Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															campagin.getEnddate(),
															userTimeZone)));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Type:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(campagin
															.getType())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Currency:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue((campagin
															.getCurrency() != null) ? campagin
															.getCurrency()
															.getSymbol() : "")));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(campagin.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURl,
																	campagin.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					campagin.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(campagin
																			.getAssignUserFullName()))));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Expected Cost:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(campagin
															.getExpectedcost())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Budget:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(campagin
															.getBudget())));

			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Expected Revenue:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(campagin
															.getExpectedrevenue())));
			trRow5.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Actual Cost:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(campagin
															.getActualcost())));
			Tr trRow6 = new Tr();

			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(campagin
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
			log.error("Error while generate Camapgin tooltip servlet", e);
			return null;
		}
	}

	public static String generateTooltipLead(SimpleLead lead, String siteURl,
			String userTimeZone) {
		try {
			if (lead == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 leadName = new H3();
			leadName.appendText(Jsoup.parse(lead.getLeadName()).html());
			div.appendChild(leadName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("First Name:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getFirstname())));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Email:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(lead.getEmail() != null) ? "mailto:"
															+ lead.getEmail()
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(lead
																			.getEmail()))));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Last Name:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getLastname())));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Office Phone:"))
					.appendChild(
							new Td().setStyle(
									"width:150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getOfficephone())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Title:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getTitle())));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Mobile:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getMobile())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Department:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getDepartment())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Fax:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getFax())));
			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Account Name:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getAccountname())));
			trRow5.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Website:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(lead.getWebsite() != null) ? lead
															.getWebsite() : "")
													.appendText(
															StringUtils
																	.getStringFieldValue(lead
																			.getWebsite()))));

			Tr trRow6 = new Tr();
			trRow6.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Lead Source:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(lead
													.getLeadsourcedesc())));
			trRow6.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(lead.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURl,
																	lead.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					lead.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(lead
																			.getAssignUserFullName()))));
			Tr trRow7 = new Tr();
			trRow7.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Address:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getPrimaddress())));
			trRow7.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Other Address:"))
					.appendChild(
							new Td().setStyle(
									"width:200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(lead
															.getOtheraddress())));

			Tr trRow8 = new Tr();
			trRow8.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Postal Code:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(lead
													.getPrimpostalcode())));
			trRow8.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Other Postal Code:"))
					.appendChild(
							new Td().setStyle(
									"width:150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(lead
													.getOtherpostalcode())));
			Tr trRow9 = new Tr();

			Td trRow9_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(lead
									.getDescription()));
			trRow9_value.setAttribute("colspan", "3");

			trRow9.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow9_value);
			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			table.appendChild(trRow6);
			table.appendChild(trRow7);
			table.appendChild(trRow8);
			table.appendChild(trRow9);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate Lead tooltip servlet", e);
			return null;
		}
	}

	public static String generateTooltipOpportunity(
			SimpleOpportunity opportunity, String siteURl, String userTimeZone) {
		try {
			if (opportunity == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
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
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(opportunity.getAccountid() != null) ? siteURl
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
											DateTimeUtils
													.converToStringWithUserTimeZone(
															opportunity
																	.getExpectedcloseddate(),
															userTimeZone)));

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
															.getCampaignid() != null) ? siteURl
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
																	siteURl,
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
			log.error("Error while generate Opportunity tooltip servlet", e);
			return null;
		}
	}

	public static String generateTooltipCases(SimpleCase cases, String siteURL,
			String userTimeZone) {
		try {
			if (cases == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 caseName = new H3();
			caseName.appendText(Jsoup.parse(cases.getSubject()).html());
			div.appendChild(caseName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Priority:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(cases
															.getPriority())));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Type:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(cases
															.getType())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											(StringUtils
													.getStringFieldValue(cases
															.getStatus()))));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Reason:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(cases
															.getReason())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Account Name:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(cases.getAccountid() != null) ? siteURL
															+ "#"
															+ CrmLinkGenerator
																	.generateAccountPreviewLink(cases
																			.getAccountid())
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(cases
																			.getAccountName()))));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Email:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(cases.getEmail() != null) ? "mailto:"
															+ cases.getEmail()
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(cases
																			.getEmail()))));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Phone Number:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(cases
															.getPhonenumber())));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(cases.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	cases.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					cases.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(cases
																			.getAssignUserFullName()))));

			Tr trRow5 = new Tr();
			Td trRow5_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(cases
									.getDescription()));
			trRow5_value.setAttribute("colspan", "3");

			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow5_value);

			Tr trRow6 = new Tr();
			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(cases
									.getResolution()));
			trRow6_value.setAttribute("colspan", "3");

			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Resolution:")).appendChild(
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
			log.error("Error while generate Case tooltip servlet", e);
			return null;
		}
	}

	public static String generateToolTipMeeting(SimpleMeeting meeting,
			String siteURl, String userTimeZone) {
		try {
			if (meeting == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 eventName = new H3();
			eventName.appendText(meeting.getSubject());
			div.appendChild(eventName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Start Date & Time:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															meeting.getStartdate(),
															userTimeZone)));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.setStyle(
											"width:110px; vertical-align: top; text-align: left;")
									.appendText(
											StringUtils
													.getStringFieldValue(meeting
															.getStatus())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("End Date & Time:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															meeting.getEnddate(),
															userTimeZone)));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Location:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(meeting
															.getLocation())));
			Tr trRow3 = new Tr();
			Td trRow3_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(meeting
									.getDescription()));
			trRow3_value.setAttribute("colspan", "3");

			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow3_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error(
					"Error while generate CRM Meeting servlert tooltip servlet",
					e);
			return null;
		}
	}

	public static String generateToolTipCall(SimpleCall call, String siteURL,
			String userTimeZone) {
		try {
			if (call == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 callName = new H3();
			callName.appendText(call.getSubject());
			div.appendChild(callName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 120px; vertical-align: top; text-align: right;")
							.appendText("Start Date & Time:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															call.getStartdate(),
															userTimeZone)));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(call
															.getStatus())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Duration:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(call
													.getDurationinseconds())));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Purpose:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(call
															.getPurpose())));
			Tr trRow3 = new Tr();
			Td trRow3_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(call
									.getDescription()));
			trRow3_value.setAttribute("colspan", "3");

			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow3_value);

			Tr trRow4 = new Tr();
			Td trRow4_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(call.getResult()));
			trRow4_value.setAttribute("colspan", "3");

			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Result:")).appendChild(trRow4_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow4);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate CRM Call servlert tooltip servlet",
					e);
			return null;
		}
	}

	public static String generateToolTipCrmTask(SimpleTask task,
			String siteURL, String userTimeZone) {
		try {
			if (task == null)
				return generateTolltipNull();
			Div div = new Div()
					.setStyle("font: 12px Arial, Verdana, Helvetica, sans-serif !important;line-height: normal;");
			H3 eventName = new H3();
			eventName.appendText(Jsoup.parse(task.getSubject()).html());
			div.appendChild(eventName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Start Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															task.getStartdate(),
															userTimeZone)));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"width:150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(task
															.getStatus())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Due Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											DateTimeUtils
													.converToStringWithUserTimeZone(
															task.getDuedate(),
															userTimeZone)));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Contact:"))

					.appendChild(
							new Td().setStyle(
									"width:110px; vertical-align: top; text-align: left;")
									.appendChild(
											new A().setHref(
													(task.getContactid() != null) ? siteURL
															+ "#"
															+ CrmLinkGenerator
																	.generateContactPreviewLink(task
																			.getContactid())
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(task
																			.getContactName()))));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Priority:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(task
															.getPriority())));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(task.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	siteURL,
																	task.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					task.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(task
																			.getAssignUserFullName()))));

			Tr trRow4 = new Tr();
			Td trRow4_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.getStringRemoveHtmlTag(task
									.getDescription()));
			trRow4_value.setAttribute("colspan", "3");

			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow4_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate CRM Task tooltip servlet", e);
			return null;
		}
	}
}
