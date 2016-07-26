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

import java.io.UnsupportedEncodingException
import java.net.{URLDecoder, URLEncoder}

import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import org.apache.commons.codec.binary.Base64

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
object UrlEncodeDecoder {
  def encode(str: String): String = {
    try {
      if (StringUtils.isBlank(str)) "" else URLEncoder.encode(new String(Base64.encodeBase64URLSafe(str.getBytes("UTF-8")), "UTF-8"), "UTF-8")
    }
    catch {
      case ex: UnsupportedEncodingException => throw new MyCollabException(ex)
    }
  }
  
  /**
    * @param str
    * @return
    */
  def decode(str: String): String = {
    try {
      val decodeStr = URLDecoder.decode(str, "UTF8")
      new String(Base64.decodeBase64(decodeStr.getBytes("UTF-8")), "UTF-8")
    } catch {
      case e: Exception => ""
    }
  }
  
  /**
    * @param str
    * @return
    */
  def encode(str: Number): String = encode(str.toString)
}
