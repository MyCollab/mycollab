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
package com.esofthead.mycollab.vaadin.events;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DefaultPreviewFormHandler<T> implements PreviewFormHandler<T> {

	@Override
	public void gotoNext(T data) {
	}

	@Override
	public void gotoPrevious(T data) {
	}

	@Override
	public void onEdit(T data) {
	}

	@Override
	public void onDelete(T data) {
	}

	@Override
	public void onClone(T data) {
	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onAssign(T data) {
	}
	
	@Override
	public void onAdd(T data) {
		
	}

	@Override
	public void onExtraAction(String action, T data) {
		throw new MyCollabException("Must be override by sub class");

	}
}
