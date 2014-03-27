package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class AbstractProjectPageView extends AbstractPageView {

	private static final long serialVersionUID = 1L;
	private Label headerText;
	private CssLayout contentWrapper;
	private HorizontalLayout header;
	private Image titleIcon;

	public AbstractProjectPageView(String headerText, String iconName) {
		super();

		this.titleIcon = new Image(null,
				MyCollabResource.newResource("icons/24/project/" + iconName));
		this.headerText = new Label(headerText);
		super.addComponent(constructHeader());

		contentWrapper = new CssLayout();
		contentWrapper.setStyleName("content-wrapper");
		super.addComponent(contentWrapper);

	}

	public ComponentContainer constructHeader() {
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

	@Override
	public void addComponent(Component c) {
		contentWrapper.addComponent(c);
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		contentWrapper.replaceComponent(oldComponent, newComponent);
	}

	public ComponentContainer getBody() {
		return contentWrapper;
	}
}
