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
package com.esofthead.mycollab.module.crm.view.contact.iexport;

import java.util.List;
import java.util.Set;

import com.esofthead.mycollab.iexporter.VCardObjectEntityConverter;
import com.esofthead.mycollab.module.crm.domain.Contact;

import ezvcard.VCard;
import ezvcard.parameters.AddressTypeParameter;
import ezvcard.parameters.TelephoneTypeParameter;
import ezvcard.types.AddressType;
import ezvcard.types.EmailType;
import ezvcard.types.TelephoneType;

public class ContactVCardObjectEntityConverter implements
		VCardObjectEntityConverter<Contact> {

	@Override
	public Contact convert(Class<Contact> cls, VCard vcard) {
		Contact contact = new Contact();
		if (vcard != null) {

			// NAME
			if (vcard.getStructuredName() != null) {
				if (vcard.getStructuredName().getFamily() != null)
					contact.setFirstname(vcard.getStructuredName().getFamily());
				else
					contact.setFirstname("");
				if (vcard.getStructuredName().getGiven() != null)
					contact.setLastname(vcard.getStructuredName().getGiven());
				else
					contact.setLastname("");
			}
			// ADDRESS
			if (vcard.getAddresses() != null) {
				List<AddressType> lstAddress = vcard.getAddresses();
				for (AddressType address : lstAddress) {
					Set<AddressTypeParameter> setPhoneType = address.getTypes();
					for (Object object : setPhoneType.toArray()) {
						int index = object.toString().indexOf("=");
						String addressType = object.toString().substring(
								index + 1, object.toString().length());
						if (addressType.equals("home")) {
							contact.setPrimaddress(address.getStreetAddress());
							contact.setPrimcountry(address.getCountry());
							contact.setPrimpostalcode(address.getPostalCode());
							contact.setPrimstate(address.getLocality());
						} else {
							contact.setOtheraddress(address.getStreetAddress());
							contact.setOthercountry(address.getCountry());
							contact.setOtherpostalcode(address.getPostalCode());
							contact.setOtherstate(address.getLocality());
						}
					}
				}
			}
			// EMAIL
			if (vcard.getEmails() != null) {
				for (EmailType email : vcard.getEmails()) {
					contact.setEmail(email.getValue());
				}
			}
			// BRITHDAY
			if (vcard.getBirthday() != null) {
				contact.setBirthday(vcard.getBirthday().getDate());
			}
			// Description
			if (vcard.getNotes() != null && vcard.getNotes().size() > 0) {
				contact.setDescription(vcard.getNotes().get(0).toString());
			}
			// Department
			if (vcard.getOrganization() != null) {
				contact.setDepartment((vcard.getOrganization().getValues() != null && vcard
						.getOrganization().getValues().size() > 0) ? vcard
						.getOrganization().getValues().get(0) : "");
			}
			// PHone
			if (vcard.getTelephoneNumbers() != null) {
				for (TelephoneType phone : vcard.getTelephoneNumbers()) {
					Set<TelephoneTypeParameter> setPhoneType = phone.getTypes();
					for (Object object : setPhoneType.toArray()) {
						int index = object.toString().indexOf("=");
						String phoneType = object.toString().substring(
								index + 1, object.toString().length());
						if (phoneType.equals("home")) { // HOME
							contact.setHomephone((phone.getText() != null) ? phone
									.getText() : phone.getUri().getNumber());
						} else if (phoneType.equals("work")) { // Office
							contact.setOfficephone((phone.getText() != null) ? phone
									.getText() : phone.getUri().getNumber());
						} else if (phoneType.equals("cell")) { // Mobie
							contact.setMobile((phone.getText() != null) ? phone
									.getText() : phone.getUri().getNumber());
						} else if (phoneType.equals("pager")) { // OtherPhone
							contact.setOtherphone((phone.getText() != null) ? phone
									.getText() : phone.getUri().getNumber());
						} else if (phoneType.equals("fax")) { // FAX
							contact.setFax((phone.getText() != null) ? phone
									.getText() : phone.getUri().getNumber());
						}
					}
				}
			}
			// Leadsource
			if (vcard.getExtendedType("leadsource") != null
					&& vcard.getExtendedType("leadsource").size() > 0) {
				String leadsource = (vcard.getExtendedType("leadsource").get(0) != null) ? vcard
						.getExtendedType("leadsource").get(0).getValue()
						: "";
				contact.setLeadsource(leadsource);
			}
			// Assistant
			if (vcard.getExtendedType("Assistant") != null
					&& vcard.getExtendedType("Assistant").size() > 0) {
				String assistant = vcard.getExtendedType("Assistant").get(0)
						.getValue();
				contact.setAssistant(assistant);
			}
			// AssistantPhone
			if (vcard.getExtendedType("AssistantPhone") != null
					&& vcard.getExtendedType("AssistantPhone").size() > 0) {
				String assisPhone = vcard.getExtendedType("AssistantPhone")
						.get(0).getValue();
				contact.setAssistantphone(assisPhone);
			}
			// AssignUser
			if (vcard.getExtendedType("AssignUser") != null
					&& vcard.getExtendedType("AssignUser").size() > 0) {
				String assignUser = vcard.getExtendedType("AssignUser").get(0)
						.getValue();
				contact.setAssignuser(assignUser);
			}
			// Callable
			if (vcard.getExtendedType("Callable") != null
					&& vcard.getExtendedType("Callable").size() > 0) {
				String bool = vcard.getExtendedType("Callable").get(0)
						.getValue();
				if (bool.equals("true"))
					contact.setIscallable(true);
				else
					contact.setIscallable(false);
			}
			// Tittle
			if (vcard.getTitles() != null && vcard.getTitles().size() > 0) {
				String title = vcard.getTitles().get(0).getValue();
				contact.setTitle(title);
			}
		}
		return contact;
	}

}
