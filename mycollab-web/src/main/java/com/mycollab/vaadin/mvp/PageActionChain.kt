/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.mvp

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class PageActionChain(vararg pageActionArr: ScreenData<Any>) {
    private val chains = mutableListOf<ScreenData<Any>>()

    init {
        chains.addAll(pageActionArr)
    }

    fun add(pageAction: ScreenData<Any>): PageActionChain {
        chains.add(pageAction)
        return this
    }

    fun pop(): ScreenData<Any>? {
        return if (chains.size > 0) {
            val pageAction = chains[0]
            chains.removeAt(0)
            pageAction
        } else null
    }

    fun peek(): ScreenData<Any> = chains[0]

    fun hasNext(): Boolean = chains.size > 0
}