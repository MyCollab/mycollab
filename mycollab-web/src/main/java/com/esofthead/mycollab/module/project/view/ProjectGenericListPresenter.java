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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.vaadin.desktop.ui.ListSelectionPresenter;
import com.esofthead.mycollab.vaadin.desktop.ui.ListView;
import com.esofthead.mycollab.vaadin.mvp.LazyPageView;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public abstract class ProjectGenericListPresenter<V extends ListView<S, B>, S extends SearchCriteria, B extends ValuedBean>
		extends ListSelectionPresenter<V, S, B> {
	private static final long serialVersionUID = 7270489652418186012L;

	private PageView candidateView;

	private Class<? extends PageView> noItemFallbackViewClass;

	public ProjectGenericListPresenter(Class<V> viewClass,
			Class<? extends PageView> noItemFallbackViewClass) {
		super(viewClass);
		this.noItemFallbackViewClass = noItemFallbackViewClass;
	}

	@Override
	public V getView() {
		super.getView();
		this.candidateView = view;
		return view;
	}

	public void displayListView(ComponentContainer container, ScreenData<?> data) {
		this.candidateView = view;
		displayView(container, data);
	}

	public void displayNoExistItems(ComponentContainer container,
			ScreenData<?> data) {
		this.candidateView = ViewManager
				.getCacheComponent(noItemFallbackViewClass);
		displayView(container, data);
	}

	private void displayView(ComponentContainer container, ScreenData<?> data) {
		container.removeAllComponents();
		if (candidateView instanceof LazyPageView) {
			candidateView.removeAllComponents();
		}
		container.addComponent(candidateView);
	}

}
