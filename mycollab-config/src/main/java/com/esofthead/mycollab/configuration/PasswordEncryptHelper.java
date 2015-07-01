/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Utility class to make encrypt and decrypt text
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PasswordEncryptHelper {
    private static StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    private static BasicTextEncryptor basicTextEncryptor;

    static {
        basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(SiteConfiguration.getEnDecryptPassword());
    }

    /**
     * Encrypt password
     *
     * @param password
     * @return
     */
    public static String encryptSaltPassword(String password) {
        return passwordEncryptor.encryptPassword(password);
    }

    public static String encryptText(String text) {
        return basicTextEncryptor.encrypt(text);
    }

    public static String decryptText(String text) {
        return basicTextEncryptor.decrypt(text);
    }

    /**
     * Check password <code>inputPassword</code> match with
     * <code>expectedPassword</code> in case <code>inputPassword</code> encrypt
     * or not
     *
     * @param inputPassword
     * @param expectedPassword
     * @param isPasswordEncrypt flag to denote <code>inputPassword</code> is encrypted or not
     * @return
     */
    public static boolean checkPassword(String inputPassword,
                                        String expectedPassword, boolean isPasswordEncrypt) {
        if (!isPasswordEncrypt) {
            return passwordEncryptor.checkPassword(inputPassword,
                    expectedPassword);
        } else {
            return inputPassword.equals(expectedPassword);
        }
    }
}
