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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class HistoryViewManager {
	private static Logger log = LoggerFactory
			.getLogger(HistoryViewManager.class);

	private static List<ViewState> history = new ArrayList<ViewState>();

	public static void addHistory(ViewState viewState) {
		if (history.size() > 10) {
			history.remove(0);
		}

		history.add(viewState);
	}

	public static ViewState getPreviousViewState() {
		if (history.size() >= 2) {
			ViewState viewState = history.get(history.size() - 2);
			return viewState;
		} else {
			return new NullViewState();
		}
	}

	public static ViewState back() {
		if (history.size() >= 2) {
			ViewState viewState = history.get(history.size() - 2);
			history.remove(history.size() - 1);
			history.remove(history.size() - 1);

			if (viewState.getPresenter().initView() instanceof IModule) {
				return new NullViewState();
			} else {
				log.debug("Back to view: " + viewState.getPresenter());

				viewState.getPresenter().go(viewState.getContainer(),
						viewState.getParams());
				return viewState;
			}

		} else {
			return new NullViewState();
		}
	}
}
