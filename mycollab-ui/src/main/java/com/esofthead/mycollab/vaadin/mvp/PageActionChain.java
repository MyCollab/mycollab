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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PageActionChain {
	private static Logger log = LoggerFactory.getLogger(PageActionChain.class);

	private List<ScreenData> chains = new ArrayList<ScreenData>();

	public PageActionChain(ScreenData... pageActionArr) {
		chains.addAll(Arrays.asList(pageActionArr));
		log.debug("Chain size: " + this + "---" + chains.size());
	}

	public PageActionChain add(ScreenData pageAction) {
		chains.add(pageAction);
		log.debug("Chain size: " + this + "---" + chains.size());
		return this;
	}

	public ScreenData pop() {
		log.debug("Pop pageActionChain " + this + "---" + chains.size());
		if (chains.size() > 0) {
			ScreenData pageAction = chains.get(0);
			chains.remove(0);
			return pageAction;
		} else {
			return null;
		}

	}

	public ScreenData peek() {
		ScreenData pageAction = chains.get(0);
		return pageAction;
	}

	public boolean hasNext() {
		return chains.size() > 0;
	}
}
