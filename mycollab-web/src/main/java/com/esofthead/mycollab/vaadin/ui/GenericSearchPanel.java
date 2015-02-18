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

import java.util.ArrayList;
import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@SuppressWarnings("serial")
public class GenericSearchPanel<S extends SearchCriteria> extends
		CustomComponent implements HasSearchHandlers<S> {

	private List<SearchHandler<S>> handers;

	private Component headerRight;

	@Override
	public void addSearchHandler(final SearchHandler<S> handler) {
		if (this.handers == null) {
			this.handers = new ArrayList<>();
		}
		this.handers.add(handler);
	}

	public void notifySearchHandler(final S criteria) {
		if (this.handers != null) {
			for (final SearchHandler<S> handler : this.handers) {
				handler.onSearch(criteria);
			}
		}
	}

	@Override
	protected void setCompositionRoot(Component compositionRoot) {
		super.setCompositionRoot(compositionRoot);
		addHeaderRight(headerRight);
	}

	public void addHeaderRight(Component c) {
		if (c != null)
			this.headerRight = c;
		else
			return;

		Component root = getCompositionRoot();
		if (root != null) {
			((SearchLayout<?>) root).addHeaderRight(this.headerRight);
		}
	}

	abstract public static class SearchLayout<S extends SearchCriteria> extends
			CustomLayoutExt {
		protected GenericSearchPanel<S> searchPanel;

		public SearchLayout(final GenericSearchPanel<S> parent,
				final String layoutName) {
			super(layoutName);
			this.searchPanel = parent;
		}

		public void callSearchAction() {
			final S searchCriteria = this.fillupSearchCriteria();
			this.searchPanel.notifySearchHandler(searchCriteria);
		}

		public TextField createSeachSupportTextField(final TextField textField,
				final String keyField) {
			textField.addShortcutListener(new ShortcutListener(keyField,
					ShortcutAction.KeyCode.ENTER, null) {
				@Override
				public void handleAction(final Object sender,
						final Object target) {
					if (target == textField) {
						SearchLayout.this.callSearchAction();
					}
				}

			});
			return textField;
		}

		abstract protected S fillupSearchCriteria();

		abstract protected void addHeaderRight(Component c);

	}

	abstract public static class BasicSearchLayout<S extends SearchCriteria>
			extends SearchLayout<S> {
		private static final long serialVersionUID = 1L;
		protected ComponentContainer header;
		protected ComponentContainer body;

		public BasicSearchLayout(final GenericSearchPanel<S> parent) {
			super(parent, "basicSearch");
			this.setStyleName("basicSearchLayout");
			this.initLayout();
		}

		protected void initLayout() {
			this.header = this.constructHeader();
			this.body = this.constructBody();
            if (header != null) {
                this.addComponent(this.header, "basicSearchHeader");
            }

			this.addComponent(this.body, "basicSearchBody");
		}

		@Override
		protected void addHeaderRight(Component c) {
			if (this.header == null)
				return;

			this.header.addComponent(c);
		}

		abstract public ComponentContainer constructHeader();

		abstract public ComponentContainer constructBody();
	}

	abstract public static class AdvancedSearchLayout<S extends SearchCriteria>
			extends SearchLayout<S> {

		protected ComponentContainer header;
		protected ComponentContainer body;
		protected ComponentContainer footer;

		public AdvancedSearchLayout(final GenericSearchPanel<S> parent) {
			super(parent, "advancedSearch");
			this.setStyleName("advancedSearchLayout");
			this.initLayout();
		}

		protected void initLayout() {
			this.header = this.constructHeader();
			this.body = this.constructBody();
			this.footer = this.constructFooter();
			this.addComponent(this.header, "advSearchHeader");
			this.addComponent(this.body, "advSearchBody");
			this.addComponent(this.footer, "advSearchFooter");
		}

		@Override
		protected void addHeaderRight(Component c) {
			if (this.header == null)
				return;

			this.header.addComponent(c);
		}

		public abstract ComponentContainer constructHeader();

		public abstract ComponentContainer constructBody();

		public abstract ComponentContainer constructFooter();
	}
}
