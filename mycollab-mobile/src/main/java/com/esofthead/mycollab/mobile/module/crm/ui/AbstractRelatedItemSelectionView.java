/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.ui;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.vaadin.AppContext;
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
			setStyleName("list-item selectable-button");
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
