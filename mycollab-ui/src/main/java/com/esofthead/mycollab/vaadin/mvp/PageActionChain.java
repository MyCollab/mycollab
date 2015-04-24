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
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PageActionChain {
	private List<ScreenData> chains = new ArrayList<>();

	public PageActionChain(ScreenData... pageActionArr) {
		chains.addAll(Arrays.asList(pageActionArr));
	}

	public PageActionChain add(ScreenData pageAction) {
		chains.add(pageAction);
		return this;
	}

	public ScreenData pop() {
		if (chains.size() > 0) {
			ScreenData pageAction = chains.get(0);
			chains.remove(0);
			return pageAction;
		} else {
			return null;
		}

	}

	public ScreenData peek() {
		return chains.get(0);
	}

	public boolean hasNext() {
		return chains.size() > 0;
	}
}
