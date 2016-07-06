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
package com.mycollab.vaadin.events

import com.mycollab.core.MyCollabException

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class DefaultPreviewFormHandler[T] extends PreviewFormHandler[T] {
  override def gotoNext(data: T) {}

  override def gotoPrevious(data: T) {}

  override def onEdit(data: T) {}

  override def onDelete(data: T) {}

  override def onPrint(source: Object, data: T) {}

  override def onClone(data: T) {}

  override def onCancel {}

  override def onAssign(data: T) {}

  override def onAdd(data: T) {}

  override def onExtraAction(action: String, data: T): Unit = throw new MyCollabException("Must be override by sub class")
}
