package com.mycollab.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author MyCollab Ltd.
 * @since 5.0.6
 */
public class VersionTest {
    @Test
    public void testHigherVersion() {
        Assert.assertFalse(Version.isEditionNewer("5.0.5", "5.0.6"));
        Assert.assertFalse(Version.isEditionNewer("5.0.5", "5.0.5"));
        Assert.assertTrue(Version.isEditionNewer("5.0.5", "5.0.4"));
        Assert.assertTrue(Version.isEditionNewer("6.0.0", "5.0.4"));
    }
}
