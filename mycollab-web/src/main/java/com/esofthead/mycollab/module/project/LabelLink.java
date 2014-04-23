package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class LabelLink extends Label {

	private static final long serialVersionUID = 1L;
	private Div div;
	private A link;

	public LabelLink(String title, String href) {
		super("", ContentMode.HTML);
		this.setStyleName("link");

		createContent(title, href);
		this.setValue(div.write());
	}

	private void createContent(String title, String href) {
		div = new Div();
		link = new A();
		if (href != null) {
			link.setHref(href);	
		}
		
		if (title != null)
		{
			link.appendText(title);
		}
		div.appendChild(link);
	}

	public void setIconLink(String source) {
		
		Img img = new Img("", source);
		div.appendChild(0, img);
		this.setValue(div.write());
	}

	public void setLink(String href) {
		div.getChild(1).setAttribute("href", href);
		this.setValue(div.write());
	}

}
