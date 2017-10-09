package com.mycollab.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
public class FormatHtmlTest {
    @Test
    public void testFormatHtml() {
        String mixTextAndHtml = StringUtils.formatRichText("Hello world https://community.mycollab.com <b>Hai Nguyen</b>");
        Assert.assertEquals("Hello world \n" +
                "<a href=\"https://community.mycollab.com\" target=\"_blank\">https://community.mycollab.com</a> \n" +
                "<b>Hai Nguyen</b>", mixTextAndHtml);

        String pureHtml = StringUtils.formatRichText("https://mycollab.com");
        Assert.assertEquals("<a href=\"https://mycollab.com\" target=\"_blank\">https://mycollab.com</a>", pureHtml);
    }
}
