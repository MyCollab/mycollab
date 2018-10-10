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

import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.vaadin.ui.NotificationUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
abstract class UrlResolver {
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(UrlResolver::class.java)
    }

    private  var subResolvers: MutableMap<String, UrlResolver>? = null
    @JvmField protected var defaultUrlResolver: UrlResolver? = null

    /**
     * @param params
     */
    protected open fun handlePage(vararg params: String) {}

    protected abstract fun defaultPageErrorHandler()

    fun addSubResolver(key: String, subResolver: UrlResolver) {
        if (subResolvers == null) {
            subResolvers = mutableMapOf()
        }
        subResolvers?.put(key, subResolver)
    }

    fun getSubResolver(key: String) = subResolvers?.get(key)

    open fun handle(vararg params: String) {
        try {
            if (params.isNotEmpty()) {
                var key = params[0]
                val index = key.indexOf('?')
                if (index > -1) {
                    key = key.substring(0, index)
                }
                if (subResolvers == null) {
                    handlePage(*params)
                } else {
                    val urlResolver = subResolvers?.get(key)
                    when (urlResolver) {
                        null -> defaultUrlResolver?.handle(*params) ?: throw ResourceNotFoundException("Can not find resolver key $key for Resolver: $this")
                        else -> urlResolver.handle(*params.drop(1).toTypedArray())

                    }
                }
            } else {
                defaultUrlResolver?.handle(*params) ?: handlePage()
            }
        } catch (e: Exception) {
            LOG.error("Error while navigation ${Arrays.toString(params)} for the resolver $this", e)
            defaultPageErrorHandler()
            NotificationUtil.showRecordNotExistNotification()
        }
    }
}