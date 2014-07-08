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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.ReadViewLayout;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 * @param <B>
 */
public abstract class AbstractPreviewItemComp<B> extends VerticalLayout
		implements PageView {
	private static final long serialVersionUID = 1L;

	protected B beanItem;
	protected AdvancedPreviewBeanForm<B> previewForm;
	protected ReadViewLayout previewLayout;
	private Label headerText;
	private HorizontalLayout header;
	private Image titleIcon;

	abstract protected void initRelatedComponents();

	public AbstractPreviewItemComp(String headerText, Resource iconResource) {

		this.titleIcon = new Image(null, iconResource);
		this.headerText = new Label(headerText);
		this.headerText.setSizeUndefined();
		this.addComponent(constructHeader());

		previewForm = initPreviewForm();
		ComponentContainer actionControls = createButtonControls();
		if (actionControls != null) {
			actionControls.addStyleName("control-buttons");
		}

		addHeaderRightContent(actionControls);

		CssLayout contentWrapper = new CssLayout();
		contentWrapper.setStyleName("content-wrapper");

		previewLayout = new ReadViewLayout("", iconResource);

		contentWrapper.addComponent(previewLayout);

		previewLayout.addBody(previewForm);

		this.addComponent(contentWrapper);
	}

	private void initLayout() {
		initRelatedComponents();
		ComponentContainer bottomPanel = createBottomPanel();
		if (bottomPanel != null) {
			previewLayout.addBottomControls(bottomPanel);
		}
	}

	private ComponentContainer constructHeader() {
		header = new HorizontalLayout();
		this.headerText.setStyleName("hdr-text");

		UiUtils.addComponent(header, titleIcon, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, headerText, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(headerText, 1.0f);

		header.setStyleName("hdr-view");
		header.setWidth("100%");
		header.setSpacing(true);
		header.setMargin(true);

		return header;
	}

	public void addHeaderRightContent(Component c) {
		header.addComponent(c);
	}

	public void previewItem(final B item) {
		this.beanItem = item;
		initLayout();
		previewLayout.setTitle(initFormTitle());

		previewForm.setFormLayoutFactory(initFormLayoutFactory());
		previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
		previewForm.setBean(item);

		onPreviewItem();
	}

	public B getBeanItem() {
		return beanItem;
	}

	public AdvancedPreviewBeanForm<B> getPreviewForm() {
		return previewForm;
	}

	protected void addLayoutStyleName(String styleName) {
		previewLayout.addTitleStyleName(styleName);
	}

	protected void removeLayoutStyleName(String styleName) {
		previewLayout.removeTitleStyleName(styleName);
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	@Override
	public void addViewListener(
			ApplicationEventListener<? extends ApplicationEvent> listener) {

	}

	abstract protected void onPreviewItem();

	abstract protected String initFormTitle();

	abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

	abstract protected IFormLayoutFactory initFormLayoutFactory();

	abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

	abstract protected ComponentContainer createButtonControls();

	abstract protected ComponentContainer createBottomPanel();
}
