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
package com.mycollab.configuration

import com.mycollab.core.MyCollabException
import org.jasypt.exceptions.EncryptionOperationNotPossibleException
import org.jasypt.util.password.StrongPasswordEncryptor
import org.jasypt.util.text.BasicTextEncryptor

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Utility class to make encrypt and decrypt text
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
object EnDecryptHelper {
    private val strongEncryptor = StrongPasswordEncryptor()
    private var basicTextEncryptor = BasicTextEncryptor()

    init {
        basicTextEncryptor.setPassword(SiteConfiguration.getEnDecryptPassword())
    }

    /**
     * Encrypt password
     *
     * @param password
     * @return
     */
    @JvmStatic
    fun encryptSaltPassword(password: String): String = strongEncryptor.encryptPassword(password)

    @JvmStatic
    fun encryptText(text: String): String = basicTextEncryptor.encrypt(text)

    @JvmStatic
    fun encryptTextWithEncodeFriendly(text: String): String {
        try {
            return URLEncoder.encode(basicTextEncryptor.encrypt(text), "ASCII")
        } catch (e: UnsupportedEncodingException) {
            throw MyCollabException(e)
        }

    }

    @JvmStatic
    fun decryptText(text: String): String? {
        try {
            return basicTextEncryptor.decrypt(text)
        } catch (e: EncryptionOperationNotPossibleException) {
            throw MyCollabException("Can not decrypt the text--$text---")
        }

    }

    @JvmStatic
    fun decryptTextWithEncodeFriendly(text: String): String? {
        try {
            return basicTextEncryptor.decrypt(URLDecoder.decode(text, "ASCII"))
        } catch (e: Exception) {
            throw MyCollabException("Can not decrypt the text--$text---", e)
        }

    }

    /**
     * Check password `inputPassword` match with
     * `expectedPassword` in case `inputPassword` encrypt
     * or not
     *
     * @param inputPassword
     * @param expectedPassword
     * @param isPasswordEncrypt flag to denote `inputPassword` is encrypted or not
     * @return
     */
    @JvmStatic
    fun checkPassword(inputPassword: String, expectedPassword: String, isPasswordEncrypt: Boolean): Boolean {
        return if (isPasswordEncrypt) {
            inputPassword == expectedPassword
        } else {
            strongEncryptor.checkPassword(inputPassword, expectedPassword)
        }
    }
}
