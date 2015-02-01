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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public abstract class ReadViewLayout extends CustomLayoutExt {
	private static final long serialVersionUID = 1L;

	public ReadViewLayout() {
		super("readView");
	}

	public void addHeader(final ComponentContainer header) {
		this.addComponent(header, "readViewHeader");
	}

	public void addBody(final ComponentContainer body) {
		this.addComponent(body, "readViewBody");
	}

	public void addBottomControls(final ComponentContainer bottomControls) {
		this.addComponent(bottomControls, "readViewBottomControls");
	}

	public void clearTitleStyleName() {
		throw new MyCollabException("Must be implemented in the sub class");
	}

	public void addTitleStyleName(final String styleName) {
		throw new MyCollabException("Must be implemented in the sub class");
	}

	public void setTitleStyleName(final String styleName) {
		throw new MyCollabException("Must be implemented in the sub class");
	}

	public void removeTitleStyleName(final String styleName) {
		throw new MyCollabException("Must be implemented in the sub class");
	}

	public void setTitle(final String title) {
		throw new MyCollabException("Must be implemented in the sub class");
	}
}
