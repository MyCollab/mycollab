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
package com.esofthead.mycollab.module.crm.view.contact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.resource.LazyStreamSource;
import com.esofthead.mycollab.vaadin.resource.OnDemandFileDownloader;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormEmailLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.UserLinkViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameters.AddressTypeParameter;
import ezvcard.parameters.TelephoneTypeParameter;
import ezvcard.types.AddressType;
import ezvcard.types.BirthdayType;
import ezvcard.types.EmailType;
import ezvcard.types.NoteType;
import ezvcard.types.OrganizationType;
import ezvcard.types.ProfileType;
import ezvcard.types.RawType;
import ezvcard.types.StructuredNameType;
import ezvcard.types.TelephoneType;
import ezvcard.types.TitleType;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class ContactReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleContact> {
	private static final long serialVersionUID = 1L;

	public ContactReadFormFieldFactory(GenericBeanForm<SimpleContact> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		if (propertyId.equals("accountid")) {
			return new FormLinkViewField(attachForm.getBean().getAccountName(),
					CrmLinkBuilder.generateAccountPreviewLinkFull(attachForm
							.getBean().getAccountid()),
					MyCollabResource
							.newResourceLink("icons/16/crm/account.png"));
		} else if (propertyId.equals("email")) {
			return new FormEmailLinkViewField(attachForm.getBean().getEmail());
		} else if (propertyId.equals("assignuser")) {
			return new UserLinkViewField(attachForm.getBean().getAssignuser(),
					attachForm.getBean().getAssignUserAvatarId(), attachForm
							.getBean().getAssignUserFullName());
		} else if (propertyId.equals("iscallable")) {
			if (attachForm.getBean().getIscallable() == null
					|| Boolean.FALSE == attachForm.getBean().getIscallable()) {
				return new FormViewField("No");
			} else {
				return new FormViewField("Yes");
			}
		} else if (propertyId.equals("birthday")) {
			return new FormViewField(AppContext.formatDate(attachForm.getBean()
					.getBirthday()));
		} else if (propertyId.equals("firstname")) {
			final FormContainerHorizontalViewField containerField = new FormContainerHorizontalViewField();
			SimpleContact contact = attachForm.getBean();
			String displayName = "";
			if (contact.getPrefix() != null) {
				displayName = contact.getPrefix();
			}
			if (contact.getFirstname() != null) {
				displayName += contact.getFirstname();
			}

			final Label nameLbl = new Label(displayName);
			containerField.addComponentField(nameLbl);
			containerField.getLayout().setExpandRatio(nameLbl, 1.0f);
			final Button vcardDownloadBtn = new Button("");
			VCardStreamSource streamSource = new VCardStreamSource();
			OnDemandFileDownloader downloaderExt = new OnDemandFileDownloader(
					streamSource);
			downloaderExt.extend(vcardDownloadBtn);

			vcardDownloadBtn.setIcon(MyCollabResource
					.newResource("icons/12/vcard.png"));
			vcardDownloadBtn.setStyleName(UIConstants.THEME_TRANSPARENT_LINK);
			containerField.addComponentField(vcardDownloadBtn);
			containerField.getLayout().setComponentAlignment(vcardDownloadBtn,
					Alignment.TOP_RIGHT);
			return containerField;
		}

		return null;
	}

	private class VCardStreamSource extends LazyStreamSource {
		private static final long serialVersionUID = 1L;
		private File vcardTemp;

		public VCardStreamSource() {
			try {
				vcardTemp = File.createTempFile("mycollab", ".vcf");
			} catch (IOException e) {
				throw new MyCollabException(
						"Can not create temporary file to download vcard export",
						e);
			}
		}

		@Override
		protected StreamSource buildStreamSource() {
			final VCard vcard = new VCard();
			// Given is name
			final StructuredNameType name = new StructuredNameType();
			if (attachForm.getBean().getFirstname() != null) {
				name.setFamily(attachForm.getBean().getLastname());
			}
			if (attachForm.getBean().getLastname() != null) {
				name.setGiven(attachForm.getBean().getFirstname());
			}
			vcard.setStructuredName(name);

			if (attachForm.getBean().getEmail() != null) {
				final EmailType email = new EmailType();
				email.setValue(attachForm.getBean().getEmail());
				vcard.addEmail(email);
			}
			// Mapping Address ---------------------
			final AddressType otherAddress = new AddressType();
			otherAddress.addType(AddressTypeParameter.WORK);
			// Street address map to OtherAddress
			if (attachForm.getBean().getOtheraddress() != null) {
				otherAddress.setStreetAddress(attachForm.getBean()
						.getOtheraddress());
			}
			if (attachForm.getBean().getOthercountry() != null) {
				otherAddress.setCountry(attachForm.getBean().getOthercountry());
			}
			// city map to Region ----------------
			if (attachForm.getBean().getOthercity() != null) {
				otherAddress.setRegion(attachForm.getBean().getOthercity());
			}
			if (attachForm.getBean().getOtherpostalcode() != null) {
				otherAddress.setPostalCode(attachForm.getBean()
						.getOtherpostalcode());
			}
			// Sate map to Locality
			if (attachForm.getBean().getOtherstate() != null) {
				otherAddress.setLocality(attachForm.getBean().getOtherstate());
			}
			vcard.addAddress(otherAddress);

			final AddressType primAddress = new AddressType();
			primAddress.addType(AddressTypeParameter.HOME);

			// StreetAddress map to PrimAddress
			if (attachForm.getBean().getPrimaddress() != null) {
				primAddress.setStreetAddress(attachForm.getBean()
						.getPrimaddress());
			}

			if (attachForm.getBean().getPrimcountry() != null) {
				primAddress.setCountry(attachForm.getBean().getPrimcountry());
			}

			if (attachForm.getBean().getPrimcity() != null) {
				primAddress.setRegion(attachForm.getBean().getPrimcity());
			}

			if (attachForm.getBean().getPrimpostalcode() != null) {
				primAddress.setPostalCode(attachForm.getBean()
						.getPrimpostalcode());
			}

			if (attachForm.getBean().getPrimstate() != null) {
				primAddress.setLocality(attachForm.getBean().getPrimstate());
			}

			vcard.addAddress(primAddress);

			// Mapping Phone --------------------
			if (attachForm.getBean().getHomephone() != null) {
				vcard.addTelephoneNumber(attachForm.getBean().getHomephone(),
						TelephoneTypeParameter.HOME);
			}
			// OFFICE PHONE
			if (attachForm.getBean().getOfficephone() != null) {
				vcard.addTelephoneNumber(attachForm.getBean().getOfficephone(),
						TelephoneTypeParameter.WORK);
			}
			// MOBIE
			if (attachForm.getBean().getMobile() != null) {
				vcard.addTelephoneNumber(attachForm.getBean().getMobile(),
						TelephoneTypeParameter.CELL);
			}

			if (attachForm.getBean().getOtherphone() != null) {
				vcard.addTelephoneNumber(attachForm.getBean().getOtherphone(),
						TelephoneTypeParameter.PAGER);
			}

			// FAX
			if (attachForm.getBean().getFax() != null) {
				final TelephoneType fax = new TelephoneType();
				fax.addType(TelephoneTypeParameter.FAX);
				fax.setText(attachForm.getBean().getFax());
				vcard.addTelephoneNumber(fax);
			}
			// Map department -----------
			if (attachForm.getBean().getDepartment() != null) {
				final OrganizationType department = new OrganizationType();
				department.addValue(attachForm.getBean().getDepartment());
				vcard.addOrganization(department);
			}
			// Map leadsource to Profile
			if (attachForm.getBean().getLeadsource() != null) {
				final ProfileType profile = new ProfileType();
				profile.setValue(attachForm.getBean().getLeadsource());
				vcard.setProfile(profile);
			}
			// Map birthday ----------
			if (attachForm.getBean().getBirthday() != null) {
				final BirthdayType birthday = new BirthdayType();
				birthday.setDate(attachForm.getBean().getBirthday(), false);
				vcard.setBirthday(birthday);
			}
			if (attachForm.getBean().getDescription() != null) {
				final NoteType noteType = new NoteType();
				noteType.setValue(attachForm.getBean().getDescription());
				vcard.addNote(noteType);
			}
			// Map leadsource
			if (attachForm.getBean().getLeadsource() != null) {
				final RawType leadsource = new RawType("leadsource");
				leadsource.setValue(attachForm.getBean().getLeadsource());
				vcard.addExtendedType(leadsource);
			}
			// Map assitance ---
			if (attachForm.getBean().getAssistant() != null) {
				final RawType assistant = new RawType("Assistant");
				assistant.setValue(attachForm.getBean().getAssistant());
				vcard.addExtendedType(assistant);
			}
			// Map AssistantPhone
			if (attachForm.getBean().getAssistantphone() != null) {
				final RawType assistantPhone = new RawType("AssistantPhone");
				assistantPhone.setValue(attachForm.getBean()
						.getAssistantphone());
				vcard.addExtendedType(assistantPhone);
			}
			// Map AssignUser
			if (attachForm.getBean().getAssignuser() != null) {
				final RawType assignuser = new RawType("AssignUser");
				assignuser.setValue(attachForm.getBean().getAssignuser());
				vcard.addExtendedType(assignuser);
			}
			// Map Callable true/false
			if (attachForm.getBean().getIscallable() != null) {
				final RawType bool = new RawType("Callable");
				bool.setValue(attachForm.getBean().getIscallable().toString());
				vcard.addExtendedType(bool);
			}
			// Map Tittle ----
			if (attachForm.getBean().getTitle() != null) {
				final TitleType title = new TitleType(attachForm.getBean()
						.getTitle());
				vcard.addExtendedType(title);
			}

			try {
				Ezvcard.write(vcard).version(VCardVersion.V4_0).go(vcardTemp);

				return new StreamSource() {
					private static final long serialVersionUID = 1L;

					@Override
					public InputStream getStream() {
						try {
							return new FileInputStream(vcardTemp);
						} catch (FileNotFoundException e) {
							throw new MyCollabException(e);
						}
					}
				};
			} catch (final IOException e) {
				throw new MyCollabException(e);
			}
		}

		public String getFilename() {
			return vcardTemp.getName();
		}

	}
}
