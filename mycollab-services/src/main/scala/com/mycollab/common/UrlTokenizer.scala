/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common

import scala.beans.BeanProperty
/**
  * @author MyCollab Ltd
  * @since 5.3.5        
  */
class UrlTokenizer(var internalVal: String, @BeanProperty var remainValue: String, @BeanProperty var query: String) {
  
  @throws[InvalidTokenException]
  def getInt: Integer = {
    if (hasMoreTokens)
      getNextToken.toInt
    else throw new InvalidTokenException("Invalid token " + internalVal)
  }
  
  @throws[InvalidTokenException]
  def getString: String = if (hasMoreTokens) getNextToken
  else throw new InvalidTokenException("Invalid token " + internalVal)
  
  def hasMoreTokens: Boolean = !(remainValue == "")
  
  private def getNextToken: String = {
    val index: Int = remainValue.indexOf("/")
    if (index < 0) {
      val result: String = remainValue + ""
      remainValue = ""
      result
    }
    else {
      val result: String = remainValue.substring(0, index)
      remainValue = remainValue.substring(index + 1)
      result
    }
  }
}

object UrlTokenizer {
  def apply(url: String): UrlTokenizer = {
    var internalVal = if (url.startsWith("/")) url.substring(1) else url
    val queryIndex: Int = internalVal.indexOf("?")
    var query, remainStrVal = ""
    if (queryIndex != -1) {
      query = internalVal.substring(queryIndex + 1)
      internalVal = internalVal.substring(0, queryIndex)
    }
    internalVal = UrlEncodeDecoder.decode(internalVal)
    remainStrVal = internalVal
    new UrlTokenizer(internalVal, remainStrVal, query)
  }
}
