package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.ui.EditFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@ViewComponent
public class PageAddViewImpl extends AbstractEditItemComp<Page> implements
		PageAddView {
	private static final long serialVersionUID = 1L;

	@Override
	protected String initFormHeader() {
		return AppContext.getMessage(Page18InEnum.VIEW_NEW_TITLE);
	}

	@Override
	protected String initFormTitle() {
		return null;
	}

	@Override
	protected Resource initFormIconResource() {
		return MyCollabResource
				.newResource("icons/22/project/page_selected.png");
	}

	@Override
	protected ComponentContainer createButtonControls() {
		final Layout controlButtons = (new EditFormControlsGenerator<Page>(
				editForm)).createButtonControls();
		return controlButtons;
	}

	@Override
	protected AdvancedEditBeanForm<Page> initPreviewForm() {
		return new AdvancedEditBeanForm<Page>();
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new PageFormLayoutFactory();
	}

	@Override
	protected AbstractBeanFieldGroupEditFieldFactory<Page> initBeanFormFieldFactory() {
		return new PageEditFormFieldFactory(editForm);
	}

}
