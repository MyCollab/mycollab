package com.mycollab.mobile.ui;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class PrefixNameListSelect extends ValueListSelect {
	private static final long serialVersionUID = 1L;

	public PrefixNameListSelect() {
		super();
		this.setWidth("50px");
		setCaption(null);
		this.loadData("Mr.", "Ms.", "Mrs.", "Dr.", "Prof.");
	}
}
