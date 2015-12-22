/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.common;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.utils.ParsingUtils;
import com.esofthead.mycollab.utils.ParsingUtils.InvalidEmailException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MailParsingTest {

    @Test
    public void testValidEmail() throws InvalidEmailException {
        String validEmails = "Hai Nguyen <hainguyen@esofthead.com>, b@a.com";
        List<MailRecipientField> emailFields = ParsingUtils.parseEmailField(validEmails);
        Assert.assertEquals(2, emailFields.size());
    }

    @Test
    public void testValidEmail2() throws InvalidEmailException {
        String validEmails = "Hai Nguyen <hainguyen@esofthead.com>";
        List<MailRecipientField> emailFields = ParsingUtils.parseEmailField(validEmails);
        Assert.assertEquals(1, emailFields.size());
    }

    @Test
    public void testValidEmail3() throws InvalidEmailException {
        String validEmails = "a@a.com, b@b.com";
        List<MailRecipientField> emailFields = ParsingUtils.parseEmailField(validEmails);
        Assert.assertEquals(2, emailFields.size());
    }

    @Test
    public void testValidEmail4() throws InvalidEmailException {
        String validEmails = " b@b.com";
        List<MailRecipientField> emailFields = ParsingUtils.parseEmailField(validEmails);
        Assert.assertEquals(1, emailFields.size());
    }

    @Test
    public void testValidEmail5() throws InvalidEmailException {
        String validEmails = "y@y.co, Hai Nguyen <hainguyen@esofthead.com>, b <mkyong@gmail.co>";
        List<MailRecipientField> emailFields = ParsingUtils
                .parseEmailField(validEmails);
        Assert.assertEquals(3, emailFields.size());
    }

    private static final String[] VALID_SINGLE_EMAIL = new String[]{
            "mkyong@yahoo.com", "mkyong-100@yahoo.com", "mkyong.100@yahoo.com",
            "mkyong111@mkyong.com", "mkyong-100@mkyong.net",
            "mkyong.100@mkyong.com.au", "mkyong@1.com", "mkyong@gmail.com.com",
            "mkyong+100@gmail.com", "mkyong-100@yahoo-test.com"};

    @Test
    public void testValidEmail6() throws InvalidEmailException {
        int totalValid = 0;
        for (String temp : VALID_SINGLE_EMAIL) {
            List<MailRecipientField> emailFields = ParsingUtils
                    .parseEmailField(temp);
            if (emailFields.size() == 1) {
                totalValid++;
            }
        }
        Assert.assertEquals(10, totalValid);
    }

    @Test
    public void testValidEmail7() throws InvalidEmailException {
        String validEmails = " <b@b.com>";
        List<MailRecipientField> emailFields = ParsingUtils
                .parseEmailField(validEmails);
        Assert.assertEquals(1, emailFields.size());
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail() throws InvalidEmailException {
        String emails = "a@a.com;b@a.com";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail2() throws InvalidEmailException {
        String emails = " ";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail3() throws InvalidEmailException {
        String emails = "a<a.com, hai@a.com";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail4() throws InvalidEmailException {
        String emails = "a>a.com<, hai@a.com";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail5() throws InvalidEmailException {
        String emails = "a<a. >, hai@a.com";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail6() throws InvalidEmailException {
        String emails = "a<.com>, hai@a.com";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail7() throws InvalidEmailException {
        String emails = "a@a.com ; b@g.com, b <b@a.com>";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail8() throws InvalidEmailException {
        String emails = "a <a@a.com> ? b <b@a.com>";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail9() throws InvalidEmailException {
        String validEmails = "Hai Nguyen <hainguyen@esofthead.com>, b b@a.com>";
        ParsingUtils.parseEmailField(validEmails);
    }

    private static final String[] INVALID_SINGLE_EMAIL = new String[]{
            "mkyong", "mkyong@.com.my", "mkyong123@gmail.a", "mkyong123@.com",
            "mkyong123@.com.com", ".mkyong@mkyong.com", "mkyong()*@gmail.com",
            "mkyong@%*.com", "mkyong..2002@gmail.com", "mkyong.@gmail.com",
            "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a"};

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail10() throws InvalidEmailException {
        for (String temp : INVALID_SINGLE_EMAIL) {
            ParsingUtils.parseEmailField(temp);
        }
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail11() throws InvalidEmailException {
        String emails = "a@a.com ? b@y.co";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail12() throws InvalidEmailException {
        String emails = "a@a.com ? b <b@y.co>";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail13() throws InvalidEmailException {
        String emails = "a@a.com , b <b@y.co> ; abc@y.co";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail14() throws InvalidEmailException {
        String emails = "a@a.com , b b@y.co> , abc@y.co";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail15() throws InvalidEmailException {
        String emails = "a@a.com , b <b@y.co , abc@y.co";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail16() throws InvalidEmailException {
        String emails = "a@a.com  b <b@y.co , abc@y.co";
        ParsingUtils.parseEmailField(emails);
    }

    @Test(expected = InvalidEmailException.class)
    public void testInvalidEmail17() throws InvalidEmailException {
        String emails = "Hai Nguyen <<a@a.com>";
        ParsingUtils.parseEmailField(emails);
    }
}
