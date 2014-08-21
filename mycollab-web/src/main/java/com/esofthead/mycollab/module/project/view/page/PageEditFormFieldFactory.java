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
package com.esofthead.mycollab.module.project.view.page;

import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import com.esofthead.mycollab.common.i18n.WikiI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
class PageEditFormFieldFactory extends
		AbstractBeanFieldGroupEditFieldFactory<Page> {

	private static final long serialVersionUID = 1L;

	PageEditFormFieldFactory(GenericBeanForm<Page> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		Page page = attachForm.getBean();
		if (propertyId.equals("content")) {
			CKEditorConfig config = new CKEditorConfig();
			config.useCompactTags();
			config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
			config.disableSpellChecker();
			config.disableResizeEditor();
			config.disableElementsPath();
			config.setToolbarCanCollapse(true);
			config.setWidth("100%");

			String appUrl = SiteConfiguration.getAppUrl();
			String params = String.format(
					"path=%s&createdUser=%s&sAccountId=%d", page.getPath(),
					AppContext.getUsername(), AppContext.getAccountId());
			if (appUrl.endsWith("/")) {
				config.setFilebrowserUploadUrl(SiteConfiguration.getAppUrl()
						+ "page/upload?" + params);
			} else {
				config.setFilebrowserUploadUrl(SiteConfiguration.getAppUrl()
						+ "/page/upload?" + params);
			}

			final CKEditorTextField ckEditorTextField = new CKEditorTextField(
					config);
			ckEditorTextField.setHeight("450px");
			return ckEditorTextField;
		} else if (propertyId.equals("status")) {
			page.setStatus(WikiI18nEnum.status_public.name());
			return new I18nValueComboBox(false, WikiI18nEnum.status_public,
					WikiI18nEnum.status_private, WikiI18nEnum.status_archieved);
		}

		return null;
	}

}
