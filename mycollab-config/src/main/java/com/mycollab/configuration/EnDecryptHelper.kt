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
