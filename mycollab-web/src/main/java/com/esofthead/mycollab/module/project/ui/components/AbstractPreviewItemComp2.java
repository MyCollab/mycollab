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

import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.ReadViewLayout;
import com.esofthead.mycollab.vaadin.ui.RightSidebarLayout;
import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

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
	protected MHorizontalLayout header;
	private RightSidebarLayout bodyContainer;

	private MVerticalLayout sidebarContent;
	private MVerticalLayout bodyContent;

	protected Image titleIcon;

	abstract protected void initRelatedComponents();

	public AbstractPreviewItemComp2(String headerText, Resource iconResource) {
		if (iconResource != null)
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

		previewLayout = new ReadViewLayout("");

		contentWrapper.addComponent(previewLayout);

		bodyContainer = new RightSidebarLayout();
		bodyContainer.setSizeFull();
		bodyContainer.addStyleName("readview-body-wrap");

		bodyContent = new MVerticalLayout().withSpacing(false).withMargin(false).with(previewForm);
		bodyContainer.setContent(bodyContent);

		sidebarContent = new MVerticalLayout().withWidth("250px").withSpacing(true).withStyleName("readview-sidebar");
		bodyContainer.setSidebar(sidebarContent);

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
		headerLbl.setStyleName("hdr-text");

		header = new MHorizontalLayout();

		if (titleIcon != null) {
			header.with(titleIcon).withAlign(titleIcon, Alignment.MIDDLE_LEFT);
		}

		header.with(headerLbl).withAlign(headerLbl, Alignment.MIDDLE_LEFT)
				.expand(headerLbl).withStyleName("hdr-view").withWidth("100%")
				.withSpacing(true).withMargin(true);

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

	@SuppressWarnings("rawtypes")
	@Override
	final public void addViewListener(ViewListener listener) {

	}

	abstract protected void onPreviewItem();

	abstract protected String initFormTitle();

	abstract protected AdvancedPreviewBeanForm<B> initPreviewForm();

	abstract protected IFormLayoutFactory initFormLayoutFactory();

	abstract protected AbstractBeanFieldGroupViewFieldFactory<B> initBeanFormFieldFactory();

	abstract protected ComponentContainer createButtonControls();

	abstract protected ComponentContainer createBottomPanel();
}
