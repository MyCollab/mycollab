package com.esofthead.mycollab.mobile.module.crm.ui;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.vaadin.AppContext;
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

	protected static String SELECTED_STYLENAME = "selected";

	protected final AbstractRelatedListView<T, S> relatedListView;
	protected Set<T> selections = new HashSet<T>();
	protected AbstractPagedBeanList<S, T> itemList;

	public AbstractRelatedItemSelectionView(String title,
			final AbstractRelatedListView<T, S> relatedListView) {
		this.setCaption(title);
		this.relatedListView = relatedListView;
		// if (!relatedListView.getItemList().getCurrentDataList().isEmpty())
		// this.selections = new HashSet<T>(relatedListView.getItemList()
		// .getCurrentDataList());
		initUI();
		this.setContent(itemList);
		Button doneBtn = new Button(
				AppContext.getMessage(GenericI18Enum.M_BUTTON_DONE),
				new Button.ClickListener() {
					private static final long serialVersionUID = -652476076947907047L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						if (!selections.isEmpty()) {
							relatedListView
									.fireSelectedRelatedItems(selections);
						}
					}
				});
		doneBtn.setStyleName("save-btn");
		this.setRightComponent(doneBtn);
	}

	protected abstract void initUI();

	public void setSearchCriteria(S criteria) {
		itemList.setSearchCriteria(criteria);
	}

	protected class SelectableButton extends Button {
		private static final long serialVersionUID = 8233451662822791473L;
		private boolean selected = false;

		public SelectableButton(String caption) {
			super(caption);
			setStyleName("selectable-button");
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

		public void select() {
			this.selected = true;
			this.addStyleName(SELECTED_STYLENAME);
		}
	}
}
