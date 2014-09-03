/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.vaadin.mobilecomponent.MobileViewToolbar;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 * @param <B>
 */
public abstract class AbstractPreviewItemComp<B> extends AbstractMobilePageView {
	private static final long serialVersionUID = 1L;

	protected B beanItem;
	protected AdvancedPreviewBeanForm<B> previewForm;

	private NavigationBarQuickMenu editBtn;

	public AbstractPreviewItemComp() {

		previewForm = initPreviewForm();
		previewForm.setStyleName("readview-layout");
		this.setContent(previewForm);

		editBtn = new NavigationBarQuickMenu();
		editBtn.setButtonCaption(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL));
		editBtn.setStyleName("edit-btn");
		editBtn.setContent(createButtonControls());
		this.setRightComponent(editBtn);

		initRelatedComponents();

		ComponentContainer toolbarContent = createBottomPanel();
		if (toolbarContent != null) {
			toolbarContent.setStyleName("related-items");
			toolbarContent.setHeight("100%");
			toolbarContent.setWidthUndefined();

			MobileViewToolbar toolbar = new MobileViewToolbar();
			toolbar.setComponent(toolbarContent);
			this.setToolbar(toolbar);
		}
	}

	public void previewItem(final B item) {
		this.beanItem = item;
		this.setCaption(initFormTitle());

		previewForm.setFormLayoutFactory(initFormLayoutFactory());
		previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
		previewForm.setBean(item);

		afterPreviewItem();
	}

	public B getItem() {
		return beanItem;
	}

	public AdvancedPreviewBeanForm<B> getPreviewForm() {
		return previewForm;
	}

	abstract protected void afterPreviewItem();

	abstract protected String initFormTitle();

	abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

	abstract protected void initRelatedComponents();

	abstract protected IFormLayoutFactory initFormLayoutFactory();

	abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

	abstract protected ComponentContainer createButtonControls();

	abstract protected ComponentContainer createBottomPanel();

}
