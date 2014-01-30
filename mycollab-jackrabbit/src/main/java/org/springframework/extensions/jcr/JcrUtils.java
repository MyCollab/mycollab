/**
 * This file is part of mycollab-jackrabbit.
 *
 * mycollab-jackrabbit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-jackrabbit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-jackrabbit.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.springframework.extensions.jcr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Repository;

/**
 * Utility class for Java Content Repository. The hex escaping/unescaping is based on Brian Moseley
 * <bcm@osafoundation.org> work.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class JcrUtils {

    /**
     * Class used for escaping XML names which contain restricted character as defined in the JCR spec version
     * 1.0 Section 6.2.5.2. (i.e. "/", ":", "[", "]", "*", "'",
     * """, "|", and all whitespace characters other than " "). The escaping schema used is described in the
     * same document in paragraphs 6.4.3 and 6.4.4. The class was initially imported from the <a href="http://svn.apache.org/viewcvs.cgi/incubator/jackrabbit/trunk/jackrabbit/src/main/java/org/apache/jackrabbit/util/ISO9075.java?view=markup"
     * >Jackrabbit project</a>. Implements the encode and decode routines as specified for XML name to SQL
     * identifier conversion in ISO 9075-14:2003.<br/>
     * If a character <code>c</code> is not valid at a certain position in an XML 1.0 NCName it is encoded in
     * the form: '_x' + hexValueOf(c) + '_'
     * <p/>
     */
    protected static class ISO9075 {

        /** Pattern on an encoded character */
        private final Pattern ENCODE_PATTERN = Pattern.compile("_x\\p{XDigit}{4}_");

        /** Padding characters */
        private final char[] PADDING = new char[] { '0', '0', '0' };

        /** All the possible hex digits */
        private final String HEX_DIGITS = "0123456789abcdefABCDEF";

        /**
         * Encodes <code>name</code> as specified in ISO 9075.
         * @param name the <code>String</code> to encode.
         * @return the encoded <code>String</code> or <code>name</code> if it does not need encoding.
         */
        public String encode(String name) {
            // quick check for root node name
            if (name.length() == 0) {
                return name;
            }
            if (XMLChar.isValidName(name) && name.indexOf("_x") < 0) {
                // already valid
                return name;
            }
            // encode
            StringBuffer encoded = new StringBuffer();
            for (int i = 0; i < name.length(); i++) {
                if (i == 0) {
                    // first character of name
                    if (XMLChar.isNameStart(name.charAt(i))) {
                        if (needsEscaping(name, i)) {
                            // '_x' must be encoded
                            encode('_', encoded);
                        } else {
                            encoded.append(name.charAt(i));
                        }
                    } else {
                        // not valid as first character -> encode
                        encode(name.charAt(i), encoded);
                    }
                } else if (!XMLChar.isName(name.charAt(i))) {
                    encode(name.charAt(i), encoded);
                } else {
                    if (needsEscaping(name, i)) {
                        // '_x' must be encoded
                        encode('_', encoded);
                    } else {
                        encoded.append(name.charAt(i));
                    }
                }
            }
            return encoded.toString();

        }

        /**
         * Decodes the <code>name</code>.
         * @param name the <code>String</code> to decode.
         * @return the decoded <code>String</code>.
         */
        public String decode(String name) {
            // quick check
            if (name.indexOf("_x") < 0) {
                // not encoded
                return name;
            }
            StringBuffer decoded = new StringBuffer();
            Matcher m = ENCODE_PATTERN.matcher(name);
            while (m.find()) {
                m.appendReplacement(decoded, Character.toString((char) Integer.parseInt(m.group().substring(2, 6), 16)));
            }
            m.appendTail(decoded);
            return decoded.toString();
        }

        // -------------------------< internal
        // >-------------------------------------

        /**
         * Encodes the character <code>c</code> as a String in the following form:
         * <code>"_x" + hex value of c + "_"</code>. Where the hex value has four digits if the character with
         * possibly leading zeros.
         * <p/>
         * Example: ' ' (the space character) is encoded to: _x0020_
         * @param c the character to encode
         * @param b the encoded character is appended to <code>StringBuffer</code> <code>b</code>.
         */
        private void encode(char c, StringBuffer b) {
            b.append("_x");
            String hex = Integer.toHexString(c);
            b.append(PADDING, 0, 4 - hex.length());
            b.append(hex);
            b.append("_");
        }

        /**
         * Returns true if <code>name.charAt(location)</code> is the underscore character and the following
         * character sequence is 'xHHHH_' where H is a hex digit.
         * @param name the name to check.
         * @param location the location to look at.
         * @throws ArrayIndexOutOfBoundsException if location > name.length()
         */
        private boolean needsEscaping(String name, int location) throws ArrayIndexOutOfBoundsException {
            if (name.charAt(location) == '_' && name.length() >= location + 6) {
                return name.charAt(location + 1) == 'x' && HEX_DIGITS.indexOf(name.charAt(location + 2)) != -1 && HEX_DIGITS.indexOf(name.charAt(location + 3)) != -1
                        && HEX_DIGITS.indexOf(name.charAt(location + 4)) != -1 && HEX_DIGITS.indexOf(name.charAt(location + 5)) != -1;
            }
            return false;
        }
    }

    private static ISO9075 escaper = new ISO9075();

    public static boolean supportsLevel2(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.LEVEL_2_SUPPORTED));
    }

    public static boolean supportsTransactions(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_TRANSACTIONS_SUPPORTED));
    }

    public static boolean supportsVersioning(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_VERSIONING_SUPPORTED));
    }

    public static boolean supportsObservation(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_OBSERVATION_SUPPORTED));
    }

    public static boolean supportsLocking(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_LOCKING_SUPPORTED));
    }

    public static boolean supportsSQLQuery(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.OPTION_QUERY_SQL_SUPPORTED));
    }

    public static boolean supportsXPathPosIndex(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.QUERY_XPATH_POS_INDEX));
    }

    public static boolean supportsXPathDocOrder(Repository repository) {
        return "true".equals(repository.getDescriptor(Repository.QUERY_XPATH_DOC_ORDER));
    }

    /**
     * Escapes the Jcr names using ISO 9075 encoding.
     * @param decoded
     * @return
     */
    public static String encode(String decoded) {
        return escaper.encode(decoded);
    }

    /**
     * Decodes the Jcr names using ISO 9075 decoding.
     * @param encoded
     * @return
     */
    public static String decode(String encoded) {
        return escaper.decode(encoded);
    }

}
