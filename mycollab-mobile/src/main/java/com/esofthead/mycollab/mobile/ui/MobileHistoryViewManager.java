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
package com.esofthead.mycollab.mobile.ui;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.HISTORY_VAL;

import java.util.ArrayList;
import java.util.List;

import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ViewState;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class MobileHistoryViewManager extends HistoryViewManager {

	@SuppressWarnings("unchecked")
	public static ViewState pop(boolean firstTime) {
		List<ViewState> history = (List<ViewState>) MyCollabSession
				.getVariable(HISTORY_VAL);
		if (history == null) {
			history = new ArrayList<ViewState>();
			MyCollabSession.putVariable(HISTORY_VAL, history);
		}
		if (firstTime) {
			if (history.size() >= 2) {
				ViewState viewState = history.get(history.size() - 2);
				history.remove(history.size() - 1);
				history.remove(history.size() - 1);
				MyCollabSession.putVariable(HISTORY_VAL, history);
				return viewState;
			}
		} else {
			if (history.size() >= 1) {
				ViewState viewState = history.get(history.size() - 1);
				history.remove(history.size() - 1);
				MyCollabSession.putVariable(HISTORY_VAL, history);
				return viewState;
			}
		}
		return new NullViewState();
	}

	public static ViewState pop() {
		return MobileHistoryViewManager.pop(true);
	}

	@SuppressWarnings("unchecked")
	public static ViewState peak() {
		List<ViewState> history = (List<ViewState>) MyCollabSession
				.getVariable(HISTORY_VAL);
		if (history == null) {
			history = new ArrayList<ViewState>();
			MyCollabSession.putVariable(HISTORY_VAL, history);
		}
		if (history.size() >= 1) {
			ViewState viewState = history.get(history.size() - 1);
			return viewState;
		}
		return new NullViewState();
	}

}
