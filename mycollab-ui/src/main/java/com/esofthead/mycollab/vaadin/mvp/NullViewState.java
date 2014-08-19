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

public class NullViewState extends ViewState {

	public NullViewState() {
		super(null, new EmptyPresenter(), null);
	}

	private static class EmptyPresenter implements IPresenter {

		private static final long serialVersionUID = 1L;

		@Override
		public void handleChain(ComponentContainer container,
				PageActionChain pageActionChain) {
			// do nothing

		}

		@Override
		public void go(ComponentContainer container, ScreenData data) {
			// do nothing

		}

		@Override
		public void go(ComponentContainer container, ScreenData data,
				boolean isHistoryTrack) {
			// do nothing

		}

		@Override
		public PageView getView() {
			// do nothing
			return null;
		}
	}
}
