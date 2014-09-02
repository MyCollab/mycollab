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

import com.esofthead.mycollab.core.arguments.SearchCriteria;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 * @param <P>
 */
public class ScreenData<P> {

	private P params;

	public ScreenData(P params) {
		this.params = params;
	}

	public P getParams() {
		return params;
	}

	public void setParams(P params) {
		this.params = params;
	}

	public static class Add<P> extends ScreenData<P> {

		public Add(P params) {
			super(params);
		}
	}

	public static class Edit<P> extends ScreenData<P> {

		public Edit(P params) {
			super(params);
		}
	}

	public static class Preview<P> extends ScreenData<P> {

		public Preview(P params) {
			super(params);
		}
	}

	public static class Search<S extends SearchCriteria> extends ScreenData<S> {

		public Search(S params) {
			super(params);
		}
	}
}
