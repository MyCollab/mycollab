package com.mycollab.common

import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import org.apache.commons.codec.binary.Base64
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object UrlEncodeDecoder {
    @JvmStatic fun encode(str: String?): String =
            try {
                if (StringUtils.isBlank(str)) "" else URLEncoder.encode(String(Base64.encodeBase64URLSafe(str?.toByteArray(Charsets.UTF_8))), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                throw  MyCollabException(e)
            }

    /**
     * @param str
     * @return
     */
    @JvmStatic fun decode(str: String): String =
            try {
                val decodeStr = URLDecoder.decode(str, "UTF8")
                String(Base64.decodeBase64(decodeStr.toByteArray(Charsets.UTF_8)))
            } catch (e: Exception) {
                ""
            }

    /**
     * @param str
     * @return
     */
    @JvmStatic fun encode(str: Number?): String = encode(str?.toString())
}