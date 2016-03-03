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
package com.esofthead.mycollab.vaadin.mvp

import scala.collection.mutable

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class PageActionChain {
  val chains: mutable.Buffer[ScreenData[_]] = mutable.Buffer()

  def this(pageActionArr: Array[ScreenData[_]]) = {
    this()
    chains.appendAll(pageActionArr)
  }

  def this(param: ScreenData[_]) = this(Array[ScreenData[_]](param))

  def this(param1: ScreenData[_], param2: ScreenData[_]) = this(Array[ScreenData[_]](param1, param2))

  def add(pageAction: ScreenData[_]): PageActionChain = {
    chains += pageAction
    return this
  }

  def pop: ScreenData[_] = {
    if (chains.size > 0) {
      val pageAction = chains(0)
      chains.remove(0)
      return pageAction
    }
    else return null
  }

  def peek: ScreenData[_] = chains(0)

  def hasNext: Boolean = chains.length > 0
}
