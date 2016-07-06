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
package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.ui.NotificationUtil
import com.mycollab.core.MyCollabException
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

  def getSubResolver(key: String): UrlResolver = subResolvers(key)

  @varargs def handle(params: String*): Unit = {
    try {
      if (params.length > 0) {
        var key = params(0)
        val index = key.indexOf('?')
        if (index > -1) {
          key = key.substring(0, index)
        }
        if (subResolvers == null) {
          handlePage(params: _*)
        }
        else {
          val urlResolver = subResolvers.get(key)
          urlResolver match {
            case Some(value) => value.handle(params.tail: _*)
            case None => {
              if (defaultUrlResolver != null) {
                defaultUrlResolver.handle(params: _*)
              }
              else {
                throw new MyCollabException(String.format("Can not register resolver key %s for Resolver: %s", key, this))
              }
            }
          }
        }
      }
      else {
        handlePage()
      }
    }
    catch {
      case e: Exception => {
        UrlResolver.LOG.error("Error while navigation", e)
        defaultPageErrorHandler()
        NotificationUtil.showRecordNotExistNotification()
      }
    }
  }


  protected def defaultPageErrorHandler(): Unit

  /**
    * @param params
    */
  @varargs protected def handlePage(params: String*): Unit = {}
}

object UrlResolver {
  val LOG: Logger = LoggerFactory.getLogger(classOf[UrlResolver]);
}
