/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.esofthead.mycollab.vaadin.mvp;

import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("rawtypes")
public class ViewState {
	private ComponentContainer container;
	private IPresenter presenter;
	private ScreenData<?> params;

	public ViewState(ComponentContainer container, IPresenter presenter,
			ScreenData<?> data) {
		this.container = container;
		this.presenter = presenter;
		this.params = data;
	}

	public IPresenter getPresenter() {
		return presenter;
	}

	public ScreenData<?> getParams() {
		return params;
	}

	public ComponentContainer getContainer() {
		return container;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("PageView State:").append("\n");
		result.append("   IPresenter: " + presenter).append("\n");
		result.append("   Params: " + ((params != null) ? params : "null"));
		return result.toString();
	}
}
