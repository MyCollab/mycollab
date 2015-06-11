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

import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.core.utils.BeanUtility
import com.esofthead.mycollab.vaadin.ui.NotificationUtil
import org.slf4j.{Logger, LoggerFactory}

import scala.annotation.varargs
import scala.collection.mutable._

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
abstract class UrlResolver {
    private var subResolvers: Map[String, UrlResolver] = _
    protected var defaultUrlResolver: UrlResolver = _

    def addSubResolver(key: String, subResolver: UrlResolver) {
        if (subResolvers == null) {
            subResolvers = Map[String, UrlResolver]()
        }
        subResolvers += key -> subResolver
    }

    def getSubResolver(key:String): UrlResolver = subResolvers(key)

    @varargs def handle(params: String*): Unit = {
        try {
            if (params.length > 0) {
                val key: String = params(0)
                if (subResolvers == null) {
                    handlePage(params:_*)
                }
                else {
                    var urlResolver: UrlResolver = subResolvers(key)
                    if (urlResolver == null) {
                        if (defaultUrlResolver != null) {
                            urlResolver = defaultUrlResolver
                        }
                        else {
                            throw new MyCollabException(String.format("Can not register resolver key %s for Resolver: %s", key, this))
                        }
                    }

                    UrlResolver.LOG.debug("Handle url in resolver: " + urlResolver)
                    urlResolver.handle(params.tail:_*)
                }
            }
            else {
                handlePage()
            }
        }
        catch {
            case e: Exception => {
                UrlResolver.LOG.error("Error while navigation", e)
                defaultPageErrorHandler
                NotificationUtil.showRecordNotExistNotification
            }
        }
    }


    protected def defaultPageErrorHandler(): Unit

    /**
     * @param params
     */
    @varargs protected def handlePage(params: String*): Unit = {
        UrlResolver.LOG.debug(String.format("Handle page: %s with params: %s", this, BeanUtility.printBeanObj(params)));
    }
}

object UrlResolver {
    val LOG: Logger = LoggerFactory.getLogger(classOf[UrlResolver]);
}
