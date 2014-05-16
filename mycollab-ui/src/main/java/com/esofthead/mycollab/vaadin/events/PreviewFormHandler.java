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

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 * @param <T>
 */
public interface PreviewFormHandler<T> {

	/**
	 * 
	 * @param data
	 */
	void gotoNext(T data);

	/**
	 * 
	 * @param data
	 */
	void gotoPrevious(T data);

	/**
	 * 
	 * @param data
	 */
	void onAssign(T data);

	/**
	 * 
	 * @param data
	 */
	void onEdit(T data);
	
	/**
	 * 
	 * @param data
	 */
	void onAdd(T data);

	/**
	 * 
	 * @param data
	 */
	void onDelete(T data);

	/**
	 * 
	 * @param data
	 */
	void onClone(T data);

	/**
     * 
     */
	void onCancel();

	/**
	 * 
	 * @param action
	 * @param data
	 */
	void onExtraAction(String action, T data);
}
