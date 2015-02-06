/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.events.SelectionOptionHandler;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class SelectionOptionButton extends SplitButton implements
		HasSelectionOptionHandlers {

	private static final long serialVersionUID = 1L;
	private boolean isSelectAll = false;
	private boolean isSelected = false;

	@SuppressWarnings("rawtypes")
	private final HasSelectableItemHandlers selectableItemHandlers;

	private static Resource selectIcon = MyCollabResource
			.newResource(WebResourceIds._16_checkbox);
	private static Resource unSelectIcon = MyCollabResource
			.newResource(WebResourceIds._16_checkbox_empty);

	private Set<SelectionOptionHandler> handlers;

	private final Button selectAllBtn;
	private final Button selectThisPageBtn;

	@SuppressWarnings("serial")
	public SelectionOptionButton(
			@SuppressWarnings("rawtypes") final HasSelectableItemHandlers selectableItemHandlers) {
		super();
		this.selectableItemHandlers = selectableItemHandlers;
		addStyleName(UIConstants.THEME_BLUE_LINK);
		setIcon(SelectionOptionButton.unSelectIcon);

		addClickListener(new SplitButtonClickListener() {
			@Override
			public void splitButtonClick(final SplitButtonClickEvent event) {
				toggleChangeOption();
			}
		});

		addPopupVisibilityListener(new SplitButtonPopupVisibilityListener() {
			@Override
			public void splitButtonPopupVisibilityChange(
					final SplitButtonPopupVisibilityEvent event) {
				if (event.isPopupVisible()) {
					selectAllBtn.setCaption("Select All ("
							+ SelectionOptionButton.this.selectableItemHandlers
									.totalItemsCount() + ")");

					selectThisPageBtn.setCaption("Select This Page ("
							+ SelectionOptionButton.this.selectableItemHandlers
									.currentViewCount() + ")");
				}
			}
		});

		final VerticalLayout selectContent = new VerticalLayout();
		selectContent.setWidth("150px");

		selectAllBtn = new ButtonLink("", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				isSelectAll = true;
				SelectionOptionButton.this
						.setIcon(SelectionOptionButton.selectIcon);
				fireSelectAll();
				SelectionOptionButton.this.setPopupVisible(false);
			}
		});
		selectContent.addComponent(selectAllBtn);

		selectThisPageBtn = new ButtonLink("", new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				isSelectAll = false;
				SelectionOptionButton.this
						.setIcon(SelectionOptionButton.selectIcon);
				fireSelectCurrentPage();
				SelectionOptionButton.this.setPopupVisible(false);
			}
		});
		selectContent.addComponent(selectThisPageBtn);

		Button deSelectBtn = new ButtonLink("Deselect All",
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						isSelectAll = false;
						SelectionOptionButton.this
								.setIcon(SelectionOptionButton.unSelectIcon);
						fireDeselect();
						SelectionOptionButton.this.setPopupVisible(false);
					}
				});
		selectContent.addComponent(deSelectBtn);
		setContent(selectContent);
	}

	@Override
	public void addSelectionOptionHandler(final SelectionOptionHandler handler) {
		if (handlers == null) {
			handlers = new HashSet<>();
		}
		handlers.add(handler);
	}

	private void fireDeselect() {
		if (handlers != null) {
			for (final SelectionOptionHandler handler : handlers) {
				handler.onDeSelect();
			}
		}
	}

	private void fireSelectAll() {
		if (handlers != null) {
			for (final SelectionOptionHandler handler : handlers) {
				handler.onSelectAll();
			}
		}

	}

	private void fireSelectCurrentPage() {
		if (handlers != null) {
			for (final SelectionOptionHandler handler : handlers) {
				handler.onSelectCurrentPage();
			}
		}
	}

	public void setSelectedChecbox(final boolean selected) {
		isSelected = selected;
		final Resource icon = (selected) ? SelectionOptionButton.selectIcon
				: SelectionOptionButton.unSelectIcon;
		SelectionOptionButton.this.setIcon(icon);
	}

	private void toggleChangeOption() {
		if (isSelectAll) {
			return;
		}

		isSelected = !isSelected;
		final Resource icon = (isSelected) ? SelectionOptionButton.selectIcon
				: SelectionOptionButton.unSelectIcon;
		this.setIcon(icon);

		if (isSelected) {
			fireSelectCurrentPage();
		} else {
			fireDeselect();
		}
	}
}
