package com.esofthead.mycollab.module.project.view.page;

import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import com.esofthead.mycollab.common.i18n.WikiI18nEnum;
import com.esofthead.mycollab.module.wiki.domain.Page;
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
class PageEditFormFieldFactory<B extends Page> extends
		AbstractBeanFieldGroupEditFieldFactory<B> {

	private static final long serialVersionUID = 1L;

	PageEditFormFieldFactory(GenericBeanForm<B> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		Page page = attachForm.getBean();
		if (propertyId.equals("content")) {
			CKEditorConfig config = new CKEditorConfig();
			config.useCompactTags();
			config.disableElementsPath();
			config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
			config.disableSpellChecker();
			config.setToolbarCanCollapse(false);
			config.setWidth("100%");

			final CKEditorTextField ckEditorTextField = new CKEditorTextField(
					config);
			return ckEditorTextField;
		} else if (propertyId.equals("status")) {
			page.setStatus(WikiI18nEnum.status_public.name());
			return new I18nValueComboBox(false, WikiI18nEnum.status_public,
					WikiI18nEnum.status_private, WikiI18nEnum.status_archieved);
		}

		return null;
	}

}
