package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.vaadin.mvp.AbstractMobilePageView;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 * 
 */
public abstract class AbstractSelectionView<B> extends AbstractMobilePageView {
	private static final long serialVersionUID = 1L;

	protected AbstractSelectionCustomField<B> selectionField;

	public AbstractSelectionCustomField<B> getSelectionField() {
		return selectionField;
	}

	public void setSelectionField(AbstractSelectionCustomField<B> selectionField) {
		this.selectionField = selectionField;
	}
}
