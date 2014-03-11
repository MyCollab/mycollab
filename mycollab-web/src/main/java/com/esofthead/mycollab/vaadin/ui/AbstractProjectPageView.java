package com.esofthead.mycollab.vaadin.ui;


import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

public class AbstractProjectPageView extends AbstractPageView{

	private static final long serialVersionUID = 1L;
	private Label headerText;
	private CssLayout headerRight;
	private CssLayout contentWrapper;
	private Image titleIcon;
	public AbstractProjectPageView(String headerText, String iconName) {
		super();

		this.headerRight = new CssLayout();
		this.titleIcon = new Image(null,
				MyCollabResource.newResource("icons/22/project/"+iconName));
		this.headerText = new Label(headerText);
		super.addComponent(constructHeader());

		contentWrapper = new CssLayout();
		contentWrapper.setStyleName("content-wrapper");
		super.addComponent(contentWrapper);

	}
	public ComponentContainer constructHeader() {
		HorizontalLayout header = new HorizontalLayout();
		this.headerText.setStyleName("hdr-text");

		UiUtils.addComponent(header, titleIcon, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, headerText, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, headerRight, Alignment.MIDDLE_RIGHT);
		header.setExpandRatio(headerText, 1.0f);

		header.setStyleName("hdr-view");
		header.setWidth("100%");
		header.setSpacing(true);
		header.setMargin(true);
		return header;
	}
	public void addHeaderRightContent(Component c) {
		headerRight.addComponent(c);
	}

	@Override
	public void addComponent(Component c) {
		contentWrapper.addComponent(c);
	}
}
