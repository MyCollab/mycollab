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
package com.esofthead.mycollab.vaadin.events

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
trait PreviewFormHandler[T] {
    /**
     *
     * @param data
     */
    def gotoNext(data: T)

    /**
     *
     * @param data
     */
    def gotoPrevious(data: T)

    /**
     *
     * @param data
     */
    def onAssign(data: T)

    /**
     *
     * @param data
     */
    def onEdit(data: T)

    /**
     *
     * @param data
     */
    def onAdd(data: T)

    /**
     *
     * @param data
     */
    def onDelete(data: T)

    /**
     *
     * @param data
     */
    def onClone(data: T)

    /**
     *
     */
    def onCancel

    /**
     *
     * @param action
     * @param data
     */
    def onExtraAction(action: String, data: T): Unit
}
