package com.esofthead.mycollab.mobile.module.crm.ui;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.mobile.ui.IPagedBeanList;
import com.esofthead.mycollab.vaadin.mvp.AbstractMobilePageView;
import com.vaadin.ui.Button;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public abstract class AbstractRelatedItemSelectionView<T, S extends SearchCriteria>
		extends AbstractMobilePageView {

	private static final long serialVersionUID = -8672605824883862622L;

	private static String SELECTED_STYLENAME = "selected";

	protected AbstractRelatedListView<T, S> relatedListView;
	protected Set<T> selections = new HashSet<T>();
	protected IPagedBeanList<S, T> itemList;

	public AbstractRelatedItemSelectionView(String title,
			AbstractRelatedListView<T, S> relatedListView) {
		this.setCaption(title);
		this.relatedListView = relatedListView;
		initUI();
		this.setContent(itemList);
	}

	protected abstract void initUI();

	public void setSearchCriteria(S criteria) {
		itemList.setSearchCriteria(criteria);
	}

	protected class SelectableButton extends Button {
		private static final long serialVersionUID = 8233451662822791473L;
		private boolean selected = false;

		public SelectableButton() {
			addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 6187441057387703570L;

				@Override
				public void buttonClick(Button.ClickEvent event) {
					selected = !selected;
					if (selected) {
						addStyleName(SELECTED_STYLENAME);
					} else {
						removeStyleName(SELECTED_STYLENAME);
					}
				}
			});
		}

		public boolean isSelected() {
			return selected;
		}
	}
}
