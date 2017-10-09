package com.mycollab.core;

import org.joda.time.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class Version {
    public static final String THEME_VERSION = "mycollab_20170210";
    public static final String THEME_MOBILE_VERSION = "mycollab_20161111";

    public static String getVersion() {
        return "5.6.0";
    }

    public static DateTime getReleasedDate() {
        return new DateTime(2017, 5, 25, 0, 0, 0);
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
