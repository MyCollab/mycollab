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

import static com.esofthead.mycollab.common.MyCollabSession.HISTORY_VAL;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.MyCollabSession;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class HistoryViewManager {
	private static Logger log = LoggerFactory
			.getLogger(HistoryViewManager.class);

	public static void addHistory(ViewState viewState) {
		List<ViewState> history = getViewState();
		if (history.size() > 10) {
			history.remove(0);
		}

		history.add(viewState);
	}

	public static ViewState back() {
		List<ViewState> history = getViewState();
		if (history.size() >= 2) {
			ViewState viewState = history.get(history.size() - 2);
			history.remove(history.size() - 1);
			history.remove(history.size() - 1);

			if (viewState.getPresenter().getView() instanceof IModule) {
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

	private static List<ViewState> getViewState() {
		List<ViewState> history = (List<ViewState>) MyCollabSession
				.getVariable(HISTORY_VAL);
		if (history == null) {
			history = new ArrayList<ViewState>();
			MyCollabSession.putVariable(HISTORY_VAL, history);
		}
		return history;
	}
}
