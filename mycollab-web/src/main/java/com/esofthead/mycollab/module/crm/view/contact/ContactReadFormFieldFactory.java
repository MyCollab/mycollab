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
import java.io.IOException;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.resources.LazyStreamSource;
import com.esofthead.mycollab.vaadin.resources.OnDemandFileDownloader;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.form.field.ContainerHorizontalViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DateViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.EmailViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UserLinkViewField;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class ContactReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleContact> {
	private static final long serialVersionUID = 1L;

	public ContactReadFormFieldFactory(GenericBeanForm<SimpleContact> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		SimpleContact contact = attachForm.getBean();

		if (propertyId.equals("accountid")) {
			return new LinkViewField(contact.getAccountName(),
					CrmLinkBuilder.generateAccountPreviewLinkFull(contact.getAccountid()),
                    CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
		} else if (propertyId.equals("email")) {
			return new EmailViewField(attachForm.getBean().getEmail());
		} else if (propertyId.equals("assignuser")) {
			return new UserLinkViewField(contact.getAssignuser(),
					contact.getAssignUserAvatarId(), contact.getAssignUserFullName());
		} else if (propertyId.equals("iscallable")) {
			if (Boolean.FALSE.equals(contact.getIscallable())) {
				return new DefaultViewField(
						AppContext.getMessage(GenericI18Enum.BUTTON_NO));
			} else {
				return new DefaultViewField(
						AppContext.getMessage(GenericI18Enum.BUTTON_YES));
			}
		} else if (propertyId.equals("birthday")) {
			return new DateViewField(contact.getBirthday());
		} else if (propertyId.equals("firstname")) {
			final ContainerHorizontalViewField containerField = new ContainerHorizontalViewField();
			String displayName = "";
			if (contact.getPrefix() != null) {
				displayName = contact.getPrefix();
			}
			if (contact.getFirstname() != null) {
				displayName += contact.getFirstname();
			}

			Label nameLbl = new Label(displayName);
			containerField.addComponentField(nameLbl);
			containerField.getLayout().setExpandRatio(nameLbl, 1.0f);
			Button vcardDownloadBtn = new Button("");
			VCardStreamSource streamSource = new VCardStreamSource();
			OnDemandFileDownloader downloaderExt = new OnDemandFileDownloader(streamSource);
			downloaderExt.extend(vcardDownloadBtn);

			vcardDownloadBtn.setIcon(FontAwesome.CREDIT_CARD);
			vcardDownloadBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);
			containerField.addComponentField(vcardDownloadBtn);
			containerField.getLayout().setComponentAlignment(vcardDownloadBtn, Alignment.TOP_RIGHT);
			return containerField;
		} else if (propertyId.equals("description")) {
			return new RichTextViewField(contact.getDescription());
		}

		return null;
	}

	private static class VCardStreamSource extends LazyStreamSource {
		private static final long serialVersionUID = 1L;
		private File vcardTemp;

		public VCardStreamSource() {
			try {
				vcardTemp = File.createTempFile("mycollab", ".vcf");
			} catch (IOException e) {
				throw new MyCollabException(
						"Can not create temporary file to download vcard export", e);
			}
		}

		@Override
		protected StreamSource buildStreamSource() {
			return null;
		}

		public String getFilename() {
			return vcardTemp.getName();
		}

	}
}
