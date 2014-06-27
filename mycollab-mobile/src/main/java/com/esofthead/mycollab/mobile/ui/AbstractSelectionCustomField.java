package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public abstract class AbstractSelectionCustomField<B> extends
		CustomField<Integer> implements FieldSelection<B> {
	private static final long serialVersionUID = 1L;

	private Class<? extends AbstractSelectionView<B>> targetSelectionViewCls;
	protected MobileNavigationButton navButton = new MobileNavigationButton();
	protected B beanItem;

	public AbstractSelectionCustomField(
			Class<? extends AbstractSelectionView<B>> targetSelectionView) {
		this.targetSelectionViewCls = targetSelectionView;
	}

	@Override
	protected Component initContent() {
		navButton.setStyleName("combo-box");
		try {

			AbstractSelectionView<B> selectionView = targetSelectionViewCls
					.newInstance();
			selectionView.setSelectionField(this);
			navButton.setTargetView(selectionView);
			navButton.setWidth("100%");
			return navButton;
		} catch (SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException e) {
			throw new MyCollabException(e);
		}
	}
}
