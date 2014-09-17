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

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.mobile.ui.AbstractListPresenter;
import com.esofthead.mycollab.mobile.ui.AbstractMobileTabPageView;
import com.esofthead.mycollab.mobile.ui.IListView;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 * @param <V>
 * @param <S>
 * @param <B>
 */
public abstract class CrmListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean>
		extends AbstractListPresenter<V, S, B> {
	private static final long serialVersionUID = -8215491621059981765L;

	public CrmListPresenter(Class<V> viewClass) {
		super(viewClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		Component targetView;
		NavigationManager currentNav = (NavigationManager) container;

		if (view.getParent() != null
				&& view.getParent() instanceof AbstractMobileTabPageView) {
			targetView = view.getParent();
			((AbstractMobileTabPageView) view.getParent()).setSelectedTab(view
					.getWidget());
		} else {
			targetView = view;
		}

		currentNav.navigateTo(targetView);

		doSearch((S) data.getParams());
	}

}
