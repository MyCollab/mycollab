/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.core;

import org.joda.time.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class Version {
    public static final String THEME_VERSION = "mycollab_20161011";
    public static final String THEME_MOBILE_VERSION = "mycollab_20161010";

    public static String getVersion() {
        return "5.4.3";
    }

    public static DateTime getReleasedDate() {
        return new DateTime(2016, 10, 17, 0, 0, 0);
    }

    static int[] getVersionNumbers(String ver) {
        Matcher m = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(beta(\\d*))?").matcher(ver);
        if (!m.matches())
            throw new IllegalArgumentException("Malformed FW version");

        return new int[]{Integer.parseInt(m.group(1)),  // majorMain
                Integer.parseInt(m.group(2)),             // minor
                Integer.parseInt(m.group(3)),             // rev.
                m.group(4) == null ? Integer.MAX_VALUE    // no beta suffix
                        : m.group(5).isEmpty() ? 1        // "beta"
                        : Integer.parseInt(m.group(5))    // "beta3"
        };
    }

    public static boolean isEditionNewer(String testFW) {
        return isEditionNewer(testFW, getVersion());
    }

    /**
     * @param testFW
     * @param baseFW
     * @return true if testFW is greater than baseFW
     */
    public static boolean isEditionNewer(String testFW, String baseFW) {
        try {
            int[] testVer = getVersionNumbers(testFW);
            int[] baseVer = getVersionNumbers(baseFW);

            for (int i = 0; i < testVer.length; i++)
                if (testVer[i] != baseVer[i])
                    return testVer[i] > baseVer[i];

            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
