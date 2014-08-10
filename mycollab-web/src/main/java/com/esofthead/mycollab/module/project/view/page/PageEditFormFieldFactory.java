package com.esofthead.mycollab.module.project.view.page;

import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
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
			final CKEditorTextField ckEditorTextField = new CKEditorTextField();
			return ckEditorTextField;
		}

		return null;
	}

}
