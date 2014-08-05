package com.esofthead.mycollab.module.project.ui.components;

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
 * @since 4.3.3
 *
 */
public abstract class AbstractPreviewItemComp2<B> extends VerticalLayout
		implements PageView {
	private static final long serialVersionUID = 1L;

	protected B beanItem;
	protected AdvancedPreviewBeanForm<B> previewForm;
	protected ReadViewLayout previewLayout;
	private Label headerText;
	private HorizontalLayout header;
	private HorizontalLayout bodyContainer;

	private VerticalLayout sidebarContent;
	private VerticalLayout bodyContent;

	private Image titleIcon;

	abstract protected void initRelatedComponents();

	public AbstractPreviewItemComp2(String headerText, Resource iconResource) {
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
		if (bottomPanel != null) {
			if (bodyContent.getComponentCount() >= 2) {
				bodyContent.replaceComponent(bodyContent
						.getComponent(bodyContent.getComponentCount() - 1),
						bottomPanel);
			} else {
				bodyContent.addComponent(bottomPanel);
			}

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
