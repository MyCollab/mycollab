package com.esofthead.mycollab.vaadin.ui;

import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 * 
 */

public class SortButton extends Button {

	private static final long serialVersionUID = 6899070243378436412L;

	private boolean isDesc = true;

	public SortButton() {
		super();
		this.setStyleName("sort-btn");
		this.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 4326727093112639245L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				SortButton.this.toggleSortOrder();
			}
		});
	}

	public SortButton(String caption, ClickListener listener) {
		this(caption);
		this.addClickListener(listener);
	}

	public SortButton(String caption) {
		this();
		this.setCaption(caption);
	}

	public void toggleSortOrder() {
		this.isDesc = !this.isDesc;
		if (this.isDesc) {
			this.addStyleName("desc");
			this.removeStyleName("asc");
		} else {
			this.addStyleName("asc");
			this.removeStyleName("desc");
		}
	}

	public boolean isDesc() {
		return this.isDesc;
	}

}
