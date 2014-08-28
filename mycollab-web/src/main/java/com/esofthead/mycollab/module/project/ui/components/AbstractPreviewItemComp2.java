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

import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.ReadViewLayout;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
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
 * @since 4.3.3
 *
 */
public abstract class AbstractPreviewItemComp2<B> extends VerticalLayout
		implements PageView {
	private static final long serialVersionUID = 1L;

	protected B beanItem;
	protected AdvancedPreviewBeanForm<B> previewForm;
	protected ReadViewLayout previewLayout;
	protected HorizontalLayout header;
	private HorizontalLayout bodyContainer;

	private VerticalLayout sidebarContent;
	private VerticalLayout bodyContent;

	protected Image titleIcon;

	abstract protected void initRelatedComponents();

	public AbstractPreviewItemComp2(String headerText, Resource iconResource) {
		this.titleIcon = new Image(null, iconResource);
		this.addComponent(constructHeader(headerText));

		previewForm = initPreviewForm();
		ComponentContainer actionControls = createButtonControls();
		if (actionControls != null) {
			actionControls.addStyleName("control-buttons");
			addHeaderRightContent(actionControls);
		}

		CssLayout contentWrapper = new CssLayout();
		contentWrapper.setStyleName("content-wrapper");

		previewLayout = new ReadViewLayout("", iconResource);

		contentWrapper.addComponent(previewLayout);

		bodyContainer = new HorizontalLayout();
		bodyContainer.setSizeFull();
		bodyContainer.setStyleName("readview-body-wrap");

		bodyContent = new VerticalLayout();
		bodyContent.addComponent(previewForm);
		bodyContainer.addComponent(bodyContent);
		bodyContainer.setExpandRatio(bodyContent, 1);

		sidebarContent = new VerticalLayout();
		sidebarContent.setWidth("250px");
		sidebarContent.setSpacing(true);
		sidebarContent.setStyleName("readview-sidebar");
		bodyContainer.addComponent(sidebarContent);

		FloatingComponent floatSidebar = FloatingComponent
				.floatThis(sidebarContent);
		floatSidebar.setContainerId("main-body");

		previewLayout.addBody(bodyContainer);

		this.addComponent(contentWrapper);
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

	private void initLayout() {
		sidebarContent.removeAllComponents();
		initRelatedComponents();
		ComponentContainer bottomPanel = createBottomPanel();
		addBottomPanel(bottomPanel);
	}

	protected void addBottomPanel(ComponentContainer container) {
		if (container != null) {
			if (bodyContent.getComponentCount() >= 2) {
				bodyContent.replaceComponent(bodyContent
						.getComponent(bodyContent.getComponentCount() - 1),
						container);
			} else {
				bodyContent.addComponent(container);
			}
		}
	}

	protected ComponentContainer constructHeader(String headerText) {
		Label headerLbl = new Label(headerText);
		headerLbl.setSizeUndefined();
		header = new HorizontalLayout();
		headerLbl.setStyleName("hdr-text");

		UiUtils.addComponent(header, titleIcon, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, headerLbl, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(headerLbl, 1.0f);

		header.setStyleName("hdr-view");
		header.setWidth("100%");
		header.setSpacing(true);
		header.setMargin(true);

		return header;
	}

	public void addHeaderRightContent(Component c) {
		header.addComponent(c);
	}

	public void addToSideBar(Component component) {
		sidebarContent.addComponent(component);
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
	public void addViewListener(ViewListener listener) {

	}

	abstract protected void onPreviewItem();

	abstract protected String initFormTitle();

	abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

	abstract protected IFormLayoutFactory initFormLayoutFactory();

	abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

	abstract protected ComponentContainer createButtonControls();

	abstract protected ComponentContainer createBottomPanel();
}
